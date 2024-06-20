package org.unifi.mecvirtualresourceallocation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
  public static void main(String[] args) {
    Vertex v1 = new Vertex("1", -1);
    Vertex v2 = new Vertex("2", -2);
    Vertex v3 = new Vertex("3", -3);
    Vertex v4 = new Vertex("4", -4);
    Vertex v5 = new Vertex("5", -5);
    Vertex v6 = new Vertex("6", -6);
    List<Vertex> vertices = Arrays.asList(v1, v2, v3, v4, v5, v6);

    HyperEdge p1 = new HyperEdge("1", Arrays.asList(v1, v2, v3));
    HyperEdge p2 = new HyperEdge("2", Arrays.asList(v2, v4));
    HyperEdge p3 = new HyperEdge("3", Arrays.asList(v3, v6));
    HyperEdge p4 = new HyperEdge("4", Arrays.asList(v1, v5));
    HyperEdge p5 = new HyperEdge("5", Arrays.asList(v3, v5, v6));
    HyperEdge p6 = new HyperEdge("6", Arrays.asList(v1, v4));
    List<HyperEdge> edges = Arrays.asList(p1, p2, p3, p4, p5, p6);

    HyperGraph hyperGraph = new HyperGraph(vertices, edges);

    AllocationStrategy strategy = new SequentialSearchStrategy();
    Set<Vertex> initialIndependentSet = strategy.allocate(hyperGraph);
    System.out.println("Initial Independent Set:");
    System.out.println(initialIndependentSet);
    double weight = 0;
    for (Vertex vertex : initialIndependentSet) {
      weight += vertex.getWeight();
    }
    System.out.println("Total weight: " + weight);
    ConflictGraph conflictGraph = hyperGraph.getConflictGraph();
    conflictGraph.showGraph();
  }
}
