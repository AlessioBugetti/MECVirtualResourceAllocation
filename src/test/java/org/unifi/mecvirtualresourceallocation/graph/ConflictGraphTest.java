package org.unifi.mecvirtualresourceallocation.graph;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Dimension;
import java.awt.Frame;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.fest.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unifi.mecvirtualresourceallocation.graph.visualization.ConflictGraphPanel;

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

  @AfterEach
  public void cleanup() {
    File conflictGraphFile = new File("conflictgraph.svg");
    if (conflictGraphFile.exists()) {
      if (conflictGraphFile.isFile() && !conflictGraphFile.delete()) {
        System.err.println("Failed to delete file: " + conflictGraphFile.getAbsolutePath());
      }
    }
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
  public void testAddDuplicateVertex() {
    conflictGraph.addVertex(vertex1);
    assertThrows(IllegalArgumentException.class, () -> conflictGraph.addVertex(vertex1));
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
  public void testAddEdgeNullVertex1() {
    assertThrows(IllegalArgumentException.class, () -> conflictGraph.addEdge(null, vertex2));
  }

  @Test
  public void testAddEdgeNullVertex2() {
    assertThrows(IllegalArgumentException.class, () -> conflictGraph.addEdge(vertex1, null));
  }

  @Test
  public void testAddDuplicateEdge() {
    conflictGraph.addVertex(vertex1);
    conflictGraph.addVertex(vertex2);
    conflictGraph.addEdge(vertex1, vertex2);

    assertThrows(IllegalArgumentException.class, () -> conflictGraph.addEdge(vertex1, vertex2));
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
  public void testAreVerticesConnected() {
    conflictGraph.addVertex(vertex1);
    conflictGraph.addVertex(vertex2);
    conflictGraph.addEdge(vertex1, vertex2);

    assertTrue(
        conflictGraph.areVerticesConnected(vertex1, vertex2), "Vertices should be connected");
  }

  @Test
  public void testAreVerticesConnectedEmpty() {
    assertFalse(
        conflictGraph.areVerticesConnected(vertex1, vertex2), "Vertices should not be connected");
  }

  @Test
  public void testGetAdjacentVertices() {
    conflictGraph.addVertex(vertex1);
    conflictGraph.addVertex(vertex2);
    conflictGraph.addEdge(vertex1, vertex2);

    Set<Vertex> adjacentVertices = conflictGraph.getAdjacentVertices(vertex1);

    for (Vertex vertex : adjacentVertices) {
      assertEquals(vertex, vertex2, "Vertex2 should be adjacent to Vertex1");
    }
  }

  @Test
  public void testGetAdjacentVerticesEmpty() {
    Set<Vertex> adjacentVertices = conflictGraph.getAdjacentVertices(vertex1);
    assertTrue(adjacentVertices.isEmpty(), "There should be no adjacent vertices");
  }

  @Test
  public void testToString() {
    conflictGraph.addVertex(vertex1);
    conflictGraph.addVertex(vertex2);
    conflictGraph.addEdge(vertex1, vertex2);

    String expected =
        """
            ConflictGraph {
            Vertices:
            Vertex{id=1, weight=1.0}
            Vertex{id=2, weight=2.0}
            Edges:
            Edge{vertices=Vertex{id=1, weight=1.0}, Vertex{id=2, weight=2.0}}
            }""";

    assertEquals(
        expected,
        conflictGraph.toString(),
        "The toString method should return the correct string representation of the graph");
  }

  @Test
  public void testShowGraph() {
    Vertex v1 = new Vertex("1", 1.0);
    Vertex v2 = new Vertex("2", 2.0);
    Vertex v3 = new Vertex("3", 3.0);
    Vertex v4 = new Vertex("4", 4.0);
    Vertex v5 = new Vertex("5", 5.0);
    Vertex v6 = new Vertex("6", 6.0);
    Set<Vertex> vertices = new HashSet<>(Arrays.asList(v1, v2, v3, v4, v5, v6));

    HyperEdge p1 = new HyperEdge("1", new HashSet<>(Arrays.asList(v1, v2, v3)));
    HyperEdge p2 = new HyperEdge("2", new HashSet<>(Arrays.asList(v2, v4)));
    HyperEdge p3 = new HyperEdge("3", new HashSet<>(Arrays.asList(v3, v6)));
    HyperEdge p4 = new HyperEdge("4", new HashSet<>(Arrays.asList(v1, v5)));
    HyperEdge p5 = new HyperEdge("5", new HashSet<>(Arrays.asList(v3, v5, v6)));
    HyperEdge p6 = new HyperEdge("6", new HashSet<>(Arrays.asList(v1, v4)));
    Set<HyperEdge> edges = new HashSet<>(Arrays.asList(p1, p2, p3, p4, p5, p6));

    ConflictGraph conflictGraph = new HyperGraph(vertices, edges).getConflictGraph();

    FrameFixture frameFixture = null;
    try {
      SwingUtilities.invokeAndWait(conflictGraph::showGraph);

      JFrame conflictGraphFrame = null;
      for (Frame frame : JFrame.getFrames()) {
        if (frame instanceof JFrame && frame.getTitle().equals("ConflictGraph")) {
          conflictGraphFrame = (JFrame) frame;
          break;
        }
      }

      assertNotNull(conflictGraphFrame, "ConflictGraph frame should be present");

      frameFixture = new FrameFixture(conflictGraphFrame);
      Dimension requiredDimension = new ConflictGraphPanel(conflictGraph).getGraphSize();
      int padding = 40;
      requiredDimension.setSize(
          requiredDimension.getWidth() + padding, requiredDimension.getHeight() + padding + 20);
      frameFixture.requireSize(requiredDimension);
      frameFixture.requireVisible();
      assertEquals("ConflictGraph", conflictGraphFrame.getTitle());
    } catch (InvocationTargetException | InterruptedException e) {
      fail("Test failed due to exception: " + e.getMessage());
    } finally {
      if (frameFixture != null) {
        frameFixture.cleanUp();
      }
    }
  }

  @Test
  public void testSaveToSvg() {
    conflictGraph.addVertex(vertex1);
    conflictGraph.addVertex(vertex2);
    conflictGraph.addEdge(vertex1, vertex2);
    conflictGraph.saveToSvg("");
    File svgFile = new File("conflictgraph.svg");
    assertTrue(svgFile.exists(), "SVG file should be created");
    assertTrue(svgFile.length() > 0, "SVG file should not be empty");
  }
}
