package org.unifi.mecvirtualresourceallocation;

import java.util.List;

/**
 * This class represents a hyperedge in a hypergraph.
 * A hyperedge is an edge that can connect more than two vertices.
 */
public class HyperEdge {
    private List<Vertex> vertices;
    private double weight;

    /**
     * Constructs a hyperedge with the specified vertices and weight.
     *
     * @param vertices the vertices of the hyperedge
     * @param weight the weight of the hyperedge
     */
    public HyperEdge(List<Vertex> vertices, double weight) {
        this.vertices = vertices;
        this.weight = weight;
    }

    /**
     * Gets the vertices of the hyperedge.
     *
     * @return the vertices
     */
    public List<Vertex> getVertices() {
        return vertices;
    }

    /**
     * Sets the vertices of the hyperedge.
     *
     * @param vertices the new vertices
     */
    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    /**
     * Gets the weight of the hyperedge.
     *
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the weight of the hyperedge.
     *
     * @param weight the new weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }
}

