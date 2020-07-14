package cz.mvcr.datavalidator.cli;

import cz.mvcr.datavalidator.core.Validator;

public class FileValidator {

    private final String pattern;

    private final Validator validator;

    public FileValidator(String pattern, Validator validator) {
        this.pattern = pattern;
        this.validator = validator;
    }

    public String getPattern() {
        return pattern;
    }

    public Validator getValidator() {
        return validator;
    }

}
