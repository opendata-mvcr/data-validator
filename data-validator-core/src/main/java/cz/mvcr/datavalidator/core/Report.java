package cz.mvcr.datavalidator.core;

public class Report {

    public enum Type {
        ERROR,
        WARNING
    }

    public final Integer line;

    public final Integer column;

    public final String message;

    public final Type type;

    protected Report(Integer line, Integer column, String message, Type type) {
        this.line = line;
        this.column = column;
        this.message = message;
        this.type = type;
    }

    public static Report error(String message) {
        return new Report(null, null, message, Type.ERROR);
    }

    public static Report error(String message, Integer line, Integer column) {
        return new Report(line, column, message, Type.ERROR);
    }

    public static Report warn(String message, Integer line, Integer column) {
        return new Report(line, column, message, Type.WARNING);
    }

}
