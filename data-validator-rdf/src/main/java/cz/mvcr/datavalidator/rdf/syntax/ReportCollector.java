package cz.mvcr.datavalidator.rdf.syntax;

import cz.mvcr.datavalidator.core.Report;
import org.apache.jena.riot.RiotException;
import org.apache.jena.riot.system.ErrorHandler;

import java.util.List;

public class ReportCollector implements ErrorHandler {

    private final List<Report> reports;

    public ReportCollector(List<Report> reports) {
        this.reports = reports;
    }

    @Override
    public void warning(String message, long line, long col) {
        reports.add(Report.warn(message, (int)line, (int)col));
    }

    @Override
    public void error(String message, long line, long col) {
        reports.add(Report.error(message, (int)line, (int)col));
    }

    @Override
    public void fatal(String message, long line, long col) {
        reports.add(Report.error(message, (int)line, (int)col));
        throw new RiotException();
    }

}
