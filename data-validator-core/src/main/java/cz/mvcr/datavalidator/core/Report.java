package cz.mvcr.datavalidator.core;

public class Report {

    public enum Type {
        ERROR,
        WARNING
    }

    public final String validator;

    public final Integer line;

    public final Integer column;

    public final String message;

    public final Type type;

    public Report(
            String validator,
            Integer line, Integer column, String message, Type type) {
        this.validator = validator;
        this.line = line;
        this.column = column;
        this.message = message;
        this.type = type;
    }

}
