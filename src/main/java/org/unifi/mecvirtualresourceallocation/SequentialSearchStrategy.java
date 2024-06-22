package org.unifi.mecvirtualresourceallocation;

import java.util.HashSet;
import java.util.Set;

public class SequentialSearchStrategy extends AllocationStrategy {

  @Override
  public Set<Vertex> allocate(HyperGraph hyperGraph) {
    ConflictGraph conflictGraph = hyperGraph.getConflictGraph();
    Set<Vertex> selectedVertices = new HashSet<>();
    Set<Vertex> vertices = new HashSet<>(conflictGraph.getVertices());
    while (!vertices.isEmpty()) {
      Vertex maxWeightVertex = findMaxWeightVertex(vertices);
      Set<Vertex> adjacentVertices = calculateAdjacentVertices(maxWeightVertex, conflictGraph);
      selectedVertices.add(maxWeightVertex);
      vertices.remove(maxWeightVertex);
      vertices.removeAll(adjacentVertices);
    }
    return selectedVertices;
  }

  private Vertex findMaxWeightVertex(Set<Vertex> vertices) {
    Vertex maxVertex = null;
    double maxWeight = Double.NEGATIVE_INFINITY;
    for (Vertex vertex : vertices) {
      if (vertex.getWeight() > maxWeight) {
        maxVertex = vertex;
        maxWeight = vertex.getWeight();
      }
    }
    return maxVertex;
  }
}
