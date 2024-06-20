package org.unifi.mecvirtualresourceallocation;

/**
 * Represents an edge in a conflict graph. Each edge connects two vertices.
 */
public class Edge {
  private Vertex vertex1;
  private Vertex vertex2;

  /**
   * Constructs an edge connecting the specified vertices.
   *
   * @param vertex1 the first vertex connected by the edge.
   * @param vertex2 the second vertex connected by the edge.
   */
  public Edge(Vertex vertex1, Vertex vertex2) {
    this.vertex1 = vertex1;
    this.vertex2 = vertex2;
  }

  /**
   * Gets the first vertex connected by the edge.
   *
   * @return the first vertex.
   */
  public Vertex getVertex1() { return vertex1; }

  /**
   * Gets the second vertex connected by the edge.
   *
   * @return the second vertex.
   */
  public Vertex getVertex2() { return vertex2; }

  /**
   * Returns a string representation of the edge, including the IDs of the
   * connected vertices.
   *
   * @return a string representation of the edge.
   */
  @Override
  public String toString() {
    return "Edge{vertices=" + vertex1 + ", " + vertex2 + "}";
  }
}
