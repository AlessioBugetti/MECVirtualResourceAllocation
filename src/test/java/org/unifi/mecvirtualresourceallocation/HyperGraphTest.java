package org.unifi.mecvirtualresourceallocation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HyperGraphTest {

    private HyperGraph hyperGraph;
    private List<HyperEdge> edges;
    private List<Vertex> vertices;

    @BeforeEach
    public void setUp() {
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");
        vertices = Arrays.asList(v1, v2);

        HyperEdge e1 = new HyperEdge(Arrays.asList(v1, v2), 10.0);
        edges = Arrays.asList(e1);

        hyperGraph = new HyperGraph(edges, vertices);
    }

    @Test
    public void testGetEdges() {
        assertEquals(edges, hyperGraph.getEdges());
    }

    @Test
    public void testSetEdges() {
        Vertex v3 = new Vertex("3");
        HyperEdge e2 = new HyperEdge(Arrays.asList(v3), 5.0);
        List<HyperEdge> newEdges = Arrays.asList(e2);

        hyperGraph.setEdges(newEdges);
        assertEquals(newEdges, hyperGraph.getEdges());
    }

    @Test
    public void testGetVertices() {
        assertEquals(vertices, hyperGraph.getVertices());
    }

    @Test
    public void testSetVertices() {
        Vertex v3 = new Vertex("3");
        List<Vertex> newVertices = Arrays.asList(v3);

        hyperGraph.setVertices(newVertices);
        assertEquals(newVertices, hyperGraph.getVertices());
    }

    @Test
    public void testHyperGraphConstructor() {
        assertNotNull(new HyperGraph(edges, vertices));
    }
}
