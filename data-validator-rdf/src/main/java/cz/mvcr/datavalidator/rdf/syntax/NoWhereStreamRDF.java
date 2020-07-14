package cz.mvcr.datavalidator.rdf.syntax;

import org.apache.jena.graph.Triple;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.sparql.core.Quad;

public class NoWhereStreamRDF implements StreamRDF {

    private long size = 0;

    @Override
    public void start() {
        size = 0;
    }

    @Override
    public void triple(Triple triple) {
        size += 1;
    }

    @Override
    public void quad(Quad quad) {
        size += 1;
    }

    @Override
    public void base(String base) {
        // No action.
    }

    @Override
    public void prefix(String prefix, String iri) {
        // No action.
    }

    @Override
    public void finish() {
        // No action.
    }

}
