package cz.mvcr.datavalidator.cli.writer;

import cz.mvcr.datavalidator.core.FileReport;

import java.util.List;

public class StdOutReportWriter {

    public void writeReports(List<FileReport> reports) {
        StringBuilder line = new StringBuilder();
        for (FileReport report : reports) {
            line.setLength(0);
            line.append(report.validator);
            line.append(" ");
            line.append(report.relativePath.toString());
            line.append(" [");
            line.append(report.type);
            line.append("] ");
            line.append(report.line);
            line.append(":");
            line.append(report.column);
            line.append(" ");
            line.append(report.message.replace("\n", "").replace("\r", ""));
            System.out.println(line.toString());
        }
    }

}
