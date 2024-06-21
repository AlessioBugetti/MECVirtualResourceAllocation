package org.unifi.mecvirtualresourceallocation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    vertices = Arrays.asList(v1, v2);

    HyperEdge e1 = new HyperEdge("1", Arrays.asList(v1, v2));
    edges = Collections.singletonList(e1);

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

    List<Vertex> verticesForMatrix = new ArrayList<>();
    verticesForMatrix.add(new Vertex("1", 1));
    verticesForMatrix.add(new Vertex("2", 2));
    verticesForMatrix.add(new Vertex("3", 3));
    verticesForMatrix.add(new Vertex("4", 4));
    verticesForMatrix.add(new Vertex("5", 5));
    verticesForMatrix.add(new Vertex("6", 6));

    HyperGraph hyperGraphFromMatrix = new HyperGraph(placementMatrix, verticesForMatrix);

    assertEquals(6, hyperGraphFromMatrix.getHyperEdges().size());
    assertEquals(verticesForMatrix, hyperGraphFromMatrix.getVertices());
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

    List<Vertex> verticesForMatrix = new ArrayList<>();
    verticesForMatrix.add(new Vertex("1", 1));
    verticesForMatrix.add(new Vertex("2", 2));
    verticesForMatrix.add(new Vertex("3", 3));
    verticesForMatrix.add(new Vertex("4", 4));
    verticesForMatrix.add(new Vertex("5", 5));
    verticesForMatrix.add(new Vertex("6", 6));

    assertThrows(
        IllegalArgumentException.class, () -> new HyperGraph(placementMatrix, verticesForMatrix));
  }

  @Test
  void testGetPlacementMatrix() {
    int[][] resultMatrix = hyperGraph.getPlacementMatrix();
    assertEquals(1, resultMatrix[0][0]);
    assertEquals(1, resultMatrix[1][0]);
  }
}
