package cz.mvcr.datavalidator.rdf.syntax;

import cz.mvcr.datavalidator.core.Report;
import cz.mvcr.datavalidator.core.DataValidator;
import cz.mvcr.datavalidator.core.ReportFactory;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.RiotException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RdfSyntaxJenaValidator implements DataValidator {

    private static final ReportFactory reportFactory =
            ReportFactory.getInstance(RdfSyntaxJenaValidator.class);

    @Override
    public List<Report> validate(File file) {
        List<Report> result = new ArrayList<>();
        try (InputStream stream = new FileInputStream(file)) {
            RDFParser.create()
                    .source(stream)
                    .lang(RDFLanguages.filenameToLang(file.getName()))
                    .errorHandler(new ReportCollector(reportFactory, result))
                    .parse(new NoWhereStreamRDF());
        } catch (IOException ex) {
            result.add(reportFactory.error(ex.getMessage()));
        } catch (RiotException ex) {
            // We already deal with this in the error handler.
        }
        return result;
    }

}
