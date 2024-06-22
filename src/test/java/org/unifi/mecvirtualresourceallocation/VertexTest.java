package org.unifi.mecvirtualresourceallocation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VertexTest {

  private Vertex vertex;
  private final String id = "1";
  private final double weight = 1.0;

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
  public void testGetWeight() {
    assertEquals(weight, vertex.getWeight());
  }

  @Test
  public void testGetNegativeWeight() {
    assertEquals(-weight, vertex.getNegativeWeight());
  }

  @Test
  public void testVertexConstructor() {
    Vertex v = new Vertex(id, weight);
    assertEquals(id, v.getId());
    assertEquals(weight, v.getWeight());
  }

  @Test
  public void testToString() {
    String expected = "Vertex{id=1, weight=1.0}";
    assertEquals(expected, vertex.toString());
  }

  @Test
  public void testEquals() {
    Vertex sameVertex = new Vertex(id, weight);
    Vertex differentVertex = new Vertex("2", 2.0);
    assertEquals(vertex, sameVertex);
    assertNotEquals(vertex, differentVertex);
  }

  @Test
  public void testEqualsWithNull() {
    assertNotEquals(null, vertex);
  }

  @Test
  public void testEqualsWithDifferentClass() {
    assertNotEquals("not a vertex", vertex);
  }

  @Test
  public void testHashCode() {
    Vertex sameVertex = new Vertex(id, weight);
    Vertex differentVertex = new Vertex("2", 2.0);
    assertEquals(vertex.hashCode(), sameVertex.hashCode());
    assertNotEquals(vertex.hashCode(), differentVertex.hashCode());
  }
}
