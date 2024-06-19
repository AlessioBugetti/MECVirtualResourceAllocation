package org.unifi.mecvirtualresourceallocation;

public class Edge {
    private Vertex v1;
    private Vertex v2;

    public Edge(Vertex v1, Vertex v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public Vertex getV1() {
        return v1;
    }

    public Vertex getV2() {
        return v2;
    }

    @Override
    public String toString() {
        return "Edge{vertices=" + v1 + ", " + v2 + "}";
    }
}