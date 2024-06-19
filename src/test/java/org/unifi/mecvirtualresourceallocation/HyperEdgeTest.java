package org.unifi.mecvirtualresourceallocation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    //TODO: Da risolvere problema testAddVertex()
    /*
    @Test
    public void testAddVertex() {
        double weightBefore = hyperEdge.getWeight();
        Vertex v3 = new Vertex("3", 3);
        hyperEdge.addVertex(v3);

        List<Vertex> updatedVertices = hyperEdge.getVertices();
        assertEquals(3, updatedVertices.size());
        assertTrue(updatedVertices.contains(v3));

        double expectedWeightAfter = weightBefore + v3.getWeight();
        assertEquals(expectedWeightAfter, hyperEdge.getWeight(), 0);
    }
    */

    @Test
    public void testGetWeight() {
        assertEquals(3, hyperEdge.getWeight(), 0);
    }

}
