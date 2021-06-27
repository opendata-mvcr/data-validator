package cz.mvcr.datavalidator.cli;

import cz.mvcr.datavalidator.cli.writer.StdOutReportWriter;
import cz.mvcr.datavalidator.core.FileReport;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AppEntry {

    private static final Logger LOG = LoggerFactory.getLogger(AppEntry.class);

    public static void main(String[] args) {
        Instant start = Instant.now();
        int returnCode;
        try {
            returnCode = (new AppEntry()).run(args);
        } catch (Throwable t) {
            LOG.error("Unexpected execution failure.", t);
            returnCode = 1;
        }
        LOG.debug("Execution finished in {} s",
                Duration.between(start, Instant.now()).toSeconds());
        System.exit(returnCode);
    }

    protected int run(String[] args) {
        setUtf8OutputEncoding();
        Configuration configuration;
        try {
            CommandLine commandLine = parseArgs(args);
            configuration = loadConfiguration(commandLine);
        } catch (Exception ex) {
            System.out.println("Can't load configuration.");
            LOG.info("Exception.", ex);
            return 1;
        }
        List<FileReport> reports = new ArrayList<>();
        FileValidator validator = new FileValidator(configuration);
        for (File file : configuration.paths) {
            LOG.debug("Validating input '{}'", file);
            reports.addAll(validator.validateFile(file));
        }
        LOG.debug("Validation completed.");
        (new StdOutReportWriter()).writeReports(reports);
        return reports.size() > 0 ? 1 : 0;
    }

    private void setUtf8OutputEncoding() {
        System.setOut(new PrintStream(
                new FileOutputStream(FileDescriptor.out),
                true,
                StandardCharsets.UTF_8));
    }

    private CommandLine parseArgs(String[] args) throws ParseException {
        Options options = new Options();

        Option configuration = new Option(
                "c", "configuration", true, "Url of a configuration.");
        configuration.setRequired(false);
        options.addOption(configuration);

        Option file = new Option(
                "p", "path", true, "Input file or directory.");
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
        Configuration configuration;
        if (cmd.hasOption("configuration")) {
            String parameter = cmd.getOptionValue("configuration");
            if (parameter.startsWith("file://")) {
                String path = parameter.substring("file://".length());
                configuration = ConfigurationAdapter.load(new File(path));
            } else {
                configuration = ConfigurationAdapter.load(new URL(parameter));
            }
        } else {
            configuration = ConfigurationAdapter.createDefaultConfiguration();
        }
        //
        if (cmd.hasOption("path")) {
            configuration.paths.clear();
            configuration.paths.add(new File(cmd.getOptionValue("path")));
        }
        if (cmd.hasOption("recursive")) {
            configuration.recursive = true;
        }
        if (cmd.hasOption("non-recursive")) {
            configuration.recursive = false;
        }
        return configuration;
    }

}
