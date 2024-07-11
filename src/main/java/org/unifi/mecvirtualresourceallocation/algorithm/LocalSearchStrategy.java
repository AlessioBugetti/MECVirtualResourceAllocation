package org.unifi.mecvirtualresourceallocation.algorithm;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import org.unifi.mecvirtualresourceallocation.graph.ConflictGraph;
import org.unifi.mecvirtualresourceallocation.graph.HyperGraph;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

/**
 * The LocalSearchStrategy class implements an allocation strategy based on the (M*)-Perfect
 * Matching algorithm described in the paper "Virtual Resource Allocation for Mobile Edge Computing:
 * A Hypergraph Matching Approach".
 */
public class LocalSearchStrategy implements AllocationStrategy {

  /**
   * Allocates resources based on the local search strategy with a default δ value of 3.
   *
   * @param hyperGraph the hypergraph used to allocate resources
   * @return a set of vertices in the conflict graph selected by the allocation strategy
   */
  @Override
  public Set<Vertex> allocate(HyperGraph hyperGraph) {
    return allocate(hyperGraph, 3);
  }

  /**
   * Allocates resources based on the local search strategy.
   *
   * @param hyperGraph the hypergraph used to allocate resources
   * @param delta the δ value used
   * @return a set of vertices in the conflict graph selected by the allocation strategy
   */
  public Set<Vertex> allocate(HyperGraph hyperGraph, int delta) {
    ConflictGraph conflictGraph = hyperGraph.getConflictGraph();
    Set<Vertex> independentSet = new SequentialSearchStrategy().allocate(hyperGraph);
    optimizeIndependentSet(independentSet, conflictGraph, delta);
    return independentSet;
  }

  /**
   * Optimizes the given independent set by searching for better sets using local search.
   *
   * @param independentSet the initial independent set to be optimized
   * @param conflictGraph the conflict graph derived from the original hypergraph
   * @param delta the δ value used
   */
  private void optimizeIndependentSet(
      Set<Vertex> independentSet, ConflictGraph conflictGraph, int delta) {
    PriorityQueue<Vertex> sortedIndependentSet =
        new PriorityQueue<>(Comparator.comparing(Vertex::getNegativeWeight));
    sortedIndependentSet.addAll(independentSet);

    while (!sortedIndependentSet.isEmpty()) {
      Vertex currentVertex = sortedIndependentSet.poll();
      Set<Vertex> adjacentVertices = conflictGraph.getAdjacentVertices(currentVertex);

      for (int phi = 2; phi <= delta; phi++) {
        Set<Vertex> claw = findClaw(independentSet, adjacentVertices, conflictGraph, phi);
        if (!claw.isEmpty()) {
          Set<Vertex> adjacentVertexIndependentSet =
              findAdjacentVertexIndependentSet(claw, independentSet, conflictGraph);
          independentSet.removeAll(adjacentVertexIndependentSet);
          independentSet.addAll(claw);
          sortedIndependentSet.clear();
          sortedIndependentSet.addAll(independentSet);
          break;
        }
      }
    }
  }

  /**
   * Finds a phi-claw from the sorted adjacent vertices.
   *
   * @param independentSet the current independent set
   * @param adjacentVertices the set of adjacent vertices
   * @param conflictGraph the conflict graph
   * @param phi the size of the claw
   * @return a set of vertices representing the claw
   */
  private Set<Vertex> findClaw(
      Set<Vertex> independentSet,
      Set<Vertex> adjacentVertices,
      ConflictGraph conflictGraph,
      int phi) {
    Set<Vertex> result = new HashSet<>();
    PriorityQueue<Vertex> sortedAdjacentVertices =
        new PriorityQueue<>(Comparator.comparing(Vertex::getNegativeWeight).reversed());
    sortedAdjacentVertices.addAll(adjacentVertices);
    generateClaw(
        independentSet, result, sortedAdjacentVertices, conflictGraph, new HashSet<>(), phi);
    return result;
  }

  /**
   * Generates a claw recursively by adding valid vertices.
   *
   * @param independentSet the current independent set
   * @param result the set to store the valid claw
   * @param remaining the priority queue of vertices that can still be added to the claw
   * @param conflictGraph the conflict graph
   * @param currentGroup the current group being formed
   * @param phi the size of the claw
   */
  private void generateClaw(
      Set<Vertex> independentSet,
      Set<Vertex> result,
      PriorityQueue<Vertex> remaining,
      ConflictGraph conflictGraph,
      Set<Vertex> currentGroup,
      int phi) {
    if (currentGroup.size() == phi) {
      if (isValidClaw(currentGroup, independentSet, conflictGraph, result)) {
        return;
      }
    } else {
      while (!remaining.isEmpty()) {
        Vertex vertex = remaining.poll();
        if (isValidToAdd(currentGroup, vertex, conflictGraph)) {
          currentGroup.add(vertex);
          generateClaw(
              independentSet,
              result,
              new PriorityQueue<>(remaining),
              conflictGraph,
              currentGroup,
              phi);
          if (result.size() == phi) {
            return;
          }
          currentGroup.remove(vertex);
        }
      }
    }
  }

  /**
   * Checks if the given claw improves the weight of the independent set.
   *
   * @param currentGroup the current group of vertices forming the claw
   * @param independentSet the current independent set
   * @param conflictGraph the conflict graph
   * @param result the set to store the valid claw
   * @return true if the claw improves the weight, false otherwise
   */
  private boolean isValidClaw(
      Set<Vertex> currentGroup,
      Set<Vertex> independentSet,
      ConflictGraph conflictGraph,
      Set<Vertex> result) {
    Set<Vertex> adjacentVertexIndependentSet =
        findAdjacentVertexIndependentSet(currentGroup, independentSet, conflictGraph);
    Set<Vertex> resultingVertices = new HashSet<>(independentSet);
    resultingVertices.removeAll(adjacentVertexIndependentSet);
    resultingVertices.addAll(currentGroup);
    if (isWeightImproved(resultingVertices, independentSet)) {
      result.addAll(currentGroup);
      return true;
    }
    return false;
  }

  /**
   * Determines if the new set of vertices has a better weight than the old set.
   *
   * @param newSet the new set of vertices
   * @param oldSet the old set of vertices
   * @return true if the new set has a better weight, false otherwise
   */
  private boolean isWeightImproved(Set<Vertex> newSet, Set<Vertex> oldSet) {
    BigDecimal oldWeightSquared = calculateWeight(oldSet).pow(2);
    BigDecimal newWeightSquared = calculateWeight(newSet).pow(2);
    return newWeightSquared.compareTo(oldWeightSquared) < 0;
  }

  /**
   * Calculates the total weight of a set of vertices.
   *
   * @param vertices the set of vertices
   * @return the total weight of the vertices
   */
  private BigDecimal calculateWeight(Set<Vertex> vertices) {
    return vertices.stream()
        .map(Vertex::getNegativeWeight)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Checks if it is valid to add a vertex to the current group.
   *
   * @param currentGroup the current group of vertices
   * @param vertex the vertex to be added
   * @param conflictGraph the conflict graph
   * @return true if the vertex can be added, false otherwise
   */
  private boolean isValidToAdd(
      Set<Vertex> currentGroup, Vertex vertex, ConflictGraph conflictGraph) {
    return currentGroup.stream()
        .noneMatch(groupVertex -> conflictGraph.areVerticesConnected(vertex, groupVertex));
  }

  /**
   * Finds the set of vertices in the initial independent set that are adjacent to the given set of
   * vertices.
   *
   * @param currentGroup the current group of vertices
   * @param independentSet the current independent set
   * @param conflictGraph the conflict graph
   * @return a set of vertices from the initial independent set that are adjacent to the given set
   *     of vertices
   */
  private Set<Vertex> findAdjacentVertexIndependentSet(
      Set<Vertex> currentGroup, Set<Vertex> independentSet, ConflictGraph conflictGraph) {
    return currentGroup.stream()
        .flatMap(vertex -> conflictGraph.getAdjacentVertices(vertex).stream())
        .filter(independentSet::contains)
        .collect(Collectors.toSet());
  }
}
