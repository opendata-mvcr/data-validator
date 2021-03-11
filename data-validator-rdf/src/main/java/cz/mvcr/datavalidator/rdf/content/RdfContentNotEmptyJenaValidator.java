package cz.mvcr.datavalidator.rdf.content;

import com.apicatalog.jsonld.api.JsonLdError;
import cz.mvcr.datavalidator.core.ConfigurableValidator;
import cz.mvcr.datavalidator.core.Report;
import cz.mvcr.datavalidator.core.ReportFactory;
import cz.mvcr.datavalidator.rdf.syntax.JsonLdTitaniumLoader;
import org.apache.jena.graph.Graph;
import org.apache.jena.riot.RDFDataMgr;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class RdfContentNotEmptyJenaValidator implements ConfigurableValidator {

    private static final ReportFactory reportFactory =
            ReportFactory.getInstance(RdfContentNotEmptyJenaValidator.class);

    @Override
    public void configure(Resource resource, List<Statement> statements) {
        // There is no configuration.
    }

    @Override
    public List<Report> validate(File file) {
        Graph fileContent;
        try {
            fileContent = loadGraph(file);
        } catch (Throwable error) {
            return Collections.singletonList(
                    reportFactory.error("Can't read file."));
        }
        if (fileContent.isEmpty()) {
            return Collections.singletonList(reportFactory.error(
                    "There are no RDF statements in the file."
            ));
        }
        return Collections.emptyList();
    }

    private Graph loadGraph(File file) throws JsonLdError {
        if (file.toString().toLowerCase().endsWith(".jsonld")) {
            JsonLdTitaniumLoader loader = new JsonLdTitaniumLoader();
            return loader.loadGraph(file);
        } else {
            return RDFDataMgr.loadGraph(file.getAbsolutePath());
        }
    }

}
