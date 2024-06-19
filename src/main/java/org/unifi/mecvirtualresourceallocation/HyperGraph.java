package org.unifi.mecvirtualresourceallocation;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a hypergraph, which consists of a set of hyperedges and vertices.
 */
public class HyperGraph {
    private List<Vertex> vertices;
    private List<HyperEdge> edges;

    /**
     * Constructs a hypergraph with the specified edges and vertices.
     *
     * @param edges the hyperedges of the hypergraph
     * @param vertices the vertices of the hypergraph
     */
    public HyperGraph(List<Vertex> vertices, List<HyperEdge> edges) {
        this.vertices = vertices;
        this.edges = new ArrayList<>();
        for (HyperEdge edge : edges) {
            addEdge(edge);
        }
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
     * Gets the hyperedges of the hypergraph.
     *
     * @return the hyperedges
     */
    public List<HyperEdge> getEdges() {
        return edges;
    }

    public void addEdge(HyperEdge edge) {
        if (edge.getVertices().isEmpty()) {
            throw new IllegalArgumentException("Cannot add an HyperEdge with no vertices: " + edge.getId());
        }

        if (edges.stream().anyMatch(e -> e.getId().equals(edge.getId()))) {
            throw new IllegalArgumentException("Duplicate HyperEdge ID found: " + edge.getId());
        }

        edges.add(edge);
    }

    public ConflictGraph generateConflictGraph(){
        ConflictGraph conflictGraph = new ConflictGraph();

        for (HyperEdge edge : edges) {
            conflictGraph.addVertex(new Vertex(edge.getId(), edge.getWeight()));
        }

        for (int i = 0; i < edges.size(); i++) {
            for (int j = i + 1; j < edges.size(); j++) {
                HyperEdge edge1 = edges.get(i);
                HyperEdge edge2 = edges.get(j);
                if (hasIntersection(edge1, edge2)) {
                    Vertex v1 = conflictGraph.getVertexFromId(edge1.getId());
                    Vertex v2 = conflictGraph.getVertexFromId(edge2.getId());
                    conflictGraph.addEdge(v1, v2);
                }
            }
        }
        return conflictGraph;
    }

    private boolean hasIntersection(HyperEdge edge1, HyperEdge edge2) {
        for (Vertex v : edge1.getVertices()) {
            if (edge2.getVertices().contains(v)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HyperGraph{\nVertices:\n");
        for (Vertex vertex : vertices) {
            sb.append(vertex).append("\n");
        }
        sb.append("HyperEdges:\n");
        for (HyperEdge edge : edges) {
            sb.append(edge).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
