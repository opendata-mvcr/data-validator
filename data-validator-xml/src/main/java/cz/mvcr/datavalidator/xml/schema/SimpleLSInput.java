package cz.mvcr.datavalidator.xml.schema;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.ls.LSInput;

import javax.naming.OperationNotSupportedException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SimpleLSInput implements LSInput {

    private String baseURI;

    private String publicId;

    private String systemId;

    private URL url;

    public SimpleLSInput(
            String baseURI, String publicId, String systemId, URL url) {
        this.baseURI = baseURI;
        this.publicId = publicId;
        this.systemId = systemId;
        this.url = url;
    }

    @Override
    public Reader getCharacterStream() {
        return new StringReader(getStringData());
    }

    @Override
    public void setCharacterStream(Reader characterStream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getByteStream() {
        try {
            return url.openStream();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void setByteStream(InputStream byteStream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStringData() {
        try {
            return IOUtils.toString(url, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void setStringData(String stringData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSystemId() {
        return systemId;
    }

    @Override
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    @Override
    public String getPublicId() {
        return publicId;
    }

    @Override
    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    @Override
    public String getBaseURI() {
        return baseURI;
    }

    @Override
    public void setBaseURI(String baseURI) {
        this.baseURI = baseURI;
    }

    @Override
    public String getEncoding() {
        return StandardCharsets.UTF_8.name();
    }

    @Override
    public void setEncoding(String encoding) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getCertifiedText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCertifiedText(boolean certifiedText) {
        throw new UnsupportedOperationException();
    }

}
