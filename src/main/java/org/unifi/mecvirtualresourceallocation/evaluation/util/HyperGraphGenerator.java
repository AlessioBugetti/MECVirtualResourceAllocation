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
public final class HyperGraphGenerator {
  /** The δ value used for generating hypergraphs. */
  public static final int DELTA = 3;

  /** Private constructor to prevent instantiation of this utility class. */
  private HyperGraphGenerator() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
  }

  /**
   * Generates a random hypergraph with the specified number of vertices.
   *
   * @param numVertices the number of vertices in the hypergraph
   * @param rand the Random instance used for generating random numbers
   * @return a randomly generated HyperGraph
   */
  public static HyperGraph generateRandomHyperGraph(int numVertices, Random rand) {
    Set<Vertex> vertices = generateVertices(numVertices, rand);
    Set<HyperEdge> edges = generateEdges(numVertices, rand, vertices);
    ensureAllVerticesConnected(vertices, edges);
    return new HyperGraph(vertices, edges);
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
   * @param rand the Random instance used for generating random numbers
   * @param vertices the set of vertices to be connected by hyperedges
   * @return a set of generated hyperedges
   */
  private static Set<HyperEdge> generateEdges(int numVertices, Random rand, Set<Vertex> vertices) {
    Set<HyperEdge> edges = new HashSet<>();
    Set<Set<Vertex>> uniqueEdgeSets = new HashSet<>();
    int maxEdges = MathUtils.sumOfBinomials(numVertices, DELTA) - 1;
    List<Vertex> vertexList = new ArrayList<>(vertices);

    for (int i = 1; i <= rand.nextInt(maxEdges) + 1; i++) {
      Set<Vertex> edgeVertices = new HashSet<>();
      int edgeSize = numVertices < DELTA ? rand.nextInt(numVertices) + 1 : rand.nextInt(DELTA) + 1;
      while (edgeVertices.size() < edgeSize) {
        edgeVertices.add(vertexList.get(rand.nextInt(numVertices)));
      }
      if (i == 1) {
        uniqueEdgeSets.add(edgeVertices);
        edges.add(new HyperEdge(String.valueOf(i), edgeVertices));
      } else if (uniqueEdgeSets.contains(edgeVertices)) {
        i--;
      } else {
        uniqueEdgeSets.add(edgeVertices);
        edges.add(new HyperEdge(String.valueOf(i), edgeVertices));
      }
    }
    return edges;
  }

  /**
   * Ensures that all vertices are connected by at least one hyperedge.
   *
   * @param vertices the set of vertices in the hypergraph
   * @param edges the set of hyperedges in the hypergraph
   */
  private static void ensureAllVerticesConnected(Set<Vertex> vertices, Set<HyperEdge> edges) {
    for (Vertex vertex : vertices) {
      boolean found = edges.stream().anyMatch(edge -> edge.getVertices().contains(vertex));
      if (!found) {
        Set<Vertex> newHyperEdge = new HashSet<>();
        newHyperEdge.add(vertex);
        edges.add(new HyperEdge(String.valueOf(edges.size() + 1), newHyperEdge));
      }
    }
  }
}
