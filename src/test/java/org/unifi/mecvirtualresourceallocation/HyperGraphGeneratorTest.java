package org.unifi.mecvirtualresourceallocation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unifi.mecvirtualresourceallocation.evaluation.Evaluator;
import org.unifi.mecvirtualresourceallocation.evaluation.util.HyperGraphGenerator;
import org.unifi.mecvirtualresourceallocation.evaluation.util.MathUtils;
import org.unifi.mecvirtualresourceallocation.graph.HyperEdge;
import org.unifi.mecvirtualresourceallocation.graph.HyperGraph;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

public class HyperGraphGeneratorTest {

  private Random rand;
  private HyperGraph hyperGraph;

  @BeforeEach
  public void setUp() {
    rand = new Random(Evaluator.SEED);
  }

  @Test
  public void testGenerateRandomHyperGraph() {
    hyperGraph = HyperGraphGenerator.generateRandomHyperGraph(5, rand);

    List<Vertex> vertices = hyperGraph.getVertices();
    List<HyperEdge> edges = hyperGraph.getHyperEdges();

    assertEquals(5, vertices.size());

    Set<Vertex> connectedVertices =
        edges.stream().flatMap(edge -> edge.getVertices().stream()).collect(Collectors.toSet());

    assertTrue(connectedVertices.containsAll(vertices));
    assertTrue(vertices.containsAll(connectedVertices));

    assertFalse(edges.isEmpty());

    Set<Set<Vertex>> uniqueEdgeSets =
        edges.stream().map(HyperEdge::getVertices).map(HashSet::new).collect(Collectors.toSet());

    assertEquals(uniqueEdgeSets.size(), edges.size());

    for (HyperEdge edge : edges) {
      assertTrue(edge.getVertices().size() <= HyperGraphGenerator.DELTA);
    }

    assertTrue(
        edges.size() <= MathUtils.sumOfBinomials(5, HyperGraphGenerator.DELTA) - 1
            && !edges.isEmpty());
  }

  @Test
  public void testGenerateRandomHyperGraphWithMinimalVertices() {
    hyperGraph = HyperGraphGenerator.generateRandomHyperGraph(1, rand);

    List<Vertex> vertices = hyperGraph.getVertices();
    List<HyperEdge> edges = hyperGraph.getHyperEdges();

    assertEquals(1, vertices.size());

    Set<Vertex> connectedVertices =
        edges.stream().flatMap(edge -> edge.getVertices().stream()).collect(Collectors.toSet());

    assertTrue(connectedVertices.containsAll(vertices));
    assertTrue(vertices.containsAll(connectedVertices));

    for (HyperEdge edge : edges) {
      assertTrue(edge.getVertices().size() <= HyperGraphGenerator.DELTA);
    }

    assertTrue(
        edges.size() <= MathUtils.sumOfBinomials(5, HyperGraphGenerator.DELTA) - 1
            && !edges.isEmpty());
  }

  @Test
  public void testGenerateRandomHyperGraphWithMediumVertices() {
    hyperGraph = HyperGraphGenerator.generateRandomHyperGraph(50, rand);

    List<Vertex> vertices = hyperGraph.getVertices();
    List<HyperEdge> edges = hyperGraph.getHyperEdges();

    assertEquals(50, vertices.size());

    Set<Vertex> connectedVertices =
        edges.stream().flatMap(edge -> edge.getVertices().stream()).collect(Collectors.toSet());

    assertTrue(connectedVertices.containsAll(vertices));
    assertTrue(vertices.containsAll(connectedVertices));

    for (HyperEdge edge : edges) {
      assertTrue(edge.getVertices().size() <= HyperGraphGenerator.DELTA);
    }

    assertTrue(
        edges.size() <= MathUtils.sumOfBinomials(50, HyperGraphGenerator.DELTA) - 1
            && !edges.isEmpty());
  }

  @Test
  public void testGenerateRandomHyperGraphWithMaxVertices() {
    hyperGraph = HyperGraphGenerator.generateRandomHyperGraph(150, rand);

    List<Vertex> vertices = hyperGraph.getVertices();
    List<HyperEdge> edges = hyperGraph.getHyperEdges();

    assertEquals(150, vertices.size());

    Set<Vertex> connectedVertices =
        edges.stream().flatMap(edge -> edge.getVertices().stream()).collect(Collectors.toSet());

    assertTrue(connectedVertices.containsAll(vertices));
    assertTrue(vertices.containsAll(connectedVertices));

    for (HyperEdge edge : edges) {
      assertTrue(edge.getVertices().size() <= HyperGraphGenerator.DELTA);
    }

    assertTrue(
        edges.size() <= MathUtils.sumOfBinomials(150, HyperGraphGenerator.DELTA) - 1
            && !edges.isEmpty());
  }
}
