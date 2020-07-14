package cz.mvcr.datavalidator.cli;

import cz.mvcr.datavalidator.core.FileReport;
import cz.mvcr.datavalidator.core.Validator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class AppEntry {

    public static void main(String[] args) {
        (new AppEntry()).run(args);
    }

    protected void run(String[] args) {
        Configuration configuration;
        try {
            CommandLine commandLine = parseArgs(args);
            configuration = loadConfiguration(commandLine);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }
        List<File> files;
        try {
            files = listFiles(configuration);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        List<FileReport> reports = validateFiles(configuration, files);
        report(reports);
    }

    private CommandLine parseArgs(String[] args) throws ParseException {
        Options options = new Options();

        Option configuration = new Option(
                "c", "configuration", true, "Url of a configuration.");
        configuration.setRequired(false);
        options.addOption(configuration);

        Option file = new Option(
                "f", "file", true, "Input file or directory..");
        file.setRequired(false);
        options.addOption(file);

        Option recursive = new Option(
                null, "recursive", false, "Enable recursive scan.");
        recursive.setRequired(false);
        options.addOption(recursive);

        Option nonRecursive = new Option(
                null, "non-recursive", false, "Disable recursive scan.");
        nonRecursive.setRequired(false);
        options.addOption(nonRecursive);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        try {
            return parser.parse(options, args);
        } catch (ParseException ex) {
            formatter.printHelp("utility-name", options);
            throw ex;
        }
    }

    private Configuration loadConfiguration(CommandLine cmd)
            throws IOException {
        URL url = new URL(cmd.getOptionValue("configuration"));
        Configuration configuration = ConfigurationAdapter.load(url);
        //
        if (cmd.hasOption("file")) {
            configuration.paths.clear();
            configuration.paths.add(new File(cmd.getOptionValue("file")));
        }
        if (cmd.hasOption("recursive")) {
            configuration.recursive = true;
        }
        if (cmd.hasOption("non-recursive")) {
            configuration.recursive = false;
        }
        return configuration;
    }

    private List<File> listFiles(Configuration configuration)
            throws IOException {
        List<File> result = new ArrayList<>();
        for (File file : configuration.paths) {
            if (!file.isDirectory()) {
                result.add(file);
            }
            int depth = configuration.recursive ? Integer.MAX_VALUE : 1;
            try (Stream<Path> paths = Files.walk(file.toPath(), depth)) {
                paths.filter(Files::isRegularFile)
                        .map(Path::toFile)
                        .forEach(result::add);
            }
        }
        return result;
    }

    private List<FileReport> validateFiles(
            Configuration configuration, List<File> files) {
        List<FileReport> result = new ArrayList<>();
        for (Configuration.Rule rule : configuration.rules) {
            for (File file : files) {
                result.addAll(validateFile(rule, file));
            }
        }
        return result;
    }

    private List<FileReport> validateFile(Configuration.Rule rule, File file) {
        for (String filePattern : rule.filePatterns) {
            if (!file.getAbsolutePath().matches(filePattern)) {
                continue;
            }
            List<FileReport> result = new ArrayList<>();
            for (Validator validator : rule.validators) {
                validator.validate(file)
                        .stream()
                        .map(report -> FileReport.file(report, file))
                        .forEach(result::add);
            }
            return result;
        }
        return Collections.emptyList();
    }

    private void report(List<FileReport> reports) {
        StringBuilder line = new StringBuilder();
        for (FileReport report : reports) {
            line.setLength(0);
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
