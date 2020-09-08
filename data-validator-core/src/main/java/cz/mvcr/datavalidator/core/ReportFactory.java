package cz.mvcr.datavalidator.core;

public class ReportFactory {

    private final String validator;

    public ReportFactory(String validator) {
        this.validator = validator;
    }

    public Report error(String message) {
        return new Report(validator, null, null, message, Report.Type.ERROR);
    }

    public Report error(
            String message, Integer line, Integer column) {
        return new Report(validator, line, column, message, Report.Type.ERROR);
    }

    public Report warn(String message) {
        return new Report(validator, null, null, message, Report.Type.WARNING);
    }

    public Report warn(String message, Integer line, Integer column) {
        return new Report(
                validator.getClass().getName(),
                line, column, message, Report.Type.WARNING);
    }

    public Report message(Report.Type type, String message) {
        return new Report(validator, null, null, message, type);
    }

    public static ReportFactory getInstance(Class<?> validatorClass) {
        return new ReportFactory(validatorClass.getSimpleName());
    }

}
