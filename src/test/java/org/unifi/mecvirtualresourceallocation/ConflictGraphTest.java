package org.unifi.mecvirtualresourceallocation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unifi.mecvirtualresourceallocation.graph.ConflictGraph;
import org.unifi.mecvirtualresourceallocation.graph.Edge;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

public class ConflictGraphTest {

  private Vertex vertex1;
  private Vertex vertex2;
  private ConflictGraph conflictGraph;

  @BeforeEach
  public void setUp() {
    vertex1 = new Vertex("1", 1.0);
    vertex2 = new Vertex("2", 2.0);
    conflictGraph = new ConflictGraph();
  }

  @Test
  public void testAddVertex() {
    conflictGraph.addVertex(vertex1);
    conflictGraph.addVertex(vertex2);

    Set<Vertex> vertices = conflictGraph.getVertices();

    assertEquals(2, vertices.size(), "There should be two vertices in the graph");
    assertTrue(vertices.contains(vertex1), "Vertex1 should be in the graph");
    assertTrue(vertices.contains(vertex2), "Vertex2 should be in the graph");
  }

  @Test
  public void testAddEdge() {
    conflictGraph.addVertex(vertex1);
    conflictGraph.addVertex(vertex2);
    conflictGraph.addEdge(vertex1, vertex2);

    Set<Edge> edges = conflictGraph.getEdges();

    assertEquals(1, edges.size(), "There should be one edge in the graph");
    Edge edge = edges.iterator().next();
    assertEquals(vertex1, edge.getVertex1(), "Edge should connect vertex1");
    assertEquals(vertex2, edge.getVertex2(), "Edge should connect vertex2");
  }

  @Test
  public void testGetVertexFromId() {
    conflictGraph.addVertex(vertex1);
    conflictGraph.addVertex(vertex2);

    assertEquals(vertex1, conflictGraph.getVertexFromId("1"), "Should return vertex1 for ID 1");
    assertEquals(vertex2, conflictGraph.getVertexFromId("2"), "Should return vertex2 for ID 2");
    assertNull(conflictGraph.getVertexFromId("3"), "Should return null for non-existing ID");
  }

  @Test
  public void testToString() {
    conflictGraph.addVertex(vertex1);
    conflictGraph.addVertex(vertex2);
    conflictGraph.addEdge(vertex1, vertex2);

    String expected =
        "ConflictGraph {\n"
            + "Vertices:\n"
            + "Vertex{id=1, weight=1.0}\n"
            + "Vertex{id=2, weight=2.0}\n"
            + "Edges:\n"
            + "Edge{vertices=Vertex{id=1, weight=1.0}, Vertex{id=2, weight=2.0}}\n"
            + "}";

    assertEquals(
        expected,
        conflictGraph.toString(),
        "The toString method should return the correct string representation of the graph");
  }
}
