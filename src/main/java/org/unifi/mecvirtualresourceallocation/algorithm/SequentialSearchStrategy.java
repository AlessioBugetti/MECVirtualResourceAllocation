package org.unifi.mecvirtualresourceallocation.algorithm;

import java.util.HashSet;
import java.util.Set;
import org.unifi.mecvirtualresourceallocation.graph.ConflictGraph;
import org.unifi.mecvirtualresourceallocation.graph.HyperGraph;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

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
    if (vertices == null || vertices.isEmpty()) {
      throw new IllegalArgumentException("The set of vertices cannot be null or empty.");
    }

    Vertex maxVertex = null;
    for (Vertex vertex : vertices) {
      if (maxVertex == null
          || vertex.getNegativeWeight().compareTo(maxVertex.getNegativeWeight()) > 0) {
        maxVertex = vertex;
      }
    }

    return maxVertex;
  }
}
