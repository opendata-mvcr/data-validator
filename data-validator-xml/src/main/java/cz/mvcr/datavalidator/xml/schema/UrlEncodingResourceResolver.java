package cz.mvcr.datavalidator.xml.schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class UrlEncodingResourceResolver implements LSResourceResolver {

    private static final Logger LOG =
            LoggerFactory.getLogger(UrlEncodingResourceResolver.class);

    @Override
    public LSInput resolveResource(
            String type, String namespaceURI,
            String publicId, String systemId, String baseURI) {
        URL url = createUrl(systemId);
        if (url == null) {
            return null;
        }
        LOG.debug("Resolving {}", systemId);
        return new SimpleLSInput(baseURI, publicId, systemId, url);
    }

    private static URL createUrl(String location) {
        try {
            return new URL(URI.create(location).toASCIIString());
        } catch (MalformedURLException ex) {
            return null;
        }
    }

}
