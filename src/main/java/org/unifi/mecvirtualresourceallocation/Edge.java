package org.unifi.mecvirtualresourceallocation;

public class Edge {
    private Vertex vertex1;
    private Vertex vertex2;

    public Edge(Vertex vertex1, Vertex vertex2) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
    }

    public Vertex getVertex1() {
        return vertex1;
    }

    public Vertex getVertex2() {
        return vertex2;
    }

    @Override
    public String toString() {
        return "Edge{vertices=" + vertex1 + ", " + vertex2 + "}";
    }
}