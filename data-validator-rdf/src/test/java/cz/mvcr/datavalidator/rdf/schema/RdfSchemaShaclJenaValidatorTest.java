package cz.mvcr.datavalidator.rdf.schema;

import cz.mvcr.datavalidator.core.Report;
import cz.mvcr.datavalidator.rdf.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RdfSchemaShaclJenaValidatorTest {

    public static class Validator extends RdfSchemaShaclJenaValidator {

        @Override
        public void loadShaclFromLocation(String location) {
            super.loadShaclFromLocation(location);
        }

    }

    @Test
    public void invalidFile000() throws Exception {
        Validator validator = new Validator();
        validator.loadShaclFromLocation(
                TestUtils.fileFromResource("schema/shacl-000.ttl")
                        .getAbsolutePath());
        List<Report> actual = validator.validate(
                TestUtils.fileFromResource("schema/invalid-000.jsonld"));
        Assertions.assertEquals(1, actual.size());
    }

}
