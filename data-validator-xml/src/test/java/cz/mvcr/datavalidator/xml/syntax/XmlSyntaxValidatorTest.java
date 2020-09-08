package cz.mvcr.datavalidator.xml.syntax;

import cz.mvcr.datavalidator.core.Report;
import cz.mvcr.datavalidator.xml.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class XmlSyntaxValidatorTest {

    @Test
    public void invalidFile000() {
        XmlSyntaxDom4jValidator validator = new XmlSyntaxDom4jValidator();
        List<Report> actual = validator.validate(
                TestUtils.fileFromResource("syntax/invalid-000.xml"));
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    public void invalidFile001() {
        XmlSyntaxDom4jValidator validator = new XmlSyntaxDom4jValidator();
        List<Report> actual = validator.validate(
                TestUtils.fileFromResource("syntax/invalid-001.xml"));
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    public void validFile000() {
        XmlSyntaxDom4jValidator validator = new XmlSyntaxDom4jValidator();
        List<Report> actual = validator.validate(
                TestUtils.fileFromResource("syntax/valid-000.xml"));
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    public void validFile001() {
        XmlSyntaxDom4jValidator validator = new XmlSyntaxDom4jValidator();
        List<Report> actual = validator.validate(
                TestUtils.fileFromResource("syntax/valid-001.xml"));
        Assertions.assertEquals(0, actual.size());
    }

}
