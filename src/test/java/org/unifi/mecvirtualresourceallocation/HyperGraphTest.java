package org.unifi.mecvirtualresourceallocation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HyperGraphTest {

    private HyperGraph hyperGraph;
    private List<Vertex> vertices;
    private List<HyperEdge> edges;

    @BeforeEach
    public void setUp() {
        Vertex v1 = new Vertex("1", 1);
        Vertex v2 = new Vertex("2", 2);
        vertices = Arrays.asList(v1, v2);

        HyperEdge e1 = new HyperEdge("1", Arrays.asList(v1, v2));
        edges = Arrays.asList(e1);

        hyperGraph = new HyperGraph(vertices, edges);
    }

    @Test
    public void testGetEdges() {
        assertEquals(edges, hyperGraph.getEdges());
    }

    @Test
    public void testGetVertices() {
        assertEquals(vertices, hyperGraph.getVertices());
    }


    @Test
    public void testHyperGraphConstructor() {
        assertNotNull(new HyperGraph(vertices, edges));
    }

    @Test
    public void testDuplicateEdgeId() {
        Vertex v3 = new Vertex("3", 3);
        HyperEdge e2 = new HyperEdge("1", Arrays.asList(v3));
        assertThrows(IllegalArgumentException.class, () -> {
            hyperGraph.addEdge(e2);
        });
    }
}
