package cz.mvcr.datavalidator.cli;

import cz.mvcr.datavalidator.core.DataValidator;
import cz.mvcr.datavalidator.core.FileReport;
import cz.mvcr.datavalidator.core.Report;
import cz.mvcr.datavalidator.core.ReportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class FileValidator {

    private static final Logger LOG =
            LoggerFactory.getLogger(FileValidator.class);

    private final ReportFactory reportFactory =
            ReportFactory.getInstance(FileValidator.class);

    private final Configuration configuration;

    public FileValidator(Configuration configuration) {
        this.configuration = configuration;
    }

    public List<FileReport> validateFile(File file) {
        if (!file.exists()) {
            return Collections.singletonList(FileReport.file(
                    reportFactory.error("File does not exists."), file, file
            ));
        }
        List<File> files;
        try {
            files = listFiles(file);
        } catch (IOException ex) {
            LOG.error("Can't list files.", ex);
            return Collections.singletonList(FileReport.file(
                    reportFactory.error("Can't list files."), file, file
            ));
        }
        return validateFiles(file, files);
    }

    private List<File> listFiles(File file) throws IOException {
        List<File> result = new ArrayList<>();
        int depth = configuration.recursive ? Integer.MAX_VALUE : 1;
        try (Stream<Path> paths = Files.walk(file.toPath(), depth)) {
            paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(result::add);
        }
        return result;
    }

    private List<FileReport> validateFiles(File root, List<File> files) {
        List<FileReport> result = new ArrayList<>();
        for (Configuration.Rule rule : configuration.rules) {
            for (File file : files) {
                validateFileWithRule(rule, file)
                        .stream()
                        .map(report -> FileReport.file(report, root, file))
                        .forEach(result::add);
            }
        }
        return result;
    }

    private List<Report> validateFileWithRule(
            Configuration.Rule rule, File file) {
        for (String filePattern : rule.filePatterns) {
            if (!file.getAbsolutePath().matches(filePattern)) {
                continue;
            }
            List<Report> result = new ArrayList<>();
            for (DataValidator validator : rule.validators) {
                result.addAll(validator.validate(file));
            }
            return result;
        }
        return Collections.emptyList();
    }


}
