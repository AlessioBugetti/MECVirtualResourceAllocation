package org.unifi.mecvirtualresourceallocation.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Dimension;
import java.awt.Frame;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.fest.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HyperGraphTest {

  private HyperGraph hyperGraph;
  private Set<Vertex> vertices;
  private Set<HyperEdge> edges;
  private FrameFixture frameFixture;

  @BeforeEach
  public void setUp() {
    Vertex v1 = new Vertex("1", 1);
    Vertex v2 = new Vertex("2", 2);
    vertices = new HashSet<>(Arrays.asList(v1, v2));

    HyperEdge e1 = new HyperEdge("1", new HashSet<>(Arrays.asList(v1, v2)));
    edges = new HashSet<>(Collections.singletonList(e1));

    hyperGraph = new HyperGraph(vertices, edges);
  }

  @AfterEach
  public void tearDown() {
    if (frameFixture != null) {
      frameFixture.cleanUp();
    }
    closeAllFrames();
  }

  private void closeAllFrames() {
    for (Frame frame : JFrame.getFrames()) {
      if (frame.isDisplayable()) {
        frame.dispose();
      }
    }
  }

  @Test
  public void testGetEdges() {
    assertEquals(edges, hyperGraph.getHyperEdges());
  }

  @Test
  public void testGetVertices() {
    assertEquals(vertices, hyperGraph.getVertices());
  }

  @Test
  public void testDuplicateEdgeId() {
    Vertex v3 = new Vertex("3", 3);
    HyperEdge e2 = new HyperEdge("1", new HashSet<>(Collections.singletonList(v3)));
    assertThrows(IllegalArgumentException.class, () -> hyperGraph.addHyperEdge(e2));
  }

  @Test
  public void testEmptyHyperEdge() {
    HyperEdge emptyEdge = new HyperEdge("2");
    assertThrows(IllegalArgumentException.class, () -> hyperGraph.addHyperEdge(emptyEdge));
  }

  @Test
  public void testValidPlacementMatrix() {
    int[][] placementMatrix = {
      {1, 0, 0, 1, 0, 1},
      {1, 1, 0, 0, 0, 0},
      {1, 0, 1, 0, 1, 0},
      {0, 1, 0, 0, 0, 1},
      {0, 0, 0, 1, 1, 0},
      {0, 0, 1, 0, 1, 0}
    };

    double[] weights = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};

    HyperGraph hyperGraphFromMatrix = new HyperGraph(placementMatrix, weights);

    assertEquals(6, hyperGraphFromMatrix.getHyperEdges().size());
    assertEquals(6, hyperGraphFromMatrix.getVertices().size());
    for (int i = 0; i < weights.length; i++) {
      for (Vertex vertex : hyperGraphFromMatrix.getVertices()) {
        if (vertex.getId().equals(String.valueOf(i + 1))) {
          assertEquals(BigDecimal.valueOf(weights[i]), vertex.getWeight());
        }
      }
    }
  }

  @Test
  public void testInvalidPlacementMatrix() {
    int[][] placementMatrix = {
      {1, 0, 0, 1, 0, 2},
      {1, 1, 0, 0, 0, 0},
      {1, 0, 1, 0, 1, 0},
      {0, 1, 0, 0, 0, 1},
      {0, 0, 0, 1, 1, 0},
      {0, 0, 1, 0, 1, 0}
    };

    double[] weights = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};

    assertThrows(IllegalArgumentException.class, () -> new HyperGraph(placementMatrix, weights));
  }

  @Test
  void testGetPlacementMatrix() {
    int[][] resultMatrix = hyperGraph.getPlacementMatrix();
    assertEquals(1, resultMatrix[0][0]);
    assertEquals(1, resultMatrix[1][0]);
  }

  @Test
  void testAddVertex() {
    Vertex v3 = new Vertex("3", 3);
    hyperGraph.addHyperEdge(new HyperEdge("2", new HashSet<>(Collections.singletonList(v3))));
    assertEquals(3, hyperGraph.getVertices().size());
    for (Vertex vertex : hyperGraph.getVertices()) {
      if (vertex.getId().equals("3")) {
        assertEquals(v3, vertex);
      }
    }
  }

  @Test
  void testAddHyperEdge() {
    Vertex v3 = new Vertex("3", 3);
    HyperEdge e2 = new HyperEdge("2", new HashSet<>(Collections.singletonList(v3)));
    hyperGraph.addHyperEdge(e2);
    assertEquals(2, hyperGraph.getHyperEdges().size());
    for (HyperEdge hyperEdge : hyperGraph.getHyperEdges()) {
      if (hyperEdge.getId().equals("2")) {
        assertEquals(e2, hyperEdge);
      }
    }
  }

  @Test
  void testAddHyperEdgeWithExistingVertex() {
    HyperEdge e2 =
        new HyperEdge("2", new HashSet<>(Collections.singletonList(vertices.iterator().next())));
    hyperGraph.addHyperEdge(e2);
    assertEquals(2, hyperGraph.getHyperEdges().size());
    for (HyperEdge hyperEdge : hyperGraph.getHyperEdges()) {
      if (hyperEdge.getId().equals("2")) {
        assertEquals(e2, hyperEdge);
      }
    }
  }

  @Test
  void testGetConflictGraph() {
    ConflictGraph conflictGraph = hyperGraph.getConflictGraph();
    assertEquals(1, conflictGraph.getVertices().size());
    assertEquals(0, conflictGraph.getEdges().size());
  }

  @Test
  void testValidate() {
    Vertex v3 = new Vertex("3", 3);
    HyperEdge e2 = new HyperEdge("2", new HashSet<>(Collections.singletonList(v3)));
    Set<HyperEdge> testEdges = new HashSet<>(List.of(e2));
    Vertex v4 = new Vertex("4", 4);
    Set<Vertex> invalidVertices = new HashSet<>(Arrays.asList(v3, v4));
    assertThrows(IllegalArgumentException.class, () -> new HyperGraph(invalidVertices, testEdges));
  }

  @Test
  void testValidateEmptyHyperEdge() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new HyperGraph(
                new HashSet<>(List.of(new Vertex("3", 3))),
                new HashSet<>(List.of(new HyperEdge("2")))));
  }

  @Test
  void testValidateHyperEdgesWithSameVertices() {
    Vertex v3 = new Vertex("3", 3);
    Vertex v4 = new Vertex("4", 4);
    HyperEdge e2 = new HyperEdge("2", new HashSet<>(Arrays.asList(v3, v4)));
    HyperEdge e3 = new HyperEdge("3", new HashSet<>(Arrays.asList(v3, v4)));
    Set<HyperEdge> testEdges = new HashSet<>(Arrays.asList(e2, e3));
    Set<Vertex> vertices = new HashSet<>(Arrays.asList(v3, v4));
    assertThrows(IllegalArgumentException.class, () -> new HyperGraph(vertices, testEdges));
  }

  @Test
  void testValidatePlacementMatrix() {
    int[][] invalidMatrix = {
      {1, 0, 0},
      {1, 2, 0},
      {0, 0, 1}
    };
    double[] weights = {1.0, 2.0, 3.0};
    assertThrows(IllegalArgumentException.class, () -> new HyperGraph(invalidMatrix, weights));
  }

  @Test
  void testToString() {
    String expected =
        """
        HyperGraph{
        Vertices:
        Vertex{id=1, weight=1.0}
        Vertex{id=2, weight=2.0}
        HyperEdges:
        HyperEdge{id=1, vertices=[Vertex{id=1, weight=1.0}, Vertex{id=2, weight=2.0}], weight=3.0}
        }""";
    assertEquals(expected, hyperGraph.toString());
  }

  @Test
  void testAddHyperEdgeWithSameVertices() {
    Vertex v3 = new Vertex("3", 3);
    Vertex v4 = new Vertex("4", 4);
    HyperEdge e2 = new HyperEdge("2", new HashSet<>(Arrays.asList(v3, v4)));
    HyperEdge e3 = new HyperEdge("3", new HashSet<>(Arrays.asList(v3, v4)));
    hyperGraph.addHyperEdge(e2);
    assertThrows(IllegalArgumentException.class, () -> hyperGraph.addHyperEdge(e3));
  }

  @Test
  public void testValidateEmptyHyperEdges() {
    Set<Vertex> testVertices = new HashSet<>(Arrays.asList(new Vertex("1", 1), new Vertex("2", 2)));
    Set<HyperEdge> testEdges = new HashSet<>();
    assertThrows(IllegalArgumentException.class, () -> new HyperGraph(testVertices, testEdges));
  }

  @Test
  public void testInvalidPlacementMatrixRowMismatch() {
    int[][] invalidMatrix = {
      {1, 0},
      {1, 1}
    };
    double[] weights = {1.0, 2.0, 3.0};
    assertThrows(IllegalArgumentException.class, () -> new HyperGraph(invalidMatrix, weights));
  }

  @Test
  public void testGetConflictGraphWithIntersections() {
    Vertex v1 = new Vertex("1", 1);
    Vertex v2 = new Vertex("2", 2);
    Vertex v3 = new Vertex("3", 3);

    HyperEdge e1 = new HyperEdge("1", new HashSet<>(Arrays.asList(v1, v2)));
    HyperEdge e2 = new HyperEdge("2", new HashSet<>(Arrays.asList(v2, v3)));
    HyperEdge e3 = new HyperEdge("3", new HashSet<>(Arrays.asList(v1, v3)));

    Set<Vertex> testVertices = new HashSet<>(Arrays.asList(v1, v2, v3));
    Set<HyperEdge> testEdges = new HashSet<>(Arrays.asList(e1, e2, e3));

    HyperGraph graphWithIntersections = new HyperGraph(testVertices, testEdges);

    ConflictGraph conflictGraph = graphWithIntersections.getConflictGraph();
    assertEquals(3, conflictGraph.getVertices().size());
    assertEquals(3, conflictGraph.getEdges().size());
  }

  @Test
  public void testGetConflictGraphWithoutIntersections() {
    Vertex v3 = new Vertex("3", 1.0);
    Vertex v4 = new Vertex("4", 2.0);

    HyperEdge e1 = new HyperEdge("1", new HashSet<>(List.of(v3)));
    HyperEdge e2 = new HyperEdge("2", new HashSet<>(List.of(v4)));

    Set<Vertex> testVertices = new HashSet<>(Arrays.asList(v3, v4));
    Set<HyperEdge> testEdges = new HashSet<>(Arrays.asList(e1, e2));

    HyperGraph graphWithoutIntersections = new HyperGraph(testVertices, testEdges);

    ConflictGraph conflictGraph = graphWithoutIntersections.getConflictGraph();
    assertEquals(2, conflictGraph.getVertices().size());
    assertEquals(0, conflictGraph.getEdges().size());
  }

  @Test
  public void testPrintPlacementMatrix() {
    java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
    System.setOut(new java.io.PrintStream(outContent));

    hyperGraph.printPlacementMatrix();

    String expectedOutput = "1\n1";
    assertEquals(expectedOutput, outContent.toString().trim());

    System.setOut(System.out);
  }

  @Test
  public void testPrintTwoHyperEdgesPlacementMatrix() {
    java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
    System.setOut(new java.io.PrintStream(outContent));
    Vertex v3 = new Vertex("3", 3.0);
    hyperGraph.addHyperEdge(new HyperEdge("2", new HashSet<>(Collections.singletonList(v3))));
    hyperGraph.printPlacementMatrix();

    String expectedOutput = "1 0\n1 0\n0 1";
    assertEquals(expectedOutput, outContent.toString().trim());

    System.setOut(System.out);
  }

  @Test
  public void testShowGraphIteratorHasNext() {
    Vertex v1 = new Vertex("1", 1);
    Vertex v2 = new Vertex("2", 2);
    Vertex v3 = new Vertex("3", 3);
    Set<Vertex> vertices = new HashSet<>(Arrays.asList(v1, v2, v3));

    HyperEdge e1 = new HyperEdge("1", new HashSet<>(Arrays.asList(v1, v2)));
    HyperEdge e2 = new HyperEdge("2", new HashSet<>(Arrays.asList(v2, v3)));
    Set<HyperEdge> edges = new HashSet<>(Arrays.asList(e1, e2));

    HyperGraph hyperGraph = new HyperGraph(vertices, edges);

    try {
      SwingUtilities.invokeAndWait(hyperGraph::showGraph);

      JFrame hyperGraphFrame = findHyperGraphFrame();

      assertNotNull(hyperGraphFrame, "HyperGraph frame should be present");

      frameFixture = new FrameFixture(hyperGraphFrame);
      frameFixture.requireSize(new Dimension(800, 600));
      frameFixture.requireVisible();
      assertEquals("HyperGraph", hyperGraphFrame.getTitle());
    } catch (InvocationTargetException | InterruptedException e) {
      fail("Test failed due to exception: " + e.getMessage());
    }
  }

  private JFrame findHyperGraphFrame() {
    for (Frame frame : JFrame.getFrames()) {
      if (frame instanceof JFrame && frame.getTitle().equals("HyperGraph")) {
        frame.setVisible(true);
        return (JFrame) frame;
      }
    }
    return null;
  }
}
