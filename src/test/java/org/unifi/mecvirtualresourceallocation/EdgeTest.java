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
    // Test the getters
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
  public void testToString() {
    // Test the toString method
    String expected = "Edge{vertices=Vertex{id=1}, Vertex{id=2}}";
    assertEquals(
        expected,
        edge.toString(),
        "The toString method should return the correct string representation of the edge");
  }
}
