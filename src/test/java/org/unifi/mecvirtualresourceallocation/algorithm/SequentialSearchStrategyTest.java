package org.unifi.mecvirtualresourceallocation.algorithm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unifi.mecvirtualresourceallocation.graph.HyperEdge;
import org.unifi.mecvirtualresourceallocation.graph.HyperGraph;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

public class SequentialSearchStrategyTest {

  private AllocationStrategy strategy;
  private HyperGraph hyperGraph;
  private Vertex v3;
  private Vertex v6;
  private HyperEdge p3;
  private HyperEdge p6;

  @BeforeEach
  void setUp() {
    Vertex v1 = new Vertex("1", 1.0);
    Vertex v2 = new Vertex("2", 2.0);
    v3 = new Vertex("3", 3.0);
    Vertex v4 = new Vertex("4", 4.0);
    Vertex v5 = new Vertex("5", 5.0);
    v6 = new Vertex("6", 6.0);
    Set<Vertex> vertices = new HashSet<>(Arrays.asList(v1, v2, v3, v4, v5, v6));

    HyperEdge p1 = new HyperEdge("1", new HashSet<>(Arrays.asList(v1, v2, v3)));
    HyperEdge p2 = new HyperEdge("2", new HashSet<>(Arrays.asList(v2, v4)));
    p3 = new HyperEdge("3", new HashSet<>(Arrays.asList(v3, v6)));
    HyperEdge p4 = new HyperEdge("4", new HashSet<>(Arrays.asList(v1, v5)));
    HyperEdge p5 = new HyperEdge("5", new HashSet<>(Arrays.asList(v3, v5, v6)));
    p6 = new HyperEdge("6", new HashSet<>(Arrays.asList(v1, v4)));
    Set<HyperEdge> edges = new HashSet<>(Arrays.asList(p1, p2, p3, p4, p5, p6));

    hyperGraph = new HyperGraph(vertices, edges);
    strategy = new SequentialSearchStrategy();
  }

  @Test
  void testAllocate() {

    Set<Vertex> allocatedVertices = strategy.allocate(hyperGraph);
    assertEquals(2, allocatedVertices.size());
    assertTrue(allocatedVertices.contains(v3));
    assertTrue(allocatedVertices.contains(v6));
    BigDecimal totalWeight = BigDecimal.ZERO;
    for (Vertex vertex : allocatedVertices) {
      totalWeight = totalWeight.add(vertex.getWeight());
    }
    assertEquals(0, totalWeight.compareTo(BigDecimal.valueOf(14.0)));
  }

  @Test
  void testGetHyperEdgesFromIndependentSet() {

    Set<Vertex> allocatedVertices = strategy.allocate(hyperGraph);
    Set<HyperEdge> allocatedHyperEdges = strategy.getHyperEdges(hyperGraph, allocatedVertices);
    assertEquals(2, allocatedHyperEdges.size());
    assertTrue(allocatedHyperEdges.contains(p3));
    assertTrue(allocatedHyperEdges.contains(p6));
    BigDecimal totalWeight = BigDecimal.ZERO;
    for (HyperEdge hyperEdge : allocatedHyperEdges) {
      totalWeight = totalWeight.add(hyperEdge.getWeight());
    }
    assertEquals(0, totalWeight.compareTo(BigDecimal.valueOf(14.0)));
  }

  @Test
  void testFindMaxWeightVertex() throws Exception {
    Method method =
        SequentialSearchStrategy.class.getDeclaredMethod("findMaxWeightVertex", Set.class);
    Set<Vertex> vertices = new HashSet<>();
    method.setAccessible(true);
    InvocationTargetException thrown =
        assertThrows(InvocationTargetException.class, () -> method.invoke(strategy, vertices));
    assertEquals(IllegalArgumentException.class, thrown.getCause().getClass());
  }
}
