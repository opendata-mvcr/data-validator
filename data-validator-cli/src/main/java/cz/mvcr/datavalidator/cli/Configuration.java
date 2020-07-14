package cz.mvcr.datavalidator.cli;

import cz.mvcr.datavalidator.core.Validator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Configuration {

    public static class Rule {

        /**
         * Regexp used to match files.
         */
        public List<String> filePatterns = new ArrayList<>();

        /**
         * Validators to apply.
         */
        public List<Validator> validators = new ArrayList<>();

    }

    public List<Rule> rules = new ArrayList<>();

    /**
     * Scan directories recursively.
     */
    public boolean recursive = false;

    /**
     * Paths to files or directories.
     */
    public List<File> paths = new ArrayList<>();

}
