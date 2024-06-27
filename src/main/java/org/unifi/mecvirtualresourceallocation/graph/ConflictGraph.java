package org.unifi.mecvirtualresourceallocation.graph;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import org.unifi.mecvirtualresourceallocation.graph.visualization.ConflictGraphPanel;

/**
 * This class represents a conflict graph derived from a hypergraph. The conflict graph is used to
 * model conflicts between hyperedges, where conflicts are defined by the presence of common
 * vertices.
 */
public class ConflictGraph {

  private Set<Vertex> vertices;
  private Set<Edge> edges;

  /** Constructs an empty conflict graph. */
  public ConflictGraph() {
    this.vertices = new HashSet<>();
    this.edges = new HashSet<>();
  }

  /**
   * Gets the vertices of the conflict graph.
   *
   * @return the vertices.
   */
  public Set<Vertex> getVertices() {
    return vertices;
  }

  /**
   * Retrieves a vertex from the conflict graph by its ID.
   *
   * @param id the ID of the vertex to retrieve.
   * @return the vertex with the specified ID, or null if not found.
   */
  public Vertex getVertexFromId(String id) {
    for (Vertex vertex : vertices) {
      if (vertex.getId().equals(id)) {
        return vertex;
      }
    }
    return null;
  }

  /**
   * Adds a vertex to the conflict graph.
   *
   * @param vertex the vertex to be added.
   */
  public void addVertex(Vertex vertex) {
    vertices.add(vertex);
  }

  /**
   * Adds an edge between two vertices in the conflict graph.
   *
   * @param vertex1 the first vertex.
   * @param vertex2 the second vertex.
   */
  public void addEdge(Vertex vertex1, Vertex vertex2) {
    edges.add(new Edge(vertex1, vertex2));
  }

  /**
   * Gets the edges of the conflict graph.
   *
   * @return the edges.
   */
  public Set<Edge> getEdges() {
    return edges;
  }

  /**
   * Returns a string representation of the conflict graph, including its vertices and edges.
   *
   * @return a string representation of the conflict graph.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("ConflictGraph {\n");

    sb.append("Vertices:\n");
    for (Vertex vertex : vertices) {
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
    JFrame frame = new JFrame("Conflict Graph");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    frame.add(new ConflictGraphPanel(this));
    frame.setVisible(true);
  }
}