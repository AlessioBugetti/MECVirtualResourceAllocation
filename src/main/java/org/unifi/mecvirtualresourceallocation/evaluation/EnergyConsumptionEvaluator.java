package org.unifi.mecvirtualresourceallocation.evaluation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import org.unifi.mecvirtualresourceallocation.algorithm.AllocationStrategy;
import org.unifi.mecvirtualresourceallocation.algorithm.LocalSearchStrategy;
import org.unifi.mecvirtualresourceallocation.algorithm.SequentialSearchStrategy;
import org.unifi.mecvirtualresourceallocation.evaluation.util.HyperGraphGenerator;
import org.unifi.mecvirtualresourceallocation.graph.HyperGraph;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

/** Abstract evaluator for measuring energy consumption in resource allocation. */
public abstract class EnergyConsumptionEvaluator implements Evaluator {

  /**
   * Executes the evaluation of energy consumption.
   *
   * @param maxVertexSize the maximum size of vertices in the hypergraph
   * @param numExecutions the number of times the evaluation is executed
   */
  @Override
  public void execute(int maxVertexSize, int numExecutions) {
    evaluateEnergyConsumption(maxVertexSize, numExecutions);
  }

  /**
   * Evaluates the energy consumption of the allocation strategies.
   *
   * @param maxVertexSize the maximum size of vertices in the hypergraph
   * @param numExecutions the number of times the evaluation is executed
   */
  private void evaluateEnergyConsumption(int maxVertexSize, int numExecutions) {
    int[] vertexSizes = generateVertexSizes(maxVertexSize);
    Map<Integer, BigDecimal> avgReducedWeightsSequential = new TreeMap<>();
    Map<Integer, BigDecimal> avgReducedWeightsLocal = new TreeMap<>();
    Random rand = new Random(SEED);

    for (int size : vertexSizes) {
      BigDecimal totalReducedWeightSequential = BigDecimal.ZERO;
      BigDecimal totalReducedWeightLocal = BigDecimal.ZERO;

      for (int i = 0; i < numExecutions; i++) {
        HyperGraph hyperGraph = HyperGraphGenerator.generateRandomHyperGraph(size, rand);
        totalReducedWeightSequential =
            totalReducedWeightSequential.add(
                calculateWeight(hyperGraph, new SequentialSearchStrategy()));
        totalReducedWeightLocal =
            totalReducedWeightLocal.add(calculateWeight(hyperGraph, new LocalSearchStrategy()));
      }

      avgReducedWeightsSequential.put(
          size,
          totalReducedWeightSequential.divide(
              BigDecimal.valueOf(numExecutions), RoundingMode.HALF_UP));
      avgReducedWeightsLocal.put(
          size,
          totalReducedWeightLocal.divide(BigDecimal.valueOf(numExecutions), RoundingMode.HALF_UP));
    }

    plotResults(avgReducedWeightsSequential, avgReducedWeightsLocal);
  }

  /**
   * Calculates the total weight reduction for the given allocation strategy.
   *
   * @param hyperGraph the hypergraph to allocate resources for
   * @param strategy the allocation strategy to be measured
   * @return the total weight reduction
   */
  private BigDecimal calculateWeight(HyperGraph hyperGraph, AllocationStrategy strategy) {
    Set<Vertex> initialIndependentSet = strategy.allocate(hyperGraph);
    return initialIndependentSet.stream()
        .map(Vertex::getNegativeWeight)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .negate();
  }

  /**
   * Plots the results of the energy consumption evaluation.
   *
   * @param avgWeightsSequential the average reduced weights for the SequentialSearchStrategy
   * @param avgWeightsLocal the average reduced weights for the LocalSearchStrategy
   */
  protected abstract void plotResults(
      Map<Integer, BigDecimal> avgWeightsSequential, Map<Integer, BigDecimal> avgWeightsLocal);
}
