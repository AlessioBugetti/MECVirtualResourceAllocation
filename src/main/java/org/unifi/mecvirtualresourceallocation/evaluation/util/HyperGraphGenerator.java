package org.unifi.mecvirtualresourceallocation.evaluation.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.unifi.mecvirtualresourceallocation.graph.HyperEdge;
import org.unifi.mecvirtualresourceallocation.graph.HyperGraph;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

/** Utility class for generating random hypergraphs. */
public final class HyperGraphGenerator {

  /** Private constructor to prevent instantiation of this utility class. */
  private HyperGraphGenerator() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
  }

  /**
   * Generates a random hypergraph with the specified number of vertices.
   *
   * @param numVertices the number of vertices in the hypergraph
   * @param rand the Random instance used for generating random numbers
   * @param delta the delta (δ) value used for generating hypergraphs
   * @return a randomly generated HyperGraph
   */
  public static HyperGraph generateRandomHyperGraph(int numVertices, int delta, Random rand) {
    Set<Vertex> vertices = generateVertices(numVertices, rand);
    Set<HyperEdge> hyperEdges = generateHyperEdges(numVertices, delta, rand, vertices);
    ensureAllVerticesConnected(vertices, hyperEdges, delta);
    return new HyperGraph(vertices, hyperEdges);
  }

  /**
   * Generates a set of vertices with random weights.
   *
   * @param numVertices the number of vertices to generate
   * @param rand the Random instance used for generating random numbers
   * @return a set of generated vertices
   */
  private static Set<Vertex> generateVertices(int numVertices, Random rand) {
    Set<Vertex> vertices = new HashSet<>();
    for (int i = 1; i <= numVertices; i++) {
      vertices.add(new Vertex(String.valueOf(i), rand.nextDouble() * 10));
    }
    return vertices;
  }

  /**
   * Generates a set of hyperedges connecting the vertices.
   *
   * @param numVertices the number of vertices in the hypergraph
   * @param delta the delta (δ) value used
   * @param rand the Random instance used for generating random numbers
   * @param vertices the set of vertices to be connected by hyperedges
   * @return a set of generated hyperedges
   */
  private static Set<HyperEdge> generateHyperEdges(
      int numVertices, int delta, Random rand, Set<Vertex> vertices) {
    Set<HyperEdge> hyperEdges = new HashSet<>();
    Set<Set<Vertex>> uniqueHyperEdgeSets = new HashSet<>();
    int numHyperEdges = numVertices / 2 > 0 ? numVertices / 2 : 1;
    List<Vertex> vertexList = new ArrayList<>(vertices);

    for (int i = 1; i <= numHyperEdges; i++) {
      Set<Vertex> edgeVertices = new HashSet<>();
      int edgeSize = numVertices < delta ? rand.nextInt(numVertices) + 1 : rand.nextInt(delta) + 1;
      while (edgeVertices.size() < edgeSize) {
        edgeVertices.add(vertexList.get(rand.nextInt(numVertices)));
      }
      if (i == 1) {
        uniqueHyperEdgeSets.add(edgeVertices);
        hyperEdges.add(new HyperEdge(String.valueOf(i), edgeVertices));
      } else if (uniqueHyperEdgeSets.contains(edgeVertices)) {
        i--;
      } else {
        uniqueHyperEdgeSets.add(edgeVertices);
        hyperEdges.add(new HyperEdge(String.valueOf(i), edgeVertices));
      }
    }
    return hyperEdges;
  }

  /**
   * Ensures that all vertices are connected by at least one hyperedge.
   *
   * @param vertices the set of vertices in the hypergraph
   * @param hyperEdges the set of hyperedges in the hypergraph
   */
  private static void ensureAllVerticesConnected(
      Set<Vertex> vertices, Set<HyperEdge> hyperEdges, int delta) {
    for (Vertex vertex : vertices) {
      boolean found = hyperEdges.stream().anyMatch(edge -> edge.getVertices().contains(vertex));
      if (!found) {
        Optional<HyperEdge> optionalTargetEdge =
            hyperEdges.stream()
                .filter(hyperEdge -> hyperEdge.getVertices().size() < delta)
                .min(Comparator.comparingInt(hyperEdge -> hyperEdge.getVertices().size()));

        if (optionalTargetEdge.isPresent()) {
          optionalTargetEdge.get().getVertices().add(vertex);
        } else {
          Map<Vertex, Long> vertexFrequency =
              hyperEdges.stream()
                  .flatMap(hyperEdge -> hyperEdge.getVertices().stream())
                  .collect(Collectors.groupingBy(v -> v, Collectors.counting()));

          Vertex mostFrequentVertex =
              vertexFrequency.entrySet().stream()
                  .max(Map.Entry.comparingByValue())
                  .orElseThrow(() -> new IllegalStateException("No vertices found in hyperedges."))
                  .getKey();

          HyperEdge targetEdge =
              hyperEdges.stream()
                  .filter(hyperEdge -> hyperEdge.getVertices().contains(mostFrequentVertex))
                  .max(Comparator.comparingInt(hyperEdge -> hyperEdge.getVertices().size()))
                  .orElseThrow(
                      () ->
                          new IllegalStateException(
                              "No hyperedge containing the most frequent vertex found."));

          targetEdge.getVertices().remove(mostFrequentVertex);
          targetEdge.getVertices().add(vertex);
        }
      }
    }
  }
}
