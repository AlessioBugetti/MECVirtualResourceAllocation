package org.unifi.mecvirtualresourceallocation.graph;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The Vertex class represents a vertex in a hypergraph or in a conflict graph. Each vertex
 * is characterized by a unique identifier and a weight. The identifier is used to distinguish
 * between different vertices, while the weight represent the energy consumption.
 */
public class Vertex {

  private String id;
  private BigDecimal weight;

  /**
   * Constructs a new {@code Vertex} with the specified identifier and weight.
   *
   * @param id the unique identifier of the vertex.
   * @param weight the weight associated with the vertex.
   */
  public Vertex(String id, double weight) {
    this.id = id;
    if (weight > 0) this.weight = BigDecimal.valueOf(-weight);
    else this.weight = BigDecimal.valueOf(weight);
  }

  public Vertex(String id, BigDecimal weight) {
    this.id = id;
    this.weight = weight.compareTo(BigDecimal.ZERO) > 0 ? weight.negate() : weight;
  }

  /**
   * Returns the identifier of this vertex.
   *
   * @return the identifier of the vertex.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the identifier of this vertex.
   *
   * @param id the new identifier of the vertex.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Returns the weight of this vertex.
   *
   * @return the weight of the vertex.
   */
  public BigDecimal getWeight() {
    return weight.negate();
  }

  /**
   * Returns the negative weight of this vertex.
   *
   * @return the negative weight of the vertex.
   */
  public BigDecimal getNegativeWeight() {
    return weight;
  }

  /**
   * Returns a string representation of the vertex. The string representation consists of the
   * vertex's identifier and weight, enclosed in curly braces.
   *
   * @return a string representation of the vertex.
   */
  @Override
  public String toString() {
    return "Vertex{id=" + id + ", weight=" + weight.negate() + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Vertex vertex = (Vertex) o;
    return Objects.equals(id, vertex.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
