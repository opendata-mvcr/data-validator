package cz.mvcr.datavalidator.rdf.schema;

import cz.mvcr.datavalidator.core.ConfigurableValidator;
import cz.mvcr.datavalidator.core.Report;
import cz.mvcr.datavalidator.core.ReportFactory;
import org.apache.jena.graph.Graph;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.validation.ReportEntry;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        Graph fileContent = RDFDataMgr.loadGraph(file.getAbsolutePath());
        ValidationReport reports =
                ShaclValidator.get().validate(shapes, fileContent);
        List<Report> result = new ArrayList<>();
        for (ReportEntry entry : reports.getEntries()) {
            // TODO Create new report type.
            result.add(reportFactory.error(entry.message()));
        }
        return result;
    }

}
