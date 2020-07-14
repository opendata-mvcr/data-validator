package cz.mvcr.datavalidator.core;

import java.io.File;
import java.util.List;

public interface Validator {

    List<Report> validate(File file);

}
