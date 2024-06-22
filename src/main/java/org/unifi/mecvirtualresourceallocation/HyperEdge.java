package org.unifi.mecvirtualresourceallocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a hyperedge in a hypergraph. A hyperedge is an edge that can connect more
 * than two vertices, which in this context corresponds to linking multiple virtual machine (VM)
 * instances. Each hyperedge can be used to model the relationships between multiple VMs and
 * physical machines (PMs) in a Mobile Edge Computing (MEC) environment.
 */
public class HyperEdge {

  private String id;
  private List<Vertex> vertices;
  private double weight;

  /**
   * Constructs a hyperedge with the specified id. Initializes an empty list of vertices and sets
   * the initial weight to zero.
   *
   * @param id the unique identifier of the hyperedge.
   */
  public HyperEdge(String id) {
    this.id = id;
    this.vertices = new ArrayList<>();
    this.weight = 0;
  }

  /**
   * Constructs a hyperedge with the specified id, vertices, and calculates the initial weight based
   * on the sum of the weights of the vertices.
   *
   * @param id the unique identifier of the hyperedge.
   * @param vertices the list of vertices that this hyperedge connects.
   */
  public HyperEdge(String id, List<Vertex> vertices) {
    if (hasDuplicateVertices(vertices)) {
      throw new IllegalArgumentException("HyperEdge cannot contain duplicate vertices.");
    }
    this.id = id;
    this.vertices = vertices;
    this.weight = calculateWeight();
  }

  private boolean hasDuplicateVertices(List<Vertex> vertices) {
    Set<Vertex> set = new HashSet<>(vertices);
    return set.size() != vertices.size();
  }

  /**
   * Calculates the total weight of the hyperedge as the sum of the weights of its vertices.
   *
   * @return the total weight of the hyperedge.
   */
  private double calculateWeight() {
    double totalWeight = 0;
    for (Vertex vertex : vertices) {
      totalWeight += vertex.getNegativeWeight();
    }
    return totalWeight;
  }

  /**
   * Gets the list of vertices that this hyperedge connects.
   *
   * @return the list of vertices.
   */
  public List<Vertex> getVertices() {
    return vertices;
  }

  /**
   * Adds a vertex to the hyperedge. If the list of vertices is null, it initializes it.
   *
   * @param vertex the vertex to be added to the hyperedge.
   */
  public void addVertex(Vertex vertex) {
    if (vertex == null) {
      throw new IllegalArgumentException("Trying to add an uninitialized vertex.");
    }
    if (vertices.contains(vertex)) {
      throw new IllegalArgumentException("Duplicate vertex found: " + vertex.getId());
    }
    this.vertices.add(vertex);
    this.weight += vertex.getNegativeWeight();
  }

  /**
   * Gets the unique identifier of the hyperedge.
   *
   * @return the unique identifier of the hyperedge.
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the weight of the hyperedge, which is the sum of the weights of its vertices.
   *
   * @return the weight of the hyperedge.
   */
  public double getWeight() {
    return -weight;
  }

  /**
   * Gets the negative weight of the hyperedge, which is the sum of the negative weights of its vertices.
   *
   * @return the negative weight of the hyperedge.
   */
  public double getNegativeWeight() {
    return weight;
  }

  /**
   * Returns a string representation of the hyperedge, including its vertices and weight.
   *
   * @return a string representation of the hyperedge.
   */
  @Override
  public String toString() {
    return "HyperEdge{id=" + getId() + ", vertices=" + vertices + ", weight=" + -weight + "}";
  }
}
