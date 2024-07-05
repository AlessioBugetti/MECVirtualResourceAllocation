package org.unifi.mecvirtualresourceallocation.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HyperEdgeTest {

  private HyperEdge hyperEdge;
  private Set<Vertex> vertices;
  private Vertex v2;

  @BeforeEach
  public void setUp() {
    Vertex v1 = new Vertex("1", 1.0);
    v2 = new Vertex("2", 2.0);
    vertices = new HashSet<>(Arrays.asList(v1, v2));

    hyperEdge = new HyperEdge("1", vertices);
  }

  @Test
  public void testGetVertices() {
    assertEquals(vertices, hyperEdge.getVertices());
  }

  @Test
  public void testAddVertex() {
    Vertex v3 = new Vertex("3", 3.0);
    hyperEdge.addVertex(v3);

    Set<Vertex> updatedVertices = hyperEdge.getVertices();
    assertEquals(3, updatedVertices.size());
    assertTrue(updatedVertices.contains(v3));
    assertEquals(BigDecimal.valueOf(6.0), hyperEdge.getWeight());
  }

  @Test
  public void testUninitializedVertex() {
    assertThrows(IllegalArgumentException.class, () -> new HyperEdge("1", null));
  }

  @Test
  public void testAddUninitializedVertex() {
    assertThrows(IllegalArgumentException.class, () -> hyperEdge.addVertex(null));
  }

  @Test
  public void testAddDuplicateVertex() {
    assertThrows(IllegalArgumentException.class, () -> hyperEdge.addVertex(v2));
  }

  @Test
  public void testGetWeight() {
    assertEquals(BigDecimal.valueOf(3.0), hyperEdge.getWeight());
  }

  @Test
  public void testGetNegativeWeight() {
    assertEquals(BigDecimal.valueOf(-3.0), hyperEdge.getNegativeWeight());
  }

  @Test
  public void testToString() {
    String expected = "HyperEdge{id=1, vertices=" + vertices.toString() + ", weight=3.0}";
    assertEquals(expected, hyperEdge.toString());
  }

  @Test
  public void testConstructorWithNoVertices() {
    HyperEdge emptyEdge = new HyperEdge("2");
    assertEquals("2", emptyEdge.getId());
    assertEquals(0, emptyEdge.getVertices().size());
    assertEquals(BigDecimal.ZERO, emptyEdge.getWeight());
  }
}
