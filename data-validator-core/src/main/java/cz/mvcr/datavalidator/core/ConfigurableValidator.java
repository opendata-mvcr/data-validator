package cz.mvcr.datavalidator.core;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;

import java.io.IOException;
import java.util.List;

public interface ConfigurableValidator extends DataValidator {

    void configure(Resource resource, List<Statement> statements) throws IOException;

}
