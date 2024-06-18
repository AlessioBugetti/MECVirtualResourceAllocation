package org.unifi.mecvirtualresourceallocation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VertexTest {

    private Vertex vertex;
    private String id = "1";

    @BeforeEach
    public void setUp() {
        vertex = new Vertex(id);
    }

    @Test
    public void testGetId() {
        assertEquals(id, vertex.getId());
    }

    @Test
    public void testSetId() {
        String newId = "2";
        vertex.setId(newId);
        assertEquals(newId, vertex.getId());
    }

    @Test
    public void testVertexConstructor() {
        assertEquals(id, new Vertex(id).getId());
    }
}
