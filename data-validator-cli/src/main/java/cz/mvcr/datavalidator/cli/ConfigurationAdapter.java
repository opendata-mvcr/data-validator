package cz.mvcr.datavalidator.cli;

import cz.mvcr.datavalidator.core.RdfAdapter;
import cz.mvcr.datavalidator.core.DataValidator;
import cz.mvcr.datavalidator.json.syntax.JsonSyntaxJacksonValidator;
import cz.mvcr.datavalidator.rdf.syntax.RdfSyntaxJenaValidator;
import cz.mvcr.datavalidator.xml.schema.XmlSchemaXercesValidator;
import cz.mvcr.datavalidator.xml.syntax.XmlSyntaxJacksonValidator;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigurationAdapter {

    public static Configuration load(URL url) throws IOException {
        List<Statement> statements = RdfAdapter.asStatements(url);
        Resource resource = getConfiguration(statements);
        return (new ConfigurationAdapter())
                .onConfiguration(resource, statements);
    }

    private static Resource getConfiguration(List<Statement> statements)
            throws IOException {
        List<Resource> configurations = statements.stream()
                .filter(st -> RDF.TYPE.equals(st.getPredicate()))
                .filter(st -> Vocabulary.Configuration.equals(
                        st.getObject().stringValue()))
                .map(Statement::getSubject)
                .collect(Collectors.toList());
        if (configurations.size() == 0) {
            throw new IOException("No configuration detected.");
        }
        if (configurations.size() > 1) {
            throw new IOException("More then one configuration detected.");
        }
        return configurations.get(0);
    }

    private final Map<Resource, DataValidator> validatorMap = new HashMap<>();


    private Configuration onConfiguration(
            Resource resource, List<Statement> statements) throws IOException {
        Configuration result = new Configuration();
        for (Statement statement : statements) {
            if (!statement.getSubject().equals(resource)) {
                continue;
            }
            Value value = statement.getObject();
            switch (statement.getPredicate().stringValue()) {
                case Vocabulary.hasRule:
                    if (statement.getObject() instanceof Resource) {
                        result.rules.add(onRule((Resource) value, statements));
                    }
                    break;
                case Vocabulary.hasPath:
                    if (statement.getObject() instanceof Literal) {
                        result.paths.add(new File(value.stringValue()));
                    }
                    break;
                case Vocabulary.hasRecursive:
                    if (statement.getObject() instanceof Literal) {
                        result.recursive = ((Literal) value).booleanValue();
                    }
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    private Configuration.Rule onRule(
            Resource resource, List<Statement> statements) throws IOException {
        Configuration.Rule result = new Configuration.Rule();
        for (Statement statement : statements) {
            if (!statement.getSubject().equals(resource)) {
                continue;
            }
            Value value = statement.getObject();
            switch (statement.getPredicate().stringValue()) {
                case Vocabulary.hasPattern:
                    result.filePatterns.add(value.stringValue());
                    break;
                case Vocabulary.hasValidator:
                    if (statement.getObject() instanceof Resource) {
                        onRuleValidator(result, statement, statements);
                    }
                    break;
            }
        }
        return result;
    }

    public void onRuleValidator(
            Configuration.Rule rule, Statement statement,
            List<Statement> statements) throws IOException {
        Resource resource = (Resource) statement.getObject();
        if (!validatorMap.containsKey(resource)) {
            DataValidator validator = ValidatorFactory.createValidator(
                    resource, statements);
            if (validator == null) {
                throw new IOException(
                        "Missing validator type for "
                                + resource.stringValue());
            }
            validatorMap.put(resource, validator);
        }
        rule.validators.add(validatorMap.get(resource));
    }

    public static Configuration load(File file) throws IOException {
        List<Statement> statements = RdfAdapter.asStatements(file);
        Resource resource = getConfiguration(statements);
        return (new ConfigurationAdapter())
                .onConfiguration(resource, statements);
    }

    public static Configuration createDefaultConfiguration() {
        Configuration result = new Configuration();
        result.paths = Collections.singletonList(new File("./"));
        result.recursive = true;

        Configuration.Rule jsonRule = new Configuration.Rule();
        jsonRule.filePatterns.add(".*.json");
        jsonRule.validators.add(new JsonSyntaxJacksonValidator());
        result.rules.add(jsonRule);

        Configuration.Rule xmlRule = new Configuration.Rule();
        xmlRule.filePatterns.add(".*.xml");
        xmlRule.validators.add(new XmlSyntaxJacksonValidator());
        xmlRule.validators.add(new XmlSchemaXercesValidator());
        result.rules.add(xmlRule);

        Configuration.Rule rdfRule = new Configuration.Rule();
        rdfRule.filePatterns.add(".*.ttl");
        rdfRule.filePatterns.add(".*.jsonld");
        rdfRule.validators.add(new RdfSyntaxJenaValidator());
        result.rules.add(rdfRule);

        return result;
    }

}
