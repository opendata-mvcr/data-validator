package cz.mvcr.datavalidator.cli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ConfigurationAdapterTest {

    @Test
    public void loadConfiguration() throws Exception {
        Configuration configuration = ConfigurationAdapter.load(
                TestUtils.fileFromResource("configuration.ttl"));
        Assertions.assertEquals(1, configuration.paths.size());
        Assertions.assertEquals(new File("./"), configuration.paths.get(0));
        Assertions.assertTrue(configuration.recursive);
        Assertions.assertEquals(1, configuration.rules.size());
        Configuration.Rule rule = configuration.rules.get(0);
        Assertions.assertEquals(2, rule.filePatterns.size());
    }

}
