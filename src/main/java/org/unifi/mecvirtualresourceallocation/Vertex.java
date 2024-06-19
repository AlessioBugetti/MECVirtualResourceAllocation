package org.unifi.mecvirtualresourceallocation;

/**
 * This class represents a vertex in a hypergraph.
 */
public class Vertex {
    private String id;
    private double weight;

    /**
     * Constructs a vertex with the specified id.
     *
     * @param id the id of the vertex
     */
    public Vertex(String id, double weight) {
        this.id = id;
        this.weight = weight;
    }

    /**
     * Gets the id of the vertex.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of the vertex.
     *
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Vertex{id=" + id + ", weight=" + weight + "}";
    }
}
