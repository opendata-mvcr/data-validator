package cz.mvcr.datavalidator.json.schema;

import cz.mvcr.datavalidator.core.ConfigurableValidator;
import cz.mvcr.datavalidator.core.Report;
import cz.mvcr.datavalidator.core.ReportFactory;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonSchemaEveritValidator implements ConfigurableValidator {

    private static final String HAS_SCHEMA = "urn:schema";

    private static final ReportFactory reportFactory =
            ReportFactory.getInstance(JsonSchemaEveritValidator.class);

    private Schema schema;

    @Override
    public void configure(Resource resource, List<Statement> statements)
            throws IOException {
        for (Statement statement : statements) {
            if (!statement.getSubject().equals(resource)) {
                continue;
            }
            Value value = statement.getObject();
            switch (statement.getPredicate().stringValue()) {
                case HAS_SCHEMA:
                    if (statement.getObject() instanceof IRI) {
                        loadSchemaFromUrl(value.stringValue());
                    } else if (statement.getObject() instanceof Literal) {
                        loadSchemaFromString(value.stringValue());
                    }
                    break;
                default:
                    break;
            }
        }
        if (schema == null) {
            throw new IOException(
                    "Missing schema (" + HAS_SCHEMA + ") specification.");
        }
    }

    protected void loadSchemaFromUrl(String urlAsString) throws IOException {
        URL url = new URL(urlAsString);
        try (InputStream stream = url.openStream()) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(stream));
            schema = SchemaLoader.load(rawSchema);
        }
    }

    protected void loadSchemaFromString(String schemaAsString) {
        JSONObject rawSchema = new JSONObject(new JSONTokener(schemaAsString));
        schema = SchemaLoader.load(rawSchema);
    }

    @Override
    public List<Report> validate(File file) {
        JSONObject fileContent;
        try {
            fileContent = readFileAsJson(file);
        } catch (IOException ex) {
            return Collections.singletonList(
                    reportFactory.error("Can't read file!"));
        }
        try {
            schema.validate(fileContent);
        } catch (ValidationException ex) {
            if (ex.getCausingExceptions().size() == 0) {
                return exceptionsToReports(Collections.singletonList(ex));
            } else {
                return exceptionsToReports(ex.getCausingExceptions());
            }
        }
        return Collections.emptyList();
    }

    private JSONObject readFileAsJson(File file) throws IOException {
        try (Reader reader = Files.newBufferedReader(
                file.toPath(), StandardCharsets.UTF_8)) {
            return new JSONObject(new JSONTokener(reader));
        }
    }

    private List<Report> exceptionsToReports(
            List<ValidationException> exceptions) {
        List<Report> result = new ArrayList<>();
        for (ValidationException exception : exceptions) {
            String pointer = exception.getPointerToViolation();
            String schema = exception.getSchemaLocation();
            String message = exception.getErrorMessage() ;
            // TODO Parse pointer and get location in a file.
            // https://stackoverflow.com/questions/5853087/reading-individual-json-events-using-jackson
            result.add(reportFactory.error(message));
        }
        return result;
    }

}
