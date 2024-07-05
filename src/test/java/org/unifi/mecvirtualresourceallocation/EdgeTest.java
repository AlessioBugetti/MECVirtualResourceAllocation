package org.unifi.mecvirtualresourceallocation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unifi.mecvirtualresourceallocation.graph.Edge;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

public class EdgeTest {

  private Vertex vertex1;
  private Vertex vertex2;
  private Edge edge;

  @BeforeEach
  public void setUp() {
    vertex1 = new Vertex("1", 1.0);
    vertex2 = new Vertex("2", 2.0);
    edge = new Edge(vertex1, vertex2);
  }

  @Test
  public void testConstructorAndGetters() {
    assertEquals(
        vertex1,
        edge.getVertex1(),
        "Vertex1 should be the same as the one provided in the constructor");
    assertEquals(
        vertex2,
        edge.getVertex2(),
        "Vertex2 should be the same as the one provided in the constructor");
  }

  @Test
  public void testEquals() {
    assertTrue(edge.equals(edge));
  }

  @Test
  public void testEqualsWithNull() {
    assertFalse(edge.equals(null));
  }

  @Test
  public void testEqualsWithString() {
    String differentClassObject = "test";
    assertFalse(edge.equals(differentClassObject));
  }

  @Test
  public void testEqualsWithObject() {
    Object differentClassObject = new Object();
    assertFalse(edge.equals(differentClassObject));
  }

  @Test
  public void testEqualsWithDifferentPosition() {
    Edge edge2 = new Edge(vertex2, vertex1);
    assertTrue(edge.equals(edge2));
  }

  @Test
  public void testEqualsWithSamePosition() {
    Edge edge2 = new Edge(vertex1, vertex2);
    assertTrue(edge.equals(edge2));
  }

  @Test
  public void testEqualsWithDifferentFirstVertex() {
    Vertex vertex3 = new Vertex("3", 3.0);
    Edge edge2 = new Edge(vertex1, vertex3);
    Edge edge3 = new Edge(vertex2, vertex3);
    assertFalse(edge2.equals(edge3));
  }

  @Test
  public void testEqualsWithDifferentSecondVertex() {
    Vertex vertex3 = new Vertex("3", 3.0);
    Edge edge2 = new Edge(vertex1, vertex3);
    assertFalse(edge.equals(edge2));
  }

  @Test
  public void testEqualsWithDifferentVertices() {
    Vertex vertex3 = new Vertex("3", 3.0);
    Vertex vertex4 = new Vertex("4", 4.0);
    Edge edge2 = new Edge(vertex3, vertex4);
    assertFalse(edge.equals(edge2));
  }

  @Test
  public void testHashCode() {
    Vertex sameVertex = new Vertex(vertex1.getId(), vertex1.getWeight());
    Vertex differentVertex = new Vertex("3", 3.0);
    assertEquals(vertex1.hashCode(), sameVertex.hashCode());
    assertNotEquals(vertex1.hashCode(), differentVertex.hashCode());
  }

  @Test
  public void testToString() {
    String expected = "Edge{vertices=Vertex{id=1, weight=1.0}, Vertex{id=2, weight=2.0}}";
    assertEquals(
        expected,
        edge.toString(),
        "The toString method should return the correct string representation of the edge");
  }
}
