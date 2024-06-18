package org.unifi.mecvirtualresourceallocation;

import java.util.List;

/**
 * This class represents a hypergraph, which consists of a set of hyperedges and vertices.
 */
public class HyperGraph {
    private List<HyperEdge> edges;
    private List<Vertex> vertices;

    /**
     * Constructs a hypergraph with the specified edges and vertices.
     *
     * @param edges the hyperedges of the hypergraph
     * @param vertices the vertices of the hypergraph
     */
    public HyperGraph(List<HyperEdge> edges, List<Vertex> vertices) {
        this.edges = edges;
        this.vertices = vertices;
    }

    /**
     * Gets the hyperedges of the hypergraph.
     *
     * @return the hyperedges
     */
    public List<HyperEdge> getEdges() {
        return edges;
    }

    /**
     * Sets the hyperedges of the hypergraph.
     *
     * @param edges the new hyperedges
     */
    public void setEdges(List<HyperEdge> edges) {
        this.edges = edges;
    }

    /**
     * Gets the vertices of the hypergraph.
     *
     * @return the vertices
     */
    public List<Vertex> getVertices() {
        return vertices;
    }

    /**
     * Sets the vertices of the hypergraph.
     *
     * @param vertices the new vertices
     */
    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }
}
