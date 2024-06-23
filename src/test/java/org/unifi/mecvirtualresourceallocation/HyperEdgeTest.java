package org.unifi.mecvirtualresourceallocation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unifi.mecvirtualresourceallocation.graph.HyperEdge;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

public class HyperEdgeTest {

  private HyperEdge hyperEdge;
  private List<Vertex> vertices;
  private Vertex v1;
  private Vertex v2;

  @BeforeEach
  public void setUp() {
    v1 = new Vertex("1", 1.0);
    v2 = new Vertex("2", 2.0);
    vertices = new ArrayList<>(Arrays.asList(v1, v2));

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

    List<Vertex> updatedVertices = hyperEdge.getVertices();
    assertEquals(3, updatedVertices.size());
    assertTrue(updatedVertices.contains(v3));
    assertEquals(BigDecimal.valueOf(6.0), hyperEdge.getWeight());
  }

  @Test
  public void testUninitializedVertex() {
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
  public void testConstructorWithDuplicateVertices() {
    Vertex v3 = new Vertex("3", 3.0);
    List<Vertex> verticesWithDuplicates = new ArrayList<>(Arrays.asList(v1, v2, v3, v3));
    assertThrows(IllegalArgumentException.class, () -> new HyperEdge("2", verticesWithDuplicates));
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
