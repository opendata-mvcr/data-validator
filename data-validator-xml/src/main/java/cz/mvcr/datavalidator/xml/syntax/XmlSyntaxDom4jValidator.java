package cz.mvcr.datavalidator.xml.syntax;

import cz.mvcr.datavalidator.core.Report;
import cz.mvcr.datavalidator.core.DataValidator;
import cz.mvcr.datavalidator.core.ReportFactory;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Alternatives to consider http://www.edankert.com/validate.html
 */
public class XmlSyntaxDom4jValidator implements DataValidator {

    private static final ReportFactory reportFactory =
            ReportFactory.getInstance(XmlSyntaxDom4jValidator.class);

    @Override
    public List<Report> validate(File file) {
        List<Report> result = new ArrayList<>();
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        reader.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) {
                result.add(reportFactory.warn(
                        exception.getMessage(),
                        exception.getLineNumber(),
                        exception.getColumnNumber()));
            }

            @Override
            public void error(SAXParseException exception) {
                result.add(reportFactory.error(
                        exception.getMessage(),
                        exception.getLineNumber(),
                        exception.getColumnNumber()));
            }

            @Override
            public void fatalError(SAXParseException exception) {
                // Do nothing, we get this information in try-catch
                // around read.
            }
        });
        try {
            reader.read(file);
        } catch (DocumentException ex) {
            if (ex.getCause() instanceof SAXParseException) {
                SAXParseException cause = (SAXParseException) ex.getCause();
                result.add(reportFactory.error(
                        ex.getMessage(),
                        cause.getLineNumber(),
                        cause.getColumnNumber()));
            } else {
                result.add(reportFactory.error(ex.getMessage()));
            }
        }
        return result;
    }


}
