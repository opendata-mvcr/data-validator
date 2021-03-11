package cz.mvcr.datavalidator.cli;

import cz.mvcr.datavalidator.core.DataValidator;
import cz.mvcr.datavalidator.json.schema.JsonSchemaEveritValidator;
import cz.mvcr.datavalidator.json.syntax.JsonSyntaxJacksonValidator;
import cz.mvcr.datavalidator.rdf.content.RdfContentNotEmptyJenaValidator;
import cz.mvcr.datavalidator.rdf.schema.RdfSchemaShaclJenaValidator;
import cz.mvcr.datavalidator.rdf.syntax.JsonLdSyntaxTitaniumValidator;
import cz.mvcr.datavalidator.rdf.syntax.RdfSyntaxJenaValidator;
import cz.mvcr.datavalidator.xml.schema.XmlSchemaXercesValidator;
import cz.mvcr.datavalidator.xml.syntax.XmlSyntaxDom4jValidator;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ValidatorFactory {

    public static DataValidator createValidator(
            Resource resource, List<Statement> statements) throws IOException {
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
                    return createJacksonJsonSyntax();
                case Vocabulary.Dom4jXmlSyntax:
                    return createDom4jXmlSyntax();
                case Vocabulary.JenaRdfSyntax:
                    return createJenaRdfSyntax();
                case Vocabulary.EveritJsonSchema:
                    return createEveritJsonSchema(resource, statements);
                case Vocabulary.XercesXmlSchema:
                    return createXercesXmlSchema(resource, statements);
                case Vocabulary.JenaRdfSchaclSchema:
                    return createJenaSchemaShacl(resource, statements);
                case Vocabulary.TitaniumJsonLdSyntax:
                    return createTitaniumJsonLdSyntax();
                case Vocabulary.RdfNotEmpty:
                    return createRdfNotEmpty();
                default:
                    break;
            }
        }
        return null;
    }

    private static DataValidator createJacksonJsonSyntax() {
        return new JsonSyntaxJacksonValidator();
    }

    private static DataValidator createDom4jXmlSyntax() {
        return new XmlSyntaxDom4jValidator();
    }

    private static DataValidator createJenaRdfSyntax() {
        return new RdfSyntaxJenaValidator();
    }

    private static DataValidator createEveritJsonSchema(
            Resource resource, List<Statement> statements) throws IOException {
        var validator = new JsonSchemaEveritValidator();
        validator.configure(resource, statements);
        return validator;
    }

    private static DataValidator createXercesXmlSchema(
            Resource resource, List<Statement> statements) {
        var validator = new XmlSchemaXercesValidator();
        validator.configure(resource, statements);
        return validator;
    }

    private static DataValidator createJenaSchemaShacl(
            Resource resource, List<Statement> statements) throws IOException {
        var validator = new RdfSchemaShaclJenaValidator();
        validator.configure(resource, statements);
        return validator;
    }

    private static DataValidator createTitaniumJsonLdSyntax() {
        return new JsonLdSyntaxTitaniumValidator();
    }

    private static  DataValidator createRdfNotEmpty() {
        return new RdfContentNotEmptyJenaValidator();
    }

}
