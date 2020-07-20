package cz.mvcr.datavalidator.xml.schema;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetectSchemaLocation extends DefaultHandler {

    private final class StopParser extends SAXException {

    }

    private List<String> schemas = new ArrayList<>();

    private final SAXParserFactory factory;

    public DetectSchemaLocation() {
        this.factory = SAXParserFactory.newInstance();
        // Resolve namespaces.
        factory.setNamespaceAware(true);
    }

    public List<String> detectSchemas(File file)
            throws ParserConfigurationException, SAXException, IOException {
        SAXParser saxParser = factory.newSAXParser();
        try {
            saxParser.parse(file, this);
        } catch (StopParser ex) {
            // Ignore.
        }
        var result = new ArrayList<>(schemas);
        schemas.clear();
        return result;
    }

    @Override
    public void startElement(
            String uri,
            String localName,
            String qName,
            Attributes attributes) throws SAXException {
        String schemaLocationStr = attributes.getValue(
                "http://www.w3.org/2001/XMLSchema-instance",
                "schemaLocation");
        if (schemaLocationStr != null) {
            for (String value : schemaLocationStr.split("\\s+")) {
                addSchema(value);
            }
        }
        throw new StopParser();
    }

    protected void addSchema(String location) {
        if (!location.toLowerCase().endsWith(".xsd")) {
            // It is not a link to a schema.
            return;
        }
        schemas.add(location);
    }

}
