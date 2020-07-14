package cz.mvcr.datavalidator.rdf.syntax;

import cz.mvcr.datavalidator.core.Report;
import cz.mvcr.datavalidator.core.Validator;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.RiotException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RdfSyntaxJenaValidator implements Validator {

    @Override
    public List<Report> validate(File file) {
        List<Report> result = new ArrayList<>();
        try (InputStream stream = new FileInputStream(file)) {
            RDFParser.create()
                    .source(stream)
                    .lang(RDFLanguages.filenameToLang(file.getName()))
                    .errorHandler(new ReportCollector(result))
                    .parse(new NoWhereStreamRDF());
        } catch (IOException ex) {
            result.add(Report.error(ex.getMessage()));
        } catch (RiotException ex) {
            // We already deal with this in the error handler.
        }
        return result;
    }

}
