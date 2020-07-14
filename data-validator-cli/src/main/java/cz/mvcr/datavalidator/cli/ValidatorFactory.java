package cz.mvcr.datavalidator.cli;

import cz.mvcr.datavalidator.core.Validator;
import cz.mvcr.datavalidator.json.syntax.JsonSyntaxJacksonValidator;
import cz.mvcr.datavalidator.rdf.syntax.RdfSyntaxJenaValidator;
import cz.mvcr.datavalidator.xml.syntax.XmlSyntaxJacksonValidator;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import java.util.List;
import java.util.stream.Collectors;

public class ValidatorFactory {

    public static Validator createValidator(
            Resource resource, List<Statement> statements) {
        List<String> types = statements.stream()
                .filter(st -> st.getSubject().equals(resource))
                .filter(st -> RDF.TYPE.equals(st.getPredicate()))
                .map(Statement::getObject)
                .filter(value -> value instanceof IRI)
                .map(Value::stringValue)
                .collect(Collectors.toList());
        for (String type : types) {
            switch (type) {
                case Vocabulary.JacksonJsonSyntax:
                    return createJacksonJsonSyntax(resource, statements);
                case Vocabulary.JacksonXmlSyntax:
                    return createJacksonXmlSyntax(resource, statements);
                case Vocabulary.JenaRdfSyntax:
                    return createJenaRdfSyntax(resource, statements);
                default:
                    break;
            }
        }
        return null;
    }

    private static Validator createJacksonJsonSyntax(
            Resource resource, List<Statement> statements) {
        return new JsonSyntaxJacksonValidator();
    }

    private static Validator createJacksonXmlSyntax(
            Resource resource, List<Statement> statements) {
        return new XmlSyntaxJacksonValidator();
    }

    private static Validator createJenaRdfSyntax(
            Resource resource, List<Statement> statements) {
        return new RdfSyntaxJenaValidator();
    }

}
