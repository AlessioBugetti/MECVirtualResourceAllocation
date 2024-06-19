package org.unifi.mecvirtualresourceallocation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VertexTest {

  private Vertex vertex;
  private final String id = "1";
  private final int weight = 1;

  @BeforeEach
  public void setUp() {
    vertex = new Vertex(id, weight);
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
    assertEquals(id, new Vertex(id, weight).getId());
  }
}
