package cz.mvcr.datavalidator.rdf.schema;

import com.apicatalog.jsonld.api.JsonLdError;
import cz.mvcr.datavalidator.core.ConfigurableValidator;
import cz.mvcr.datavalidator.core.Report;
import cz.mvcr.datavalidator.core.ReportFactory;
import cz.mvcr.datavalidator.rdf.syntax.JsonLdTitaniumLoader;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.GraphUtil;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.mem.GraphMem;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.engine.constraint.ShNode;
import org.apache.jena.shacl.parser.Constraint;
import org.apache.jena.shacl.parser.Shape;
import org.apache.jena.shacl.validation.ReportEntry;
import org.apache.jena.shacl.validation.Severity;
import org.apache.jena.sparql.core.Prologue;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RdfSchemaShaclJenaValidator implements ConfigurableValidator {

    private static final String HAS_SHACL_URL = "urn:shaclUrl";

    private static final ReportFactory reportFactory =
            ReportFactory.getInstance(RdfSchemaShaclJenaValidator.class);

    private Shapes shapes;

    @Override
    public void configure(Resource resource, List<Statement> statements)
            throws IOException {
        for (Statement statement : statements) {
            if (!statement.getSubject().equals(resource)) {
                continue;
            }
            Value value = statement.getObject();
            switch (statement.getPredicate().stringValue()) {
                case HAS_SHACL_URL:
                    loadShaclFromLocation(value.stringValue());
                    break;
                default:
                    break;
            }
        }
        if (shapes == null) {
            throw new IOException(
                    "Missing SHACL (" + HAS_SHACL_URL + ") specification.");
        }
    }

    protected void loadShaclFromLocation(String location) {
        Graph content = RDFDataMgr.loadGraph(location);
        shapes = Shapes.parse(content);
    }

    @Override
    public List<Report> validate(File file) {
        Graph fileContent;
        try {
            fileContent = loadGraph(file);
        } catch (JsonLdError error) {
            return Collections.singletonList(
                    reportFactory.error("Can't read file."));
        }
        ValidationReport reports =
                ShaclValidator.get().validate(shapes, fileContent);

        List<Report> result = new ArrayList<>();
        for (ReportEntry entry : reports.getEntries()) {
            Report.Type type = getType(entry.severity());
            if (type == null) {
                continue;
            }
            Constraint constraint = entry.constraint();
            if (constraint instanceof ShNode) {
                result.add(createReport(entry, type));
            } else {
                result.add(reportFactory.message(type, entry.message()));
            }
        }
        return result;
    }

    private Graph loadGraph(File file) throws JsonLdError {
        if (file.toString().toLowerCase().endsWith(".jsonld")) {
            JsonLdTitaniumLoader loader = new JsonLdTitaniumLoader();
            return loader.loadGraph(file);
        } else {
            return RDFDataMgr.loadGraph(file.getAbsolutePath());
        }
    }

    private Report.Type getType(Severity severity) {
        if (severity == Severity.Warning) {
            return Report.Type.WARNING;
        } else if (severity == Severity.Violation) {
            return Report.Type.ERROR;
        }
        return null;
    }

    private Report createReport(ReportEntry entry, Report.Type type) {
        String focus = entry.focusNode().getURI();
        String path = entry.resultPath().toString(new Prologue());
        String value = entry.value().toString();
        String message =
                "Constraints " + String.join(" ", getShapeDescriptions(entry))
                        + " dos not hold for "
                        + "<" + focus + "> <" + path + "> " + value + " .";
        return reportFactory.message(type, message);
    }

    private List<String> getShapeDescriptions(ReportEntry entry) {
        var iter = shapes.getGraph().find(
                entry.source(),
                NodeFactory.createURI("http://www.w3.org/ns/shacl#description"),
                Node.ANY);
        List<String> result = new ArrayList<>();
        while (iter.hasNext()) {
            result.add(iter.next().getObject().toString());
        }
        return result;
    }

}
