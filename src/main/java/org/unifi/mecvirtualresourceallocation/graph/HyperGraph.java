package org.unifi.mecvirtualresourceallocation.graph;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import org.unifi.mecvirtualresourceallocation.graph.visualization.HyperGraphPanel;

/**
 * This class represents a hypergraph, which consists of a set of hyperedges and vertices. In the
 * context of Mobile Edge Computing (MEC), a hypergraph is used to model the complex relationships
 * between virtual machine (VM) instances and physical machines (PMs). Each hyperedge connects
 * multiple vertices (VMs).
 */
public final class HyperGraph {
  private Set<Vertex> vertices;
  private Set<HyperEdge> hyperEdges;

  /**
   * Constructs a hypergraph with the specified vertices and hyperedges.
   *
   * @param vertices the vertices of the hypergraph
   * @param hyperEdges the hyperedges of the hypergraph
   */
  public HyperGraph(Set<Vertex> vertices, Set<HyperEdge> hyperEdges) {
    validate(vertices, hyperEdges);
    this.vertices = vertices;
    this.hyperEdges = hyperEdges;
  }

  /**
   * Constructs a hypergraph from a placement matrix and vertex weights. The order of vertices is
   * determined by their corresponding weights in the weights array, which must match the order of
   * rows in the placement matrix.
   *
   * @param placementMatrix the placement matrix where rows represent vertices and columns represent
   *     hyperedges. Each element should be 1 if the corresponding vertex is part of the hyperedge,
   *     otherwise 0
   * @param weights the array of weights for the vertices. The order of weights must match the order
   *     of rows in the placement matrix
   * @throws IllegalArgumentException if the number of weights does not match the number of rows in
   *     the placement matrix
   */
  public HyperGraph(int[][] placementMatrix, double[] weights) {
    validatePlacementMatrix(placementMatrix);

    if (placementMatrix.length != weights.length) {
      throw new IllegalArgumentException(
          "Mismatch between number of weights and placement matrix rows");
    }

    List<Vertex> vertexList = new ArrayList<>();
    for (int i = 0; i < weights.length; i++) {
      vertexList.add(new Vertex(Integer.toString(i + 1), weights[i]));
    }

    Set<Vertex> tmpVertices = new HashSet<>(vertexList);

    Set<HyperEdge> tmpHyperEdges = new HashSet<>();
    for (int j = 0; j < placementMatrix[0].length; j++) {
      Set<Vertex> verticesInHyperEdge = new HashSet<>();
      for (int i = 0; i < placementMatrix.length; i++) {
        if (placementMatrix[i][j] == 1) {
          verticesInHyperEdge.add(vertexList.get(i));
        }
      }
      tmpHyperEdges.add(new HyperEdge(Integer.toString(j + 1), verticesInHyperEdge));
    }

    validate(tmpVertices, tmpHyperEdges);
    this.vertices = tmpVertices;
    this.hyperEdges = tmpHyperEdges;
  }

  /**
   * Validates the placement matrix to ensure it contains only 0s and 1s.
   *
   * @param placementMatrix the placement matrix to validate
   * @throws IllegalArgumentException if any element in the matrix is not 0 or 1
   */
  private void validatePlacementMatrix(int[][] placementMatrix) {
    for (int[] matrix : placementMatrix) {
      for (int i : matrix) {
        if (i != 0 && i != 1) {
          throw new IllegalArgumentException("Placement matrix must contain only 0 or 1 values");
        }
      }
    }
  }

  /**
   * Validates that the union of all hyperedges exactly matches the set of vertices.
   *
   * @param vertices the set of vertices
   * @param hyperEdges the set of hyperedges
   * @throws IllegalArgumentException if the union of all hyperedges does not exactly match the set
   *     of vertices
   */
  private void validate(Set<Vertex> vertices, Set<HyperEdge> hyperEdges) {

    Set<Vertex> allUniqueVertices = new HashSet<>();
    Set<Set<Vertex>> uniqueVertexSets = new HashSet<>();

    for (HyperEdge hyperEdge : hyperEdges) {
      Set<Vertex> verticesInEdge = hyperEdge.getVertices();
      if (verticesInEdge.isEmpty()) {
        throw new IllegalArgumentException("Cannot add a HyperEdge with no vertices.");
      }

      allUniqueVertices.addAll(verticesInEdge);

      if (!uniqueVertexSets.add(verticesInEdge)) {
        throw new IllegalArgumentException(
            "Different HyperEdges with the same set of vertices found: " + verticesInEdge);
      }
    }

    if (!allUniqueVertices.equals(vertices)) {
      throw new IllegalArgumentException(
          "The union of all hyperedges do not exactly match the set of vertices.");
    }
  }

  /**
   * Gets the vertices of the hypergraph.
   *
   * @return the vertices
   */
  public Set<Vertex> getVertices() {
    return vertices;
  }

  /**
   * Gets the hyperedges of the hypergraph.
   *
   * @return the hyperedges
   */
  public Set<HyperEdge> getHyperEdges() {
    return hyperEdges;
  }

  /**
   * Adds a hyperedge to the hypergraph, ensuring no duplicate hyperedge IDs exist. The vertices of
   * the hyperedge are also added to the hypergraph if not already present.
   *
   * @param hyperEdge the hyperedge to be added
   * @throws IllegalArgumentException if the hyperedge has no vertices or if there are duplicate IDs
   */
  public void addHyperEdge(HyperEdge hyperEdge) {
    if (hyperEdge.getVertices().isEmpty()) {
      throw new IllegalArgumentException(
          "Cannot add an HyperEdge with no vertices: " + hyperEdge.getId());
    }

    if (hyperEdges.stream().anyMatch(e -> e.getId().equals(hyperEdge.getId()))) {
      throw new IllegalArgumentException("Duplicate HyperEdge ID found: " + hyperEdge.getId());
    }

    Set<Vertex> verticesInNewEdge = new HashSet<>(hyperEdge.getVertices());
    for (HyperEdge existingEdge : hyperEdges) {
      Set<Vertex> verticesInExistingEdge = new HashSet<>(existingEdge.getVertices());
      if (verticesInNewEdge.equals(verticesInExistingEdge)) {
        throw new IllegalArgumentException(
            "Duplicate HyperEdge with the same set of vertices found: " + hyperEdge.getId());
      }
    }

    vertices.addAll(hyperEdge.getVertices());

    hyperEdges.add(hyperEdge);
  }

  /**
   * Generates a conflict graph based on the hypergraph. The conflict graph represents conflicts
   * between hyperedges where conflicts are defined by the presence of common vertices.
   *
   * @return the generated conflict graph
   */
  public ConflictGraph getConflictGraph() {
    ConflictGraph conflictGraph = new ConflictGraph();

    for (HyperEdge hyperEdge : hyperEdges) {
      conflictGraph.addVertex(new Vertex(hyperEdge.getId(), hyperEdge.getNegativeWeight()));
    }

    List<HyperEdge> hyperEdgeList = new ArrayList<>(hyperEdges);
    for (int i = 0; i < hyperEdgeList.size(); i++) {
      for (int j = i + 1; j < hyperEdgeList.size(); j++) {
        HyperEdge hyperEdge1 = hyperEdgeList.get(i);
        HyperEdge hyperEdge2 = hyperEdgeList.get(j);
        if (hasIntersection(hyperEdge1, hyperEdge2)) {
          Vertex v1 = conflictGraph.getVertexFromId(hyperEdge1.getId());
          Vertex v2 = conflictGraph.getVertexFromId(hyperEdge2.getId());
          conflictGraph.addEdge(v1, v2);
        }
      }
    }
    return conflictGraph;
  }

  /**
   * Checks if two hyperedges have at least one vertex in common.
   *
   * @param hyperEdge1 the first hyperedge
   * @param hyperEdge2 the second hyperedge
   * @return true if there is an intersection; false otherwise
   */
  private boolean hasIntersection(HyperEdge hyperEdge1, HyperEdge hyperEdge2) {
    for (Vertex v : hyperEdge1.getVertices()) {
      if (hyperEdge2.getVertices().contains(v)) {
        return true;
      }
    }
    return false;
  }

  /** Prints the placement matrix to the console. */
  public void printPlacementMatrix() {
    int[][] placementMatrix = getPlacementMatrix();

    for (int[] vector : placementMatrix) {
      for (int i = 0; i < vector.length; i++) {
        System.out.print(vector[i]);
        if (vector.length > 1 && i < vector.length - 1) {
          System.out.print(" ");
        }
      }
      System.out.println();
    }
  }

  /**
   * Generates and returns a placement matrix based on the current hypergraph.
   *
   * @return the placement matrix where rows represent vertices and columns represent hyperedges
   *     Each element is 1 if the corresponding vertex is part of the hyperedge, otherwise 0
   */
  public int[][] getPlacementMatrix() {
    int numVertices = vertices.size();
    int numHyperEdges = hyperEdges.size();

    int[][] placementMatrix = new int[numVertices][numHyperEdges];

    List<Vertex> sortedVertices = new ArrayList<>(vertices);
    sortedVertices.sort(Comparator.comparing(Vertex::getId));

    List<HyperEdge> sortedHyperEdges = new ArrayList<>(hyperEdges);
    sortedHyperEdges.sort(Comparator.comparing(HyperEdge::getId));

    for (int j = 0; j < placementMatrix[0].length; j++) {
      for (int i = 0; i < placementMatrix.length; i++) {
        if (sortedHyperEdges.get(j).getVertices().contains(sortedVertices.get(i))) {
          placementMatrix[i][j] = 1;
        } else {
          placementMatrix[i][j] = 0;
        }
      }
    }

    return placementMatrix;
  }

  /**
   * Returns a string representation of the hypergraph, including its vertices and hyperedges.
   *
   * @return a string representation of the hypergraph
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("HyperGraph{\nVertices:\n");
    for (Vertex vertex : vertices) {
      sb.append(vertex).append("\n");
    }
    sb.append("HyperEdges:\n");
    for (HyperEdge hyperEdge : hyperEdges) {
      sb.append(hyperEdge).append("\n");
    }
    sb.append("}");
    return sb.toString();
  }

  /** Displays the hypergraph using a graphical user interface. */
  public void showGraph() {
    HyperGraphPanel panel = new HyperGraphPanel(this);
    panel.setOpaque(false);
    Dimension graphSize = panel.getGraphSize();
    JFrame frame = new JFrame("HyperGraph");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    int padding = 40;
    frame.setSize(graphSize.width + padding, graphSize.height + padding + 20);
    frame.add(panel);
    frame.setVisible(true);
  }

  /**
   * Saves the current hypergraph visualization as an SVG file. The file is saved as
   * "hypergraph.svg" in the current directory.
   */
  public void saveToSvg() {
    HyperGraphPanel panel = new HyperGraphPanel(this);
    Dimension graphSize = panel.getGraphSize();
    panel.setSize(graphSize);
    panel.setPreferredSize(graphSize);
    panel.setOpaque(false);
    try {
      panel.saveToSvg();
    } catch (Exception e) {
      System.err.println("Error saving the HyperGraph as SVG: " + e.getMessage());
    }
  }
}
