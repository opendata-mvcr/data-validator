package cz.mvcr.datavalidator.xml.schema;

import cz.mvcr.datavalidator.core.Report;
import cz.mvcr.datavalidator.xml.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class XmlSchemaXercesValidatorTest {

    @Test
    public void invalidFile000() {
        XmlSchemaXercesValidator validator = new XmlSchemaXercesValidator();
        validator.addSchema(
                TestUtils.fileFromResource("schema/schema-000.xsd"));
        List<Report> actual = validator.validate(
                TestUtils.fileFromResource("schema/invalid-000.xml"));
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    public void invalidFile001() {
        XmlSchemaXercesValidator validator = new XmlSchemaXercesValidator();
        List<Report> actual = validator.validate(
                TestUtils.fileFromResource("schema/invalid-001.xml"));
        // We got two errors for a single error.
        Assertions.assertEquals(2, actual.size());
    }

    @Test
    public void validFile000() {
        XmlSchemaXercesValidator validator = new XmlSchemaXercesValidator();
        List<Report> actual = validator.validate(
                TestUtils.fileFromResource("schema/valid-000.xml"));
        Assertions.assertEquals(0, actual.size());
    }

}
