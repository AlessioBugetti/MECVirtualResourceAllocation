package org.unifi.mecvirtualresourceallocation.evaluation.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.unifi.mecvirtualresourceallocation.graph.HyperEdge;
import org.unifi.mecvirtualresourceallocation.graph.HyperGraph;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

public class HyperGraphGenerator {

  public static final int DELTA = 3;

  private HyperGraphGenerator() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
  }

  public static HyperGraph generateRandomHyperGraph(int numVertices, Random rand) {
    List<Vertex> vertices = generateVertices(numVertices, rand);
    List<HyperEdge> edges = generateEdges(numVertices, rand, vertices);
    ensureAllVerticesConnected(vertices, edges, rand);
    return new HyperGraph(vertices, edges);
  }

  private static List<Vertex> generateVertices(int numVertices, Random rand) {
    List<Vertex> vertices = new ArrayList<>();
    for (int i = 1; i <= numVertices; i++) {
      vertices.add(new Vertex(String.valueOf(i), rand.nextDouble() * 10));
    }
    return vertices;
  }

  private static List<HyperEdge> generateEdges(
      int numVertices, Random rand, List<Vertex> vertices) {
    List<HyperEdge> edges = new ArrayList<>();
    Set<Set<Vertex>> uniqueEdgeSets = new HashSet<>();
    int maxEdges = MathUtils.sumOfBinomials(numVertices, DELTA);

    for (int i = 1; i <= rand.nextInt(maxEdges) + 1; i++) {
      Set<Vertex> edgeVertices = new HashSet<>();
      int edgeSize = numVertices < 4 ? rand.nextInt(numVertices) + 1 : rand.nextInt(4) + 1;
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

  private static void ensureAllVerticesConnected(
      List<Vertex> vertices, List<HyperEdge> edges, Random rand) {
    for (Vertex vertex : vertices) {
      boolean found = edges.stream().anyMatch(edge -> edge.getVertices().contains(vertex));
      if (!found) {
        edges.get(rand.nextInt(edges.size())).getVertices().add(vertex);
      }
    }
  }
}
