package org.unifi.mecvirtualresourceallocation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unifi.mecvirtualresourceallocation.algorithm.AllocationStrategy;
import org.unifi.mecvirtualresourceallocation.algorithm.LocalSearchStrategy;
import org.unifi.mecvirtualresourceallocation.graph.HyperEdge;
import org.unifi.mecvirtualresourceallocation.graph.HyperGraph;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

public class LocalSearchStrategyTest {

  private AllocationStrategy strategy;
  private HyperGraph hyperGraph;
  private Vertex v3;
  private Vertex v4;
  private HyperEdge p3;
  private HyperEdge p4;

  @BeforeEach
  void setUp() {
    Vertex v1 = new Vertex("1", 1.0);
    Vertex v2 = new Vertex("2", 2.0);
    v3 = new Vertex("3", 3.0);
    v4 = new Vertex("4", 4.0);
    Vertex v5 = new Vertex("5", 5.0);
    Vertex v6 = new Vertex("6", 6.0);
    Vertex v7 = new Vertex("7", 7.0);
    Vertex v8 = new Vertex("8", 8.0);
    Vertex v9 = new Vertex("9", 9.0);
    Vertex v10 = new Vertex("10", 10.0);

    Set<Vertex> vertices = new HashSet<>(Arrays.asList(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10));

    HyperEdge p1 = new HyperEdge("1", new HashSet<>(Arrays.asList(v1, v2, v3)));
    HyperEdge p2 = new HyperEdge("2", new HashSet<>(Arrays.asList(v2, v4)));
    p3 = new HyperEdge("3", new HashSet<>(Arrays.asList(v3, v6)));
    p4 = new HyperEdge("4", new HashSet<>(Arrays.asList(v1, v5)));
    HyperEdge p5 = new HyperEdge("5", new HashSet<>(Arrays.asList(v3, v5, v6)));
    HyperEdge p6 = new HyperEdge("6", new HashSet<>(Arrays.asList(v1, v4)));
    HyperEdge p7 = new HyperEdge("7", new HashSet<>(Arrays.asList(v4, v7, v8)));
    HyperEdge p8 = new HyperEdge("8", new HashSet<>(Arrays.asList(v5, v7, v10)));
    HyperEdge p9 = new HyperEdge("9", new HashSet<>(Arrays.asList(v6, v8, v9, v10)));
    HyperEdge p10 = new HyperEdge("10", new HashSet<>(Arrays.asList(v1, v2, v4, v7)));

    Set<HyperEdge> edges = new HashSet<>(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10));

    hyperGraph = new HyperGraph(vertices, edges);
    strategy = new LocalSearchStrategy();
  }

  @Test
  void testAllocate() {

    Set<Vertex> allocatedVertices = strategy.allocate(hyperGraph);
    assertEquals(2, allocatedVertices.size());
    assertTrue(allocatedVertices.contains(v3));
    assertTrue(allocatedVertices.contains(v4));
    BigDecimal totalWeight = BigDecimal.ZERO;
    for (Vertex vertex : allocatedVertices) {
      totalWeight = totalWeight.add(vertex.getWeight());
    }
    assertEquals(0, totalWeight.compareTo(BigDecimal.valueOf(15.0)));
  }

  @Test
  void testGetHyperEdgesFromIndependentSet() {

    Set<Vertex> allocatedVertices = strategy.allocate(hyperGraph);
    Set<HyperEdge> allocatedHyperEdges = strategy.getHyperEdges(hyperGraph, allocatedVertices);
    assertEquals(2, allocatedHyperEdges.size());
    assertTrue(allocatedHyperEdges.contains(p3));
    assertTrue(allocatedHyperEdges.contains(p4));
    BigDecimal totalWeight = BigDecimal.ZERO;
    for (HyperEdge hyperEdge : allocatedHyperEdges) {
      totalWeight = totalWeight.add(hyperEdge.getWeight());
    }
    assertEquals(0, totalWeight.compareTo(BigDecimal.valueOf(15.0)));
  }
}
