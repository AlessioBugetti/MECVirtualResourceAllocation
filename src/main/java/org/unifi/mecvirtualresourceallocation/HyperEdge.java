package org.unifi.mecvirtualresourceallocation;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a hyperedge in a hypergraph.
 * A hyperedge is an edge that can connect more than two vertices.
 */
public class HyperEdge {
    private String id;
    private List<Vertex> vertices;
    private double weight;

    public HyperEdge(String id) {
        this.id = id;
        this.vertices = new ArrayList<>();
        this.weight = 0;
    }

    /**
     * Constructs a hyperedge with the specified vertices and weight.
     *
     * @param id the id of the hyperedge
     * @param vertices the vertices of the hyperedge
     */
    public HyperEdge(String id, List<Vertex> vertices) {
        this.id = id;
        this.vertices = vertices;
        this.weight = calculateWeight();
    }

    private double calculateWeight() {
        double totalWeight = 0;
        for (Vertex vertex : vertices) {
            totalWeight += vertex.getWeight();
        }
        return totalWeight;
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
     * @param vertex the new vertex
     */
    public void addVertex(Vertex vertex) {
        if (this.vertices == null) {
            this.vertices = new ArrayList<>();
        }
        this.vertices.add(vertex);
    }

    public String getId() {
        return id;
    }

    /**
     * Gets the weight of the hyperedge.
     *
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "HyperEdge{vertices=" + vertices + ", weight=" + weight + "}";
    }

}

