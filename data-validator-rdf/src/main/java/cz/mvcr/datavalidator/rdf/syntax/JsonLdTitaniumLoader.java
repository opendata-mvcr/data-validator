package cz.mvcr.datavalidator.rdf.syntax;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.api.JsonLdError;
import com.apicatalog.jsonld.api.impl.ToRdfApi;
import com.apicatalog.rdf.RdfDataset;
import com.apicatalog.rdf.RdfLiteral;
import com.apicatalog.rdf.RdfNQuad;
import com.apicatalog.rdf.RdfResource;
import com.apicatalog.rdf.RdfValue;
import org.apache.jena.graph.Graph;
import org.apache.jena.rdf.model.AnonId;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.io.File;

public class JsonLdTitaniumLoader {

    private Model model;

    public Graph loadGraph(File file) throws JsonLdError {
        return loadModel(file).getGraph();
    }

    public Model loadModel(File file) throws JsonLdError {
        ToRdfApi api = JsonLd.toRdf(file.toURI());
        RdfDataset dataset = api.get();
        model = ModelFactory.createDefaultModel();
        for (RdfNQuad quad : dataset.toList()) {
            Statement statement = model.createStatement(
                    createResource(quad.getSubject()),
                    createProperty(quad.getPredicate()),
                    createValue(quad.getObject()));
            model.add(statement);
        }
        return model;
    }

    private Resource createResource(RdfResource resource) {
        String value = resource.getValue();
        if (resource.isBlankNode()) {
            return model.createResource(new AnonId(value));
        }
        return model.createResource(value);
    }

    private Property createProperty(RdfResource resource) {
        return model.createProperty(resource.getValue());
    }

    private RDFNode createValue(RdfValue value) {
        if (value.isBlankNode()) {
            return model.createResource(new AnonId(value.getValue()));
        } else if (value.isIRI()) {
            return model.createResource(value.getValue());
        }
        RdfLiteral literal = value.asLiteral();
        if (literal.getLanguage().isPresent()) {
            return model.createLiteral(
                    literal.getValue(),
                    literal.getLanguage().get());
        } else {
            return model.createTypedLiteral(
                    literal.getValue(),
                    literal.getDatatype());
        }
    }

}
