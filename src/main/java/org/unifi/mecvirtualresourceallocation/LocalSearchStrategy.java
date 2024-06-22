package org.unifi.mecvirtualresourceallocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The LocalSearchStrategy class implements an allocation strategy based on the (M*)-Perfect
 * Matching algorithm described in the paper "Virtual Resource Allocation for Mobile Edge Computing:
 * A Hypergraph Matching Approach".
 */
public class LocalSearchStrategy extends AllocationStrategy {

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
    Map<Vertex, Set<Vertex>> adjacencyCache = new HashMap<>();
    int index = 0;

    while (index < sortedIndependentSet.size()) {
      Vertex currentVertex = sortedIndependentSet.get(index);
      Set<Vertex> adjacentVertices =
          getAdjacentVertices(currentVertex, conflictGraph, adjacencyCache);
      List<Vertex> sortedAdjacentVertices = sortVerticesByWeightDescending(adjacentVertices);

      for (int phi = 2; phi <= 4; phi++) {
        Set<Vertex> claw =
            findClaw(independentSet, sortedAdjacentVertices, adjacencyCache, conflictGraph, phi);
        if (!claw.isEmpty()) {
          Set<Vertex> adjacentVertexIndependentSet =
              findAdjacentVertexIndependentSet(claw, independentSet, conflictGraph, adjacencyCache);
          independentSet.removeAll(adjacentVertexIndependentSet);
          independentSet.addAll(claw);
          sortedIndependentSet =
              sortVerticesByWeightAscending(independentSet); // Re-sort the updated set
          index = -1; // Restart the process
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
        .sorted(Comparator.comparing(Vertex::getWeight))
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
        .sorted(Comparator.comparing(Vertex::getWeight).reversed())
        .collect(Collectors.toList());
  }

  /**
   * Retrieves the adjacent vertices for a given vertex. If not present in the cache, calculates and
   * stores it.
   *
   * @param vertex the vertex for which to find adjacent vertices
   * @param conflictGraph the conflict graph
   * @param adjacencyCache the cache to store and retrieve adjacent vertices
   * @return a set of adjacent vertices
   */
  private Set<Vertex> getAdjacentVertices(
      Vertex vertex, ConflictGraph conflictGraph, Map<Vertex, Set<Vertex>> adjacencyCache) {
    return adjacencyCache.computeIfAbsent(vertex, v -> calculateAdjacentVertices(v, conflictGraph));
  }

  /**
   * Finds a phi-claw from the sorted adjacent vertices.
   *
   * @param independentSet the current independent set
   * @param sortedAdjacentVertices the list of adjacent vertices sorted by weight
   * @param adjacencyCache the adjacency cache
   * @param conflictGraph the conflict graph
   * @param phi the size of the claw
   * @return a set of vertices representing the claw
   */
  private Set<Vertex> findClaw(
      Set<Vertex> independentSet,
      List<Vertex> sortedAdjacentVertices,
      Map<Vertex, Set<Vertex>> adjacencyCache,
      ConflictGraph conflictGraph,
      int phi) {
    Set<Vertex> result = new HashSet<>();
    generateClaw(
        independentSet,
        result,
        sortedAdjacentVertices,
        conflictGraph,
        adjacencyCache,
        new HashSet<>(),
        phi);
    return result;
  }

  /**
   * Generates a claw recursively by adding valid vertices.
   *
   * @param independentSet the current independent set
   * @param result the set to store the valid claw
   * @param remaining the list of vertices that can still be added to the claw
   * @param conflictGraph the conflict graph
   * @param adjacencyCache the adjacency cache
   * @param currentGroup the current group being formed
   * @param phi the size of the claw
   */
  private void generateClaw(
      Set<Vertex> independentSet,
      Set<Vertex> result,
      List<Vertex> remaining,
      ConflictGraph conflictGraph,
      Map<Vertex, Set<Vertex>> adjacencyCache,
      Set<Vertex> currentGroup,
      int phi) {
    if (currentGroup.size() == phi) {
      if (isValidClaw(currentGroup, independentSet, conflictGraph, adjacencyCache, result)) {
        return;
      }
    }

    for (Vertex vertex : new ArrayList<>(remaining)) {
      if (isValidToAdd(currentGroup, vertex, conflictGraph, adjacencyCache)) {
        currentGroup.add(vertex);
        remaining.remove(vertex);
        generateClaw(
            independentSet, result, remaining, conflictGraph, adjacencyCache, currentGroup, phi);

        if (currentGroup.size() == phi) {
          if (isValidClaw(currentGroup, independentSet, conflictGraph, adjacencyCache, result)) {
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
   * @param adjacencyCache the adjacency cache
   * @param result the set to store the valid claw
   * @return true if the claw improves the weight, false otherwise
   */
  private boolean isValidClaw(
      Set<Vertex> currentGroup,
      Set<Vertex> independentSet,
      ConflictGraph conflictGraph,
      Map<Vertex, Set<Vertex>> adjacencyCache,
      Set<Vertex> result) {
    Set<Vertex> adjacentVertexIndependentSet =
        findAdjacentVertexIndependentSet(
            currentGroup, independentSet, conflictGraph, adjacencyCache);
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
   * Checks if it is valid to add a vertex to the current group.
   *
   * @param currentGroup the current group of vertices
   * @param vertex the vertex to be added
   * @param adjacencyCache the adjacency cache
   * @return true if the vertex can be added, false otherwise
   */
  private boolean isValidToAdd(
      Set<Vertex> currentGroup,
      Vertex vertex,
      ConflictGraph conflictGraph,
      Map<Vertex, Set<Vertex>> adjacencyCache) {
    return currentGroup.stream()
        .noneMatch(
            groupVertex ->
                areVerticesConnected(vertex, groupVertex, conflictGraph, adjacencyCache));
  }

  /**
   * Checks if two vertices are connected in the conflict graph using the adjacency cache.
   *
   * @param vertex1 the first vertex
   * @param vertex2 the second vertex
   * @param conflictGraph the conflict graph
   * @param adjacencyCache the adjacency cache
   * @return true if the vertices are connected, false otherwise
   */
  private boolean areVerticesConnected(
      Vertex vertex1,
      Vertex vertex2,
      ConflictGraph conflictGraph,
      Map<Vertex, Set<Vertex>> adjacencyCache) {
    if (adjacencyCache.containsKey(vertex1)) {
      return adjacencyCache.get(vertex1).contains(vertex2);
    } else if (adjacencyCache.containsKey(vertex2)) {
      return adjacencyCache.get(vertex2).contains(vertex1);
    } else {
      return getAdjacentVertices(vertex1, conflictGraph, adjacencyCache).contains(vertex2);
    }
  }

  /**
   * Finds the set of vertices in the initial independent set that are adjacent to the given set of
   * vertices.
   *
   * @param currentGroup the current group of vertices
   * @param independentSet the current independent set
   * @param conflictGraph the conflict graph
   * @param adjacencyCache the adjacency cache
   * @return a set of vertices from the initial independent set that are adjacent to the given set
   *     of vertices
   */
  private Set<Vertex> findAdjacentVertexIndependentSet(
      Set<Vertex> currentGroup,
      Set<Vertex> independentSet,
      ConflictGraph conflictGraph,
      Map<Vertex, Set<Vertex>> adjacencyCache) {
    return currentGroup.stream()
        .flatMap(vertex -> getAdjacentVertices(vertex, conflictGraph, adjacencyCache).stream())
        .filter(independentSet::contains)
        .collect(Collectors.toSet());
  }

  /**
   * Determines if the new set of vertices has a better weight than the old set.
   *
   * @param newSet the new set of vertices
   * @param oldSet the old set of vertices
   * @return true if the new set has a better weight, false otherwise
   */
  private boolean isWeightImproved(Set<Vertex> newSet, Set<Vertex> oldSet) {
    return Math.pow(calculateWeight(newSet), 2) < Math.pow(calculateWeight(oldSet), 2);
  }

  /**
   * Calculates the total weight of a set of vertices.
   *
   * @param vertices the set of vertices
   * @return the total weight of the vertices
   */
  private double calculateWeight(Set<Vertex> vertices) {
    return vertices.stream().mapToDouble(Vertex::getWeight).sum();
  }
}
