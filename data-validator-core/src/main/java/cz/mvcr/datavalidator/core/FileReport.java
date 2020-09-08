package cz.mvcr.datavalidator.core;

import java.io.File;
import java.nio.file.Path;

public class FileReport extends Report {

    public final Path relativePath;

    public final File absolutePath;

    protected FileReport(
            String validator,
            Integer line, Integer column, String message, Type type,
            File root, File absolutePath) {
        super(validator, line, column, message, type);
        this.relativePath = root.toPath().relativize(absolutePath.toPath());
        this.absolutePath = absolutePath;
    }

    public static FileReport file(Report report, File root, File absolutePath) {
        return new FileReport(
                report.validator,
                report.line, report.column, report.message, report.type,
                root, absolutePath);
    }

}
