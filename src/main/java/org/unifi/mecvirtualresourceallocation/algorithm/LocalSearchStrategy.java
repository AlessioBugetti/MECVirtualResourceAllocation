package org.unifi.mecvirtualresourceallocation.algorithm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.unifi.mecvirtualresourceallocation.evaluation.util.HyperGraphGenerator;
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
   * Allocates resources based on the local search strategy.
   *
   * @param hyperGraph the hypergraph used to allocate resources
   * @return a set of vertices in the conflict graph selected by the allocation strategy
   */
  public Set<Vertex> allocate(HyperGraph hyperGraph) {
    ConflictGraph conflictGraph = hyperGraph.getConflictGraph();
    Set<Vertex> independentSet = new SequentialSearchStrategy().allocate(hyperGraph);
    optimizeIndependentSet(independentSet, conflictGraph);
    return independentSet;
  }

  /**
   * Optimizes the given independent set by searching for better sets using local search.
   *
   * @param independentSet the initial independent set to be optimized
   * @param conflictGraph the conflict graph derived from the original hypergraph
   */
  private void optimizeIndependentSet(Set<Vertex> independentSet, ConflictGraph conflictGraph) {
    List<Vertex> sortedIndependentSet = sortVerticesByWeightAscending(independentSet);
    int index = 0;

    while (index < sortedIndependentSet.size()) {
      Vertex currentVertex = sortedIndependentSet.get(index);
      Set<Vertex> adjacentVertices = conflictGraph.getAdjacentVertices(currentVertex);
      List<Vertex> sortedAdjacentVertices = sortVerticesByWeightDescending(adjacentVertices);

      for (int phi = 2; phi <= HyperGraphGenerator.DELTA; phi++) {
        Set<Vertex> claw = findClaw(independentSet, sortedAdjacentVertices, conflictGraph, phi);
        if (!claw.isEmpty()) {
          Set<Vertex> adjacentVertexIndependentSet =
              findAdjacentVertexIndependentSet(claw, independentSet, conflictGraph);
          independentSet.removeAll(adjacentVertexIndependentSet);
          independentSet.addAll(claw);
          sortedIndependentSet = sortVerticesByWeightAscending(independentSet);
          index = -1;
          break;
        }
      }
      index++;
    }
  }

  /**
   * Sorts a collection of vertices by their weight in ascending order.
   *
   * @param vertices the collection of vertices to be sorted
   * @return a list of vertices sorted by weight in ascending order
   */
  private List<Vertex> sortVerticesByWeightAscending(Collection<Vertex> vertices) {
    return vertices.stream()
        .sorted(Comparator.comparing(Vertex::getNegativeWeight))
        .collect(Collectors.toList());
  }

  /**
   * Sorts a collection of vertices by their weight in descending order.
   *
   * @param vertices the collection of vertices to be sorted
   * @return a list of vertices sorted by weight in descending order
   */
  private List<Vertex> sortVerticesByWeightDescending(Collection<Vertex> vertices) {
    return vertices.stream()
        .sorted(Comparator.comparing(Vertex::getNegativeWeight).reversed())
        .collect(Collectors.toList());
  }

  /**
   * Finds a phi-claw from the sorted adjacent vertices.
   *
   * @param independentSet the current independent set
   * @param sortedAdjacentVertices the list of adjacent vertices sorted by weight
   * @param conflictGraph the conflict graph
   * @param phi the size of the claw
   * @return a set of vertices representing the claw
   */
  private Set<Vertex> findClaw(
      Set<Vertex> independentSet,
      List<Vertex> sortedAdjacentVertices,
      ConflictGraph conflictGraph,
      int phi) {
    Set<Vertex> result = new HashSet<>();
    generateClaw(
        independentSet, result, sortedAdjacentVertices, conflictGraph, new HashSet<>(), phi);
    return result;
  }

  /**
   * Generates a claw recursively by adding valid vertices.
   *
   * @param independentSet the current independent set
   * @param result the set to store the valid claw
   * @param remaining the list of vertices that can still be added to the claw
   * @param conflictGraph the conflict graph
   * @param currentGroup the current group being formed
   * @param phi the size of the claw
   */
  private void generateClaw(
      Set<Vertex> independentSet,
      Set<Vertex> result,
      List<Vertex> remaining,
      ConflictGraph conflictGraph,
      Set<Vertex> currentGroup,
      int phi) {
    if (currentGroup.size() == phi) {
      if (isValidClaw(currentGroup, independentSet, conflictGraph, result)) {
        return;
      }
    }

    for (Vertex vertex : new ArrayList<>(remaining)) {
      if (isValidToAdd(currentGroup, vertex, conflictGraph)) {
        currentGroup.add(vertex);
        remaining.remove(vertex);
        generateClaw(independentSet, result, remaining, conflictGraph, currentGroup, phi);

        if (currentGroup.size() == phi) {
          if (isValidClaw(currentGroup, independentSet, conflictGraph, result)) {
            return;
          }
        }

        currentGroup.remove(vertex);
        remaining.add(vertex);
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
