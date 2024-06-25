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

public abstract class EnergyConsumptionEvaluator implements Evaluation {

  @Override
  public void execute() {
    evaluateEnergyConsumption();
  }

  private void evaluateEnergyConsumption() {
    int[] vertexSizes = generateVertexSizes();
    Map<Integer, BigDecimal> avgReducedWeightsSequential = new TreeMap<>();
    Map<Integer, BigDecimal> avgReducedWeightsLocal = new TreeMap<>();
    Random rand = new Random(SEED);

    for (int size : vertexSizes) {
      BigDecimal totalReducedWeightSequential = BigDecimal.ZERO;
      BigDecimal totalReducedWeightLocal = BigDecimal.ZERO;

      for (int i = 0; i < NUM_EXECUTIONS; i++) {
        HyperGraph hyperGraph = HyperGraphGenerator.generateRandomHyperGraph(size, rand);
        totalReducedWeightSequential =
            totalReducedWeightSequential.add(
                calculateReducedWeight(hyperGraph, new SequentialSearchStrategy()));
        totalReducedWeightLocal =
            totalReducedWeightLocal.add(
                calculateReducedWeight(hyperGraph, new LocalSearchStrategy()));
      }

      avgReducedWeightsSequential.put(
          size,
          totalReducedWeightSequential.divide(
              BigDecimal.valueOf(NUM_EXECUTIONS), RoundingMode.HALF_UP));
      avgReducedWeightsLocal.put(
          size,
          totalReducedWeightLocal.divide(BigDecimal.valueOf(NUM_EXECUTIONS), RoundingMode.HALF_UP));
    }

    plotResults(avgReducedWeightsSequential, avgReducedWeightsLocal);
  }

  private BigDecimal calculateReducedWeight(HyperGraph hyperGraph, AllocationStrategy strategy) {
    Set<Vertex> initialIndependentSet = strategy.allocate(hyperGraph);
    BigDecimal initialWeight =
        initialIndependentSet.stream()
            .map(Vertex::getNegativeWeight)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .negate();
    return initialWeight;
  }

  protected abstract void plotResults(
      Map<Integer, BigDecimal> avgReducedWeightsSequential,
      Map<Integer, BigDecimal> avgReducedWeightsLocal);
}
