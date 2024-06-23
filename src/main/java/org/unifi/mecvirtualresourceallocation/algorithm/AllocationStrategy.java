package org.unifi.mecvirtualresourceallocation.algorithm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.unifi.mecvirtualresourceallocation.graph.ConflictGraph;
import org.unifi.mecvirtualresourceallocation.graph.Edge;
import org.unifi.mecvirtualresourceallocation.graph.HyperEdge;
import org.unifi.mecvirtualresourceallocation.graph.HyperGraph;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

public abstract class AllocationStrategy {

  public abstract Set<Vertex> allocate(HyperGraph hyperGraph);

  public Set<HyperEdge> getHyperEdges(HyperGraph hyperGraph, Set<Vertex> vertices) {
    Set<HyperEdge> resultingHyperEdges = new HashSet<>();
    List<HyperEdge> hyperEdges = hyperGraph.getHyperEdges();

    for (Vertex vertex : vertices) {
      for (HyperEdge hyperedge : hyperEdges) {
        if (vertex.getId().equals(hyperedge.getId())) {
          resultingHyperEdges.add(hyperedge);
        }
      }
    }
    return resultingHyperEdges;
  }

  protected Set<Vertex> calculateAdjacentVertices(Vertex vertex, ConflictGraph conflictGraph) {
    Set<Vertex> adjacentVertices = new HashSet<>();
    for (Edge edge : conflictGraph.getEdges()) {
      if (edge.getVertex1().equals(vertex)) {
        adjacentVertices.add(edge.getVertex2());
      } else if (edge.getVertex2().equals(vertex)) {
        adjacentVertices.add(edge.getVertex1());
      }
    }
    return adjacentVertices;
  }
}
