package org.unifi.mecvirtualresourceallocation;

/**
 * This class represents a vertex in a hypergraph.
 */
public class Vertex {
    private String id;

    /**
     * Constructs a vertex with the specified id.
     *
     * @param id the id of the vertex
     */
    public Vertex(String id) {
        this.id = id;
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
}
