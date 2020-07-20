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
        Assertions.assertEquals(3, configuration.rules.size());
        for (Configuration.Rule rule : configuration.rules) {
            Assertions.assertEquals(2, rule.filePatterns.size());
        }
    }

}
