package org.unifi.mecvirtualresourceallocation.evaluation.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.unifi.mecvirtualresourceallocation.graph.HyperEdge;
import org.unifi.mecvirtualresourceallocation.graph.HyperGraph;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

/** Utility class for generating random hypergraphs. */
public class HyperGraphGenerator {

  public static final int DELTA = 3;

  /** Private constructor to prevent instantiation of this utility class. */
  private HyperGraphGenerator() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
  }

  /**
   * Generates a random hypergraph with the specified number of vertices.
   *
   * @param numVertices the number of vertices in the hypergraph.
   * @param rand the Random instance used for generating random numbers.
   * @return a randomly generated HyperGraph.
   */
  public static HyperGraph generateRandomHyperGraph(int numVertices, Random rand) {
    List<Vertex> vertices = generateVertices(numVertices, rand);
    List<HyperEdge> edges = generateEdges(numVertices, rand, vertices);
    ensureAllVerticesConnected(vertices, edges);
    return new HyperGraph(vertices, edges);
  }

  /**
   * Generates a list of vertices with random weights.
   *
   * @param numVertices the number of vertices to generate.
   * @param rand the Random instance used for generating random numbers.
   * @return a list of generated vertices.
   */
  private static List<Vertex> generateVertices(int numVertices, Random rand) {
    List<Vertex> vertices = new ArrayList<>();
    for (int i = 1; i <= numVertices; i++) {
      vertices.add(new Vertex(String.valueOf(i), rand.nextDouble() * 10));
    }
    return vertices;
  }

  /**
   * Generates a list of hyperedges connecting the vertices.
   *
   * @param numVertices the number of vertices in the hypergraph.
   * @param rand the Random instance used for generating random numbers.
   * @param vertices the list of vertices to be connected by hyperedges.
   * @return a list of generated hyperedges.
   */
  private static List<HyperEdge> generateEdges(
      int numVertices, Random rand, List<Vertex> vertices) {
    List<HyperEdge> edges = new ArrayList<>();
    Set<Set<Vertex>> uniqueEdgeSets = new HashSet<>();
    int maxEdges = MathUtils.sumOfBinomials(numVertices, DELTA) - 1;

    for (int i = 1; i <= rand.nextInt(maxEdges) + 1; i++) {
      Set<Vertex> edgeVertices = new HashSet<>();
      int edgeSize = numVertices < DELTA ? rand.nextInt(numVertices) + 1 : rand.nextInt(DELTA) + 1;
      while (edgeVertices.size() < edgeSize) {
        edgeVertices.add(vertices.get(rand.nextInt(numVertices)));
      }
      if (i == 1) {
        uniqueEdgeSets.add(edgeVertices);
        edges.add(new HyperEdge(String.valueOf(i), new ArrayList<>(edgeVertices)));
      } else if (uniqueEdgeSets.contains(edgeVertices)) {
        i--;
      } else {
        uniqueEdgeSets.add(edgeVertices);
        edges.add(new HyperEdge(String.valueOf(i), new ArrayList<>(edgeVertices)));
      }
    }
    return edges;
  }

  /**
   * Ensures that all vertices are connected by at least one hyperedge.
   *
   * @param vertices the list of vertices in the hypergraph.
   * @param edges the list of hyperedges in the hypergraph.
   */
  private static void ensureAllVerticesConnected(List<Vertex> vertices, List<HyperEdge> edges) {
    for (Vertex vertex : vertices) {
      boolean found = edges.stream().anyMatch(edge -> edge.getVertices().contains(vertex));
      if (!found) {
        Set<Vertex> newHyperEdge = new HashSet<>();
        newHyperEdge.add(vertex);
        edges.add(new HyperEdge(String.valueOf(edges.size() + 1), new ArrayList<>(newHyperEdge)));
      }
    }
  }
}
