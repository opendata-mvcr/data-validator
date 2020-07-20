package cz.mvcr.datavalidator.json.schema;

import cz.mvcr.datavalidator.core.Report;
import cz.mvcr.datavalidator.json.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.util.List;

public class JsonSchemaEveritValidatorTest {

    public static class Validator extends JsonSchemaEveritValidator {

        @Override
        public void loadSchemaFromString(String schemaAsString) {
            super.loadSchemaFromString(schemaAsString);
        }
    }

    @Test
    public void invalidFile000() throws Exception {
        Validator validator = new Validator();
        validator.loadSchemaFromString(Files.readString(
                TestUtils.fileFromResource("schema/schema-000.json").toPath()));
        List<Report> actual = validator.validate(
                TestUtils.fileFromResource("schema/invalid-000.json"));
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    public void invalidFile001() throws Exception {
        Validator validator = new Validator();
        validator.loadSchemaFromString(Files.readString(
                TestUtils.fileFromResource("schema/schema-001.json").toPath()));
        List<Report> actual = validator.validate(
                TestUtils.fileFromResource("schema/invalid-001.json"));
        Assertions.assertEquals(1, actual.size());
    }

}
