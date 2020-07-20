package cz.mvcr.datavalidator.xml.schema;

import cz.mvcr.datavalidator.xml.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DetectSchemaLocationTest {

    @Test
    public void invalidFile001() throws Exception {
        DetectSchemaLocation schemaDetector = new DetectSchemaLocation();
        var actual = schemaDetector.detectSchemas(
                TestUtils.fileFromResource("schema/invalid-001.xml"));
        Assertions.assertEquals(1, actual.size());
        // To check for a value we need to deal with encoding.
    }

}
