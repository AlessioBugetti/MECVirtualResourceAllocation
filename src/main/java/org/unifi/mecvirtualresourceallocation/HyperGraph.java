package org.unifi.mecvirtualresourceallocation;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * This class represents a hypergraph, which consists of a set of hyperedges and
 * vertices. In the context of Mobile Edge Computing (MEC), a hypergraph is used
 * to model the complex relationships between virtual machine (VM) instances and
 * physical machines (PMs). Each hyperedge connects multiple vertices (VMs).
 */
public class HyperGraph {
  private List<Vertex> vertices;
  private List<HyperEdge> edges;

  /**
   * Constructs a hypergraph with the specified vertices and hyperedges.
   *
   * @param vertices the vertices of the hypergraph.
   * @param edges    the hyperedges of the hypergraph.
   */
  public HyperGraph(List<Vertex> vertices, List<HyperEdge> edges) {
    this.vertices = vertices;
    this.edges = new ArrayList<>();
    for (HyperEdge edge : edges) {
      addEdge(edge);
    }
  }

  /**
   * Constructs a hypergraph from a placement matrix.
   * The order of vertices in the provided list must correspond to the order of
   * vertices in the rows of the placement matrix.
   *
   * @param placementMatrix the placement matrix where rows represent vertices
   *     and columns represent hyperedges.
   *                        Each element should be 1 if the corresponding vertex
   * is part of the hyperedge, otherwise 0.
   * @param vertices        the list of vertices. The order of vertices must
   *     match the order of rows in the placement matrix.
   * @throws IllegalArgumentException if the number of vertices in the list does
   *     not match the number of rows in the placement matrix.
   */
  public HyperGraph(int[][] placementMatrix, List<Vertex> vertices) {
    this.vertices = vertices;
    this.edges = new ArrayList<>();

    validatePlacementMatrix(placementMatrix);

    if (placementMatrix.length != vertices.size()) {
      throw new IllegalArgumentException(
          "Mismatch between number of vertices and placement matrix rows");
    }

    for (int j = 0; j < placementMatrix[0].length; j++) {
      List<Vertex> verticesInHyperEdge = new ArrayList<>();
      for (int i = 0; i < placementMatrix.length; i++) {
        if (placementMatrix[i][j] == 1) {
          verticesInHyperEdge.add(vertices.get(i));
        }
      }
      HyperEdge edge =
          new HyperEdge(Integer.toString(j + 1), verticesInHyperEdge);
      this.addEdge(edge);
    }
  }

  /**
   * Validates the placement matrix to ensure it contains only 0s and 1s.
   *
   * @param placementMatrix the placement matrix to validate.
   * @throws IllegalArgumentException if any element in the matrix is not 0
   *     or 1.
   */
  private void validatePlacementMatrix(int[][] placementMatrix) {
    for (int[] matrix : placementMatrix) {
      for (int i : matrix) {
        if (i != 0 && i != 1) {
          throw new IllegalArgumentException(
              "Placement matrix must contain only 0 or 1 values");
        }
      }
    }
  }

  /**
   * Gets the vertices of the hypergraph.
   *
   * @return the vertices.
   */
  public List<Vertex> getVertices() { return vertices; }

  /**
   * Gets the hyperedges of the hypergraph.
   *
   * @return the hyperedges.
   */
  public List<HyperEdge> getEdges() { return edges; }

  /**
   * Adds a vertex to the hypergraph, ensuring no duplicate vertex IDs exist.
   *
   * @param vertex the vertex to be added.
   */
  private void addVertex(Vertex vertex) {
    for (Vertex v : vertices) {
      if (v.getId().equals(vertex.getId())) {
        throw new IllegalArgumentException("Duplicate Vertex ID found: " +
                                           vertex.getId());
      }
    }
    vertices.add(vertex);
  }

  /**
   * Adds a hyperedge to the hypergraph, ensuring no duplicate hyperedge IDs
   * exist. The vertices of the hyperedge are also added to the hypergraph if
   * not already present.
   *
   * @param edge the hyperedge to be added.
   * @throws IllegalArgumentException if the hyperedge has no vertices or if
   *     there are duplicate IDs.
   */
  public void addEdge(HyperEdge edge) {
    for (Vertex vertex : vertices) {
      if (!vertices.contains(vertex)) {
        addVertex(vertex);
      }
    }

    if (edge.getVertices().isEmpty()) {
      throw new IllegalArgumentException(
          "Cannot add an HyperEdge with no vertices: " + edge.getId());
    }

    if (edges.stream().anyMatch(e -> e.getId().equals(edge.getId()))) {
      throw new IllegalArgumentException("Duplicate HyperEdge ID found: " +
                                         edge.getId());
    }

    edges.add(edge);
  }

  /**
   * Generates a conflict graph based on the hypergraph. The conflict graph
   * represents conflicts between hyperedges where conflicts are defined by the
   * presence of common vertices.
   *
   * @return the generated conflict graph.
   */
  public ConflictGraph getConflictGraph() {
    ConflictGraph conflictGraph = new ConflictGraph();

    for (HyperEdge edge : edges) {
      conflictGraph.addVertex(new Vertex(edge.getId(), edge.getWeight()));
    }

    for (int i = 0; i < edges.size(); i++) {
      for (int j = i + 1; j < edges.size(); j++) {
        HyperEdge edge1 = edges.get(i);
        HyperEdge edge2 = edges.get(j);
        if (hasIntersection(edge1, edge2)) {
          Vertex v1 = conflictGraph.getVertexFromId(edge1.getId());
          Vertex v2 = conflictGraph.getVertexFromId(edge2.getId());
          conflictGraph.addEdge(v1, v2);
        }
      }
    }
    return conflictGraph;
  }

  /**
   * Checks if two hyperedges have at least one vertex in common.
   *
   * @param edge1 the first hyperedge.
   * @param edge2 the second hyperedge.
   * @return true if there is an intersection; false otherwise.
   */
  private boolean hasIntersection(HyperEdge edge1, HyperEdge edge2) {
    for (Vertex v : edge1.getVertices()) {
      if (edge2.getVertices().contains(v)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Generates and returns a placement matrix based on the current hypergraph.
   *
   * @return the placement matrix where rows represent vertices and columns
   *     represent hyperedges.
   * Each element is 1 if the corresponding vertex is part of the hyperedge,
   * otherwise 0.
   */
  public int[][] getPlacementMatrix() {
    int numVertices = vertices.size();
    int numHyperEdges = edges.size();

    int[][] placementMatrix = new int[numVertices][numHyperEdges];

    for (int edgeIndex = 0; edgeIndex < numHyperEdges; edgeIndex++) {
      HyperEdge edge = edges.get(edgeIndex);
      List<Vertex> verticesInHyperEdge = edge.getVertices();

      for (int vertexIndex = 0; vertexIndex < numVertices; vertexIndex++) {
        Vertex vertex = vertices.get(vertexIndex);

        if (verticesInHyperEdge.contains(vertex)) {
          placementMatrix[vertexIndex][edgeIndex] = 1;
        } else {
          placementMatrix[vertexIndex][edgeIndex] = 0;
        }
      }
    }

    return placementMatrix;
  }

  /**
   * Prints the placement matrix to the console.
   */
  public void printPlacementMatrix() {
    int[][] placementMatrix = getPlacementMatrix();

    for (int[] vector : placementMatrix) {
      for (int element : vector) {
        System.out.print(element + " ");
      }
      System.out.println();
    }
  }

  /**
   * Returns a string representation of the hypergraph, including its vertices
   * and hyperedges.
   *
   * @return a string representation of the hypergraph.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("HyperGraph{\nVertices:\n");
    for (Vertex vertex : vertices) {
      sb.append(vertex).append("\n");
    }
    sb.append("HyperEdges:\n");
    for (HyperEdge edge : edges) {
      sb.append(edge).append("\n");
    }
    sb.append("}");
    return sb.toString();
  }

  /**
   * Displays the hypergraph using a graphical user interface.
   */
  public void showGraph() {
    JFrame frame = new JFrame("HyperGraph");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    frame.add(new HyperGraphPanel(this));
    frame.setVisible(true);
  }
}
