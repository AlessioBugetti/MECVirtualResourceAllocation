package org.unifi.mecvirtualresourceallocation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

public class VertexTest {

  private Vertex vertex;
  private final String id = "1";
  private final BigDecimal weight = BigDecimal.valueOf(1.0);

  @BeforeEach
  public void setUp() {
    vertex = new Vertex(id, weight);
  }

  @Test
  void testSetUpNegativeWeight() {
    double weight = -1.0;
    Vertex vertex2 = new Vertex(id, weight);
    assertEquals(BigDecimal.valueOf(weight), vertex2.getNegativeWeight());
  }

  @Test
  void testSetNegativeId() {
    assertThrows(IllegalArgumentException.class, () -> new Vertex("-1", 1.0));
    assertThrows(IllegalArgumentException.class, () -> new Vertex("0", 1.0));
    assertThrows(IllegalArgumentException.class, () -> new Vertex("-1", BigDecimal.valueOf(1.0)));
    assertThrows(IllegalArgumentException.class, () -> new Vertex("0", BigDecimal.valueOf(1.0)));
  }

  @Test
  void testSetWrongId() {
    assertThrows(IllegalArgumentException.class, () -> new Vertex("test", 1.0));
    assertThrows(IllegalArgumentException.class, () -> new Vertex("test", BigDecimal.valueOf(1.0)));
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
    assertEquals(weight.negate(), vertex.getNegativeWeight());
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
  public void testEqualsWithNull() {
    Vertex vertex1 = new Vertex("1", 1.0);
    assertFalse(vertex1.equals(null));
  }

  @Test
  public void testEqualsWithString() {
    Vertex vertex1 = new Vertex("1", 1.0);
    String differentClassObject = "test";
    assertFalse(vertex1.equals(differentClassObject));
  }

  @Test
  public void testEqualsWithObject() {
    Vertex vertex1 = new Vertex("1", 1.0);
    Object differentClassObject = new Object();
    assertFalse(vertex1.equals(differentClassObject));
  }

  @Test
  public void testEqualsWithDifferentId() {
    Vertex differentVertex = new Vertex("2", 2.0);
    assertFalse(vertex.equals(differentVertex));
  }

  @Test
  public void testEqualsWithSameId() {
    Vertex sameVertex = new Vertex(id, weight);
    assertTrue(vertex.equals(sameVertex));
  }

  @Test
  public void testHashCode() {
    Vertex sameVertex = new Vertex(id, weight);
    Vertex differentVertex = new Vertex("2", 2.0);
    assertEquals(vertex.hashCode(), sameVertex.hashCode());
    assertNotEquals(vertex.hashCode(), differentVertex.hashCode());
  }
}
