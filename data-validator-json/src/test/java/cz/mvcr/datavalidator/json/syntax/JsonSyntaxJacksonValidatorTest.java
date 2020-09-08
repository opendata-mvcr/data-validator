package cz.mvcr.datavalidator.json.syntax;

import cz.mvcr.datavalidator.core.Report;
import cz.mvcr.datavalidator.json.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class JsonSyntaxJacksonValidatorTest {

    @Test
    public void invalidFile000() {
        JsonSyntaxJacksonValidator validator = new JsonSyntaxJacksonValidator();
        List<Report> actual = validator.validate(
                TestUtils.fileFromResource("syntax/invalid-000.json"));
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    public void invalidFile001() {
        JsonSyntaxJacksonValidator validator = new JsonSyntaxJacksonValidator();
        List<Report> actual = validator.validate(
                TestUtils.fileFromResource("syntax/invalid-001.json"));
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    public void invalidFile002() {
        JsonSyntaxJacksonValidator validator = new JsonSyntaxJacksonValidator();
        List<Report> actual = validator.validate(
                TestUtils.fileFromResource("syntax/invalid-002.json"));
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    public void issue004() {
        JsonSyntaxJacksonValidator validator = new JsonSyntaxJacksonValidator();
        List<Report> actual = validator.validate(
                TestUtils.fileFromResource("syntax/issue-004.json"));
        Assertions.assertEquals(1, actual.size());
    }

}
