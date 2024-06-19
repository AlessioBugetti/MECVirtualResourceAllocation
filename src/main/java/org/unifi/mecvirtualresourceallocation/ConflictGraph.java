package org.unifi.mecvirtualresourceallocation;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class ConflictGraph {
    private Set<Vertex> vertices;
    private Set<Edge> edges;

    public ConflictGraph() {
        this.vertices = new HashSet<>();
        this.edges = new HashSet<>();
    }

    public Set<Vertex> getVertices() {
        return vertices;
    }

    public Vertex getVertexFromId(String id) {
        for (Vertex vertex : vertices) {
            if (vertex.getId().equals(id)) {
                return vertex;
            }
        }
        return null;
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    public void addEdge(Vertex v1, Vertex v2) {
        edges.add(new Edge(v1, v2));
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ConflictGraph {\n");

        sb.append("Vertices:\n");
        for (Vertex vertex : vertices) {
            sb.append(vertex).append("\n");
        }

        sb.append("Edges:\n");
        for (Edge edge : edges) {
            sb.append(edge).append("\n");
        }

        sb.append("}");

        return sb.toString();
    }

    public void showGraph() {
        JFrame frame = new JFrame("Conflict Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new GraphPanel(this));
        frame.setVisible(true);
    }

}
