package org.unifi.mecvirtualresourceallocation.graph;

import java.util.Objects;

/** Represents an edge in a conflict graph. Each edge connects two vertices. */
public class Edge {

  private Vertex vertex1;
  private Vertex vertex2;

  /**
   * Constructs an edge connecting the specified vertices. Ensures that the vertices are stored in a
   * consistent order based on their IDs.
   *
   * @param vertex1 the first vertex connected by the edge
   * @param vertex2 the second vertex connected by the edge
   */
  public Edge(Vertex vertex1, Vertex vertex2) {
    if (vertex1.getId().compareTo(vertex2.getId()) < 0) {
      this.vertex1 = vertex1;
      this.vertex2 = vertex2;
    } else {
      this.vertex1 = vertex2;
      this.vertex2 = vertex1;
    }
  }

  /**
   * Gets the first vertex connected by the edge.
   *
   * @return the first vertex
   */
  public Vertex getVertex1() {
    return vertex1;
  }

  /**
   * Gets the second vertex connected by the edge.
   *
   * @return the second vertex
   */
  public Vertex getVertex2() {
    return vertex2;
  }

  /**
   * Checks if this edge is equal to another object. Two edges are considered equal if they connect
   * the same pair of vertices.
   *
   * @param o the object to compare with
   * @return true if the edges are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Edge edge = (Edge) o;
    return Objects.equals(vertex1, edge.vertex1) && Objects.equals(vertex2, edge.vertex2);
  }

  /**
   * Returns a hash code value for the edge. The hash code is computed based on the vertices
   * connected by the edge.
   *
   * @return a hash code value for the edge
   */
  @Override
  public int hashCode() {
    return Objects.hash(vertex1, vertex2);
  }

  /**
   * Returns a string representation of the edge, including the IDs of the connected vertices.
   *
   * @return a string representation of the edge
   */
  @Override
  public String toString() {
    return "Edge{vertices=" + vertex1 + ", " + vertex2 + "}";
  }
}
