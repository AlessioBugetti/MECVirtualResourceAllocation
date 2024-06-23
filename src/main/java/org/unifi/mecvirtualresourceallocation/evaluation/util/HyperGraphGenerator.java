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

  private HyperGraphGenerator() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
  }

  public static HyperGraph generateRandomHyperGraph(int numVertices, Random rand) {
    List<Vertex> vertices = new ArrayList<>();
    for (int i = 1; i <= numVertices; i++) {
      vertices.add(new Vertex(String.valueOf(i), rand.nextDouble() * 10));
    }

    List<HyperEdge> edges = new ArrayList<>();
    for (int i = 1; i <= numVertices; i++) {
      Set<Vertex> edgeVertices = new HashSet<>();
      int edgeSize;
      if (numVertices < 4) {
        edgeSize = rand.nextInt(numVertices) + 1;
      } else {
        edgeSize = rand.nextInt(4) + 1;
      }
      while (edgeVertices.size() < edgeSize) {
        edgeVertices.add(vertices.get(rand.nextInt(numVertices)));
      }
      edges.add(new HyperEdge(String.valueOf(i), new ArrayList<>(edgeVertices)));
    }

    for (Vertex vertex : vertices) {
      boolean found = false;
      for (HyperEdge edge : edges) {
        if (edge.getVertices().contains(vertex)) {
          found = true;
          break;
        }
      }
      if (!found) {
        edges.get(rand.nextInt(edges.size())).getVertices().add(vertex);
      }
    }

    return new HyperGraph(vertices, edges);
  }
}
