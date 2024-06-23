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

public abstract class EnergyConsumptionEvaluation implements Evaluation {
  @Override
  public void run() {
    runEnergyConsumptionEvaluation();
  }

  public void runEnergyConsumptionEvaluation() {
    int[] vertexSizes = new int[50];
    for (int i = 0; i < vertexSizes.length; i++) {
      vertexSizes[i] = i + 1;
    }
    int numExecutions = 100;
    Map<Integer, BigDecimal> avgReducedWeightsSequential = new TreeMap<>();
    Map<Integer, BigDecimal> avgReducedWeightsLocal = new TreeMap<>();

    Random rand = new Random(42);
    for (int size : vertexSizes) {
      BigDecimal totalReducedWeightSequential = BigDecimal.ZERO;
      BigDecimal totalReducedWeightLocal = BigDecimal.ZERO;
      for (int i = 0; i < numExecutions; i++) {
        HyperGraph hyperGraph = HyperGraphGenerator.generateRandomHyperGraph(size, rand);
        totalReducedWeightSequential =
            totalReducedWeightSequential.add(
                getReducedWeightWithStrategy(hyperGraph, new SequentialSearchStrategy()));
        totalReducedWeightLocal =
            totalReducedWeightLocal.add(
                getReducedWeightWithStrategy(hyperGraph, new LocalSearchStrategy()));
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

  private BigDecimal getReducedWeightWithStrategy(
      HyperGraph hyperGraph, AllocationStrategy strategy) {
    Set<Vertex> initialIndependentSet = strategy.allocate(hyperGraph);

    BigDecimal initialWeight =
        initialIndependentSet.stream()
            .map(Vertex::getNegativeWeight)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .negate();

    return initialWeight;
  }

  public abstract void plotResults(
      Map<Integer, BigDecimal> avgReducedWeightsSequential,
      Map<Integer, BigDecimal> avgReducedWeightsLocal);
}
