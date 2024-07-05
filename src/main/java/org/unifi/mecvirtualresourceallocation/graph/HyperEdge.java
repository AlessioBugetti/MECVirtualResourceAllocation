package org.unifi.mecvirtualresourceallocation.graph;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a hyperedge in a hypergraph. A hyperedge is an edge that can connect more
 * than two vertices, which in this context corresponds to linking multiple virtual machine (VM)
 * instances. Each hyperedge can be used to model the relationships between multiple VMs and
 * physical machines (PMs) in a Mobile Edge Computing (MEC) environment.
 */
public final class HyperEdge {

  private String id;
  private Set<Vertex> vertices;
  private BigDecimal weight;

  /**
   * Constructs a hyperedge with the specified id. Initializes an empty set of vertices and sets the
   * initial weight to zero.
   *
   * @param id the unique identifier of the hyperedge
   */
  public HyperEdge(String id) {
    this.id = id;
    this.vertices = new HashSet<>();
    this.weight = BigDecimal.ZERO;
  }

  /**
   * Constructs a hyperedge with the specified id, vertices, and calculates the initial weight based
   * on the sum of the weights of the vertices.
   *
   * @param id the unique identifier of the hyperedge
   * @param vertices the set of vertices that this hyperedge connects
   */
  public HyperEdge(String id, Set<Vertex> vertices) {
    if (vertices == null) {
      throw new IllegalArgumentException("HyperEdge cannot be null.");
    }
    this.id = id;
    this.vertices = new HashSet<>(vertices);
    this.weight = calculateWeight();
  }

  /**
   * Calculates the total weight of the hyperedge as the sum of the weights of its vertices.
   *
   * @return the total weight of the hyperedge
   */
  private BigDecimal calculateWeight() {
    BigDecimal totalWeight = BigDecimal.ZERO;
    for (Vertex vertex : vertices) {
      totalWeight = totalWeight.add(vertex.getNegativeWeight());
    }
    return totalWeight;
  }

  /**
   * Gets the unique identifier of the hyperedge.
   *
   * @return the unique identifier of the hyperedge
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the set of vertices that this hyperedge connects.
   *
   * @return the set of vertices
   */
  public Set<Vertex> getVertices() {
    return vertices;
  }

  /**
   * Adds a vertex to the hyperedge.
   *
   * @param vertex the vertex to be added to the hyperedge
   */
  public void addVertex(Vertex vertex) {
    if (vertex == null) {
      throw new IllegalArgumentException("Trying to add an uninitialized vertex.");
    }
    if (!vertices.add(vertex)) {
      throw new IllegalArgumentException("Duplicate vertex found: " + vertex.getId());
    }
    this.weight = this.weight.add(vertex.getNegativeWeight());
  }

  /**
   * Gets the weight of the hyperedge, which is the sum of the weights of its vertices.
   *
   * @return the weight of the hyperedge
   */
  public BigDecimal getWeight() {
    return weight.negate();
  }

  /**
   * Gets the negative weight of the hyperedge, which is the sum of the negative weights of its
   * vertices.
   *
   * @return the negative weight of the hyperedge
   */
  public BigDecimal getNegativeWeight() {
    return weight;
  }

  /**
   * Returns a string representation of the hyperedge, including its vertices and weight.
   *
   * @return a string representation of the hyperedge
   */
  @Override
  public String toString() {
    return "HyperEdge{id="
        + getId()
        + ", vertices="
        + vertices
        + ", weight="
        + weight.negate()
        + "}";
  }
}
