package cz.mvcr.datavalidator.xml.syntax;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import cz.mvcr.datavalidator.core.Report;
import cz.mvcr.datavalidator.core.Validator;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class XmlSyntaxJacksonValidator implements Validator {

    @Override
    public List<Report> validate(File file) {
        ObjectMapper mapper = new ObjectMapper();
        Object value;
        try {
            value = mapper.readTree(file);
        } catch (JsonParseException ex) {
            JsonLocation location = ex.getLocation();
            String message = ex.getMessage();
            return Collections.singletonList(Report.error(
                    message, location.getLineNr(), location.getColumnNr()));
        } catch (IOException ex) {
            return Collections.singletonList(Report.error(ex.getMessage()));
        }
        if (value == null || value instanceof MissingNode) {
            return Collections.singletonList(Report.error("File is empty!"));
        }
        return Collections.emptyList();
    }

}
