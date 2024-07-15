package org.unifi.mecvirtualresourceallocation.graph;

import java.awt.Dimension;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import org.unifi.mecvirtualresourceallocation.graph.visualization.ConflictGraphPanel;

/**
 * This class represents a conflict graph derived from a hypergraph. The conflict graph is used to
 * model conflicts between hyperedges, where conflicts are defined by the presence of common
 * vertices.
 */
public class ConflictGraph {

  private Map<String, Vertex> vertices;
  private Map<String, Set<Vertex>> adjacencyList;
  private Set<Edge> edges;

  /** Constructs an empty conflict graph. */
  public ConflictGraph() {
    this.vertices = new HashMap<>();
    this.adjacencyList = new HashMap<>();
    this.edges = new HashSet<>();
  }

  /**
   * Gets the vertices of the conflict graph.
   *
   * @return the vertices
   */
  public Set<Vertex> getVertices() {
    return new HashSet<>(vertices.values());
  }

  /**
   * Retrieves a vertex from the conflict graph by its ID.
   *
   * @param id the ID of the vertex to retrieve
   * @return the vertex with the specified ID, or null if not found
   */
  public Vertex getVertexFromId(String id) {
    return vertices.get(id);
  }

  /**
   * Adds a vertex to the conflict graph.
   *
   * @param vertex the vertex to be added
   */
  public void addVertex(Vertex vertex) {
    if (vertices.containsKey(vertex.getId())) {
      throw new IllegalArgumentException("Vertex with ID " + vertex.getId() + " already exists.");
    }
    vertices.put(vertex.getId(), vertex);
    adjacencyList.putIfAbsent(vertex.getId(), new HashSet<>());
  }

  /**
   * Adds an edge between two vertices in the conflict graph.
   *
   * @param vertex1 the first vertex
   * @param vertex2 the second vertex
   */
  public void addEdge(Vertex vertex1, Vertex vertex2) {
    if (vertex1 == null || vertex2 == null) {
      throw new IllegalArgumentException("Vertices cannot be null.");
    }

    Edge edge = new Edge(vertex1, vertex2);
    if (edges.contains(edge)) {
      throw new IllegalArgumentException(
          "Edge between " + vertex1.getId() + " and " + vertex2.getId() + " already exists.");
    }

    adjacencyList.get(vertex1.getId()).add(vertex2);
    adjacencyList.get(vertex2.getId()).add(vertex1);
    edges.add(new Edge(vertex1, vertex2));
  }

  /**
   * Gets the edges of the conflict graph.
   *
   * @return the edges
   */
  public Set<Edge> getEdges() {
    return edges;
  }

  /**
   * Checks if two vertices are connected.
   *
   * @param vertex1 the first vertex
   * @param vertex2 the second vertex
   * @return true if the vertices are connected, false otherwise
   */
  public boolean areVerticesConnected(Vertex vertex1, Vertex vertex2) {
    return adjacencyList.getOrDefault(vertex1.getId(), Collections.emptySet()).contains(vertex2);
  }

  /**
   * Retrieves the adjacent vertices for a given vertex.
   *
   * @param vertex the vertex for which to find adjacent vertices
   * @return a set of adjacent vertices
   */
  public Set<Vertex> getAdjacentVertices(Vertex vertex) {
    return adjacencyList.getOrDefault(vertex.getId(), Collections.emptySet());
  }

  /**
   * Returns a string representation of the conflict graph, including its vertices and edges.
   *
   * @return a string representation of the conflict graph
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("ConflictGraph {\n");

    sb.append("Vertices:\n");
    for (Vertex vertex : vertices.values()) {
      sb.append(vertex).append("\n");
    }

    sb.append("Edges:\n");
    for (Edge edge : edges) {
      sb.append(edge).append("\n");
    }

    sb.append("}");

    return sb.toString();
  }

  /** Displays the conflict graph using a graphical user interface. */
  public void showGraph() {
    ConflictGraphPanel panel = new ConflictGraphPanel(this);
    panel.setOpaque(false);
    Dimension graphSize = panel.getGraphSize();
    JFrame frame = new JFrame("ConflictGraph");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    int padding = 40;
    frame.setSize(graphSize.width + padding, graphSize.height + padding + 20);
    frame.add(panel);
    frame.setVisible(true);
  }

  /**
   * Saves the current conflict graph visualization as an SVG file. The file is saved as
   * "conflictgraph.svg" in the current directory.
   */
  public void saveToSvg() {
    ConflictGraphPanel panel = new ConflictGraphPanel(this);
    Dimension graphSize = panel.getGraphSize();
    panel.setSize(graphSize);
    panel.setPreferredSize(graphSize);
    panel.setOpaque(false);
    try {
      panel.saveToSvg();
    } catch (Exception e) {
      System.err.println("Error saving the ConflictGraph as SVG: " + e.getMessage());
    }
  }
}
