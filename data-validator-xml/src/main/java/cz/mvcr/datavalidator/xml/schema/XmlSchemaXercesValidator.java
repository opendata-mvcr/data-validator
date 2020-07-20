package cz.mvcr.datavalidator.xml.schema;

import cz.mvcr.datavalidator.core.ConfigurableValidator;
import cz.mvcr.datavalidator.core.Report;
import cz.mvcr.datavalidator.core.ReportFactory;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlSchemaXercesValidator implements ConfigurableValidator {

    private static final String HAS_SCHEMA = "urn:schemaUrl";

    private static final Logger LOG =
            LoggerFactory.getLogger(XmlSchemaXercesValidator.class);

    private static final ReportFactory reportFactory =
            ReportFactory.getInstance(XmlSchemaXercesValidator.class);

    private final SchemaFactory schemaFactory;

    private final Map<String, Schema> schemaCache = new HashMap<>();

    /**
     * Schemas to apply to every file.
     */
    private final List<String> extraSchemas = new ArrayList<>();

    private final DetectSchemaLocation schemaDetector =
            new DetectSchemaLocation();

    public XmlSchemaXercesValidator() {
        // Utilize xercesImpl-xsd11
        schemaFactory = SchemaFactory.newInstance(
                "http://www.w3.org/XML/XMLSchema/v1.1");
        schemaFactory.setResourceResolver(new UrlEncodingResourceResolver());
    }

    @Override
    public void configure(
            Resource resource, List<Statement> statements) {
        for (Statement statement : statements) {
            if (!statement.getSubject().equals(resource)) {
                continue;
            }
            Value value = statement.getObject();
            switch (statement.getPredicate().stringValue()) {
                case HAS_SCHEMA:
                    extraSchemas.add(value.stringValue());
                    break;
                default:
                    break;
            }
        }
    }

    public void addSchema(File file) {
        addSchema(file.getAbsolutePath());
    }

    public void addSchema(String location) {
        extraSchemas.add(location);
    }

    @Override
    public List<Report> validate(File file) {
        List<String> schemas;
        try {
            schemas = schemaDetector.detectSchemas(file);
        } catch (IOException | SAXException | ParserConfigurationException ex) {
            return Collections.singletonList(
                    reportFactory.error(ex.getMessage()));
        }
        LOG.debug("Detected {} schemas for '{}'", schemas.size(), file);
        schemas.addAll(extraSchemas);
        List<Report> result = new ArrayList<>();
        for (String schemaLocation : schemas) {
            result.addAll(validateFile(file, schemaLocation));
        }
        return result;
    }

    private List<Report> validateFile(File file, String schemaLocation) {
        Schema schema;
        try {
            schema = loadSchema(schemaLocation);
        } catch (SAXException | IOException ex) {
            return Collections.singletonList(
                    reportFactory.error(ex.getMessage()));
        }
        Validator validator = schema.newValidator();
        ErrorHandlerReportCollector collector =
                new ErrorHandlerReportCollector(reportFactory);
        validator.setErrorHandler(collector);
        StreamSource streamSource = new StreamSource(file);
        try {
            validator.validate(streamSource);
        } catch (SAXException | IOException ex) {
            return Collections.singletonList(
                    reportFactory.error(ex.getMessage()));
        }
        return collector.getReports();
    }

    private Schema loadSchema(String location)
            throws SAXException, IOException {
        if (schemaCache.containsKey(location)) {
            return schemaCache.get(location);
        }
        //
        Schema result;
        if (location.toLowerCase().startsWith("http://")
                || location.toLowerCase().startsWith("https://")) {
            result = schemaFactory.newSchema(createUrl(location));
        } else {
            result = schemaFactory.newSchema(new File(location));
        }
        schemaCache.put(location, result);
        return result;
    }

    private static URL createUrl(String location) throws MalformedURLException {
        return new URL(URI.create(location).toASCIIString());
    }

}
