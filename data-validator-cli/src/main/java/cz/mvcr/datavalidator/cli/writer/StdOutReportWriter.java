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
            line.append(report.file.getName());
            line.append(" [");
            line.append(report.type);
            line.append("] ");
            line.append(report.line);
            line.append(":");
            line.append(report.column);
            line.append(" ");
            line.append(report.message.replace("\n", ""));
            System.out.println(line.toString());
        }
    }

}