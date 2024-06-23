package org.unifi.mecvirtualresourceallocation.evaluation;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import org.unifi.mecvirtualresourceallocation.algorithm.AllocationStrategy;
import org.unifi.mecvirtualresourceallocation.algorithm.LocalSearchStrategy;
import org.unifi.mecvirtualresourceallocation.algorithm.SequentialSearchStrategy;
import org.unifi.mecvirtualresourceallocation.evaluation.util.ChartUtils;
import org.unifi.mecvirtualresourceallocation.evaluation.util.HyperGraphGenerator;
import org.unifi.mecvirtualresourceallocation.graph.HyperGraph;

public class ExecutionTimeEvaluation implements Evaluation {
  @Override
  public void run() {
    runExecutionTimeEvaluation();
  }

  private void runExecutionTimeEvaluation() {
    int[] vertexSizes = new int[50];
    for (int i = 0; i < vertexSizes.length; i++) {
      vertexSizes[i] = i + 1;
    }
    int numExecutions = 100;
    Map<Integer, Long> avgExecutionTimeSequential = new TreeMap<>();
    Map<Integer, Long> avgExecutionTimeLocal = new TreeMap<>();

    Random rand = new Random(42);
    for (int size : vertexSizes) {
      long totalExecutionTimeSequential = 0;
      long totalExecutionTimeLocal = 0;
      for (int i = 0; i < numExecutions; i++) {
        HyperGraph hyperGraph = HyperGraphGenerator.generateRandomHyperGraph(size, rand);
        totalExecutionTimeSequential +=
            measureExecutionTime(hyperGraph, new SequentialSearchStrategy());
        totalExecutionTimeLocal += measureExecutionTime(hyperGraph, new LocalSearchStrategy());
      }
      avgExecutionTimeSequential.put(size, totalExecutionTimeSequential / numExecutions);
      avgExecutionTimeLocal.put(size, totalExecutionTimeLocal / numExecutions);
    }

    plotResults(avgExecutionTimeSequential, avgExecutionTimeLocal);
  }

  private long measureExecutionTime(HyperGraph hyperGraph, AllocationStrategy strategy) {
    long startTime = System.nanoTime();
    strategy.allocate(hyperGraph);
    long endTime = System.nanoTime();
    return endTime - startTime;
  }

  private void plotResults(
      Map<Integer, Long> avgExecutionTimeSequential, Map<Integer, Long> avgExecutionTimeLocal) {
    avgExecutionTimeLocal.put(0, 0L);
    avgExecutionTimeSequential.put(0, 0L);
    ChartUtils.createAndShowExecutionTimeChart(
        "Average Execution Time",
        "Number of VMs",
        "Execution Time (nanoseconds)",
        avgExecutionTimeSequential,
        "Sequential Search Algorithm",
        avgExecutionTimeLocal,
        "Local Search Algorithm");
  }
}
