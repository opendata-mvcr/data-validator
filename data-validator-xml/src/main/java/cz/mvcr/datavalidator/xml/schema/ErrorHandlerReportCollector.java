package cz.mvcr.datavalidator.xml.schema;

import cz.mvcr.datavalidator.core.Report;
import cz.mvcr.datavalidator.core.ReportFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;
import java.util.List;

public class ErrorHandlerReportCollector implements ErrorHandler {

    private final ReportFactory reportFactory;

    private final List<Report> reports = new ArrayList<>();

    public ErrorHandlerReportCollector(ReportFactory reportFactory) {
        this.reportFactory = reportFactory;
    }

    @Override
    public void warning(SAXParseException exception) {
        reports.add(reportFactory.warn(
                exception.getMessage(),
                exception.getLineNumber(),
                exception.getColumnNumber()));
    }

    @Override
    public void error(SAXParseException exception) {
        reports.add(reportFactory.error(
                exception.getMessage(),
                exception.getLineNumber(),
                exception.getColumnNumber()));
    }

    @Override
    public void fatalError(SAXParseException exception)
            throws SAXException {
        reports.add(reportFactory.error(
                exception.getMessage(),
                exception.getLineNumber(),
                exception.getColumnNumber()));
        throw exception;
    }

    public List<Report> getReports() {
        return reports;
    }

}
