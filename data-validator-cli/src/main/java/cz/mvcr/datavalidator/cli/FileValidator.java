package cz.mvcr.datavalidator.cli;

import cz.mvcr.datavalidator.core.DataValidator;

public class FileValidator {

    private final String pattern;

    private final DataValidator validator;

    public FileValidator(String pattern, DataValidator validator) {
        this.pattern = pattern;
        this.validator = validator;
    }

    public String getPattern() {
        return pattern;
    }

    public DataValidator getValidator() {
        return validator;
    }

}
