package org.unifi.mecvirtualresourceallocation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HyperEdgeTest {

    private HyperEdge hyperEdge;
    private List<Vertex> vertices;

    @BeforeEach
    public void setUp() {
        Vertex v1 = new Vertex("1", 1);
        Vertex v2 = new Vertex("2", 2);
        vertices = Arrays.asList(v1, v2);

        hyperEdge = new HyperEdge("1", vertices);
    }

    @Test
    public void testGetVertices() {
        assertEquals(vertices, hyperEdge.getVertices());
    }

    @Test
    public void testSetVertices() {
        Vertex v3 = new Vertex("3", 3);
        List<Vertex> newVertices = Arrays.asList(v3);

        hyperEdge.setVertices(newVertices);
        assertEquals(newVertices, hyperEdge.getVertices());
    }

    @Test
    public void testGetWeight() {
        assertEquals(3, hyperEdge.getWeight(), 0);
    }


    @Test
    public void testHyperEdgeConstructor() {
        assertNotNull(new HyperEdge("2", vertices));
    }
}
