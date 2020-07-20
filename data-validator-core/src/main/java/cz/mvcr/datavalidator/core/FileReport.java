package cz.mvcr.datavalidator.core;

import java.io.File;

public class FileReport extends Report {

    public final File file;

    protected FileReport(
            String validator,
            Integer line, Integer column, String message, Type type,
            File file) {
        super(validator, line, column, message, type);
        this.file = file;
    }

    public static FileReport file(Report report, File file) {
        return new FileReport(
                report.validator,
                report.line, report.column, report.message, report.type,
                file);
    }

}
