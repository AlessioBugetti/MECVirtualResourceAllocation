package org.unifi.mecvirtualresourceallocation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HyperGraphTest {

  private HyperGraph hyperGraph;
  private List<Vertex> vertices;
  private List<HyperEdge> edges;

  @BeforeEach
  public void setUp() {
    Vertex v1 = new Vertex("1", 1);
    Vertex v2 = new Vertex("2", 2);
    vertices = new ArrayList<>(Arrays.asList(v1, v2));

    HyperEdge e1 = new HyperEdge("1", Arrays.asList(v1, v2));
    edges = new ArrayList<>(Collections.singletonList(e1));

    hyperGraph = new HyperGraph(vertices, edges);
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
    HyperEdge e2 = new HyperEdge("1", Collections.singletonList(v3));
    assertThrows(IllegalArgumentException.class, () -> hyperGraph.addEdge(e2));
  }

  @Test
  public void testEmptyHyperEdge() {
    HyperEdge emptyEdge = new HyperEdge("2");
    assertThrows(IllegalArgumentException.class, () -> hyperGraph.addEdge(emptyEdge));
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
      assertEquals(
          BigDecimal.valueOf(weights[i]), hyperGraphFromMatrix.getVertices().get(i).getWeight());
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
    hyperGraph.addEdge(new HyperEdge("2", Collections.singletonList(v3)));
    assertEquals(3, hyperGraph.getVertices().size());
    assertEquals(v3, hyperGraph.getVertices().get(2));
  }

  @Test
  void testAddEdge() {
    Vertex v3 = new Vertex("3", 3);
    HyperEdge e2 = new HyperEdge("2", Collections.singletonList(v3));
    hyperGraph.addEdge(e2);
    assertEquals(2, hyperGraph.getHyperEdges().size());
    assertEquals(e2, hyperGraph.getHyperEdges().get(1));
  }

  @Test
  void testAddEdgeWithExistingVertex() {
    HyperEdge e2 = new HyperEdge("2", Collections.singletonList(vertices.get(0)));
    hyperGraph.addEdge(e2);
    assertEquals(2, hyperGraph.getHyperEdges().size());
    assertEquals(e2, hyperGraph.getHyperEdges().get(1));
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
    HyperEdge e2 = new HyperEdge("2", Collections.singletonList(v3));
    List<HyperEdge> testEdges = List.of(e2);
    Vertex v4 = new Vertex("4", 4);
    List<Vertex> invalidVertices = Arrays.asList(v3, v4);
    assertThrows(IllegalArgumentException.class, () -> new HyperGraph(invalidVertices, testEdges));
  }

  @Test
  void testValidateEmptyHyperEdge() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new HyperGraph(List.of(new Vertex("3", 3)), List.of(new HyperEdge("2"))));
  }

  @Test
  void testValidateDuplicateHyperEdge() {
    Vertex v3 = new Vertex("3", 3);
    Vertex v4 = new Vertex("4", 4);
    HyperEdge e2 = new HyperEdge("2", Arrays.asList(v3, v4));
    List<HyperEdge> testEdges = Arrays.asList(e2, e2);
    List<Vertex> vertices = Arrays.asList(v3, v4);
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
  void testAddEdgeWithEmptyVertexList() {
    HyperEdge e2 = new HyperEdge("2", new ArrayList<>());
    assertThrows(IllegalArgumentException.class, () -> hyperGraph.addEdge(e2));
  }

  @Test
  public void testValidateEmptyHyperEdges() {
    List<Vertex> testVertices = Arrays.asList(new Vertex("1", 1), new Vertex("2", 2));
    List<HyperEdge> testEdges = new ArrayList<>();
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

    HyperEdge e1 = new HyperEdge("1", Arrays.asList(v1, v2));
    HyperEdge e2 = new HyperEdge("2", Arrays.asList(v2, v3));
    HyperEdge e3 = new HyperEdge("3", Arrays.asList(v1, v3));

    List<Vertex> testVertices = Arrays.asList(v1, v2, v3);
    List<HyperEdge> testEdges = Arrays.asList(e1, e2, e3);

    HyperGraph graphWithIntersections = new HyperGraph(testVertices, testEdges);

    ConflictGraph conflictGraph = graphWithIntersections.getConflictGraph();
    assertEquals(3, conflictGraph.getVertices().size());
    assertEquals(3, conflictGraph.getEdges().size());
  }

  @Test
  public void testGetConflictGraphWithoutIntersections() {
    Vertex v3 = new Vertex("3", 1.0);
    Vertex v4 = new Vertex("4", 2.0);

    HyperEdge e1 = new HyperEdge("1", List.of(v3));
    HyperEdge e2 = new HyperEdge("2", List.of(v4));

    List<Vertex> testVertices = Arrays.asList(v3, v4);
    List<HyperEdge> testEdges = Arrays.asList(e1, e2);

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
    hyperGraph.addEdge(new HyperEdge("2", Collections.singletonList(v3)));
    hyperGraph.printPlacementMatrix();

    String expectedOutput = "1 0\n1 0\n0 1";
    assertEquals(expectedOutput, outContent.toString().trim());

    System.setOut(System.out);
  }
}
