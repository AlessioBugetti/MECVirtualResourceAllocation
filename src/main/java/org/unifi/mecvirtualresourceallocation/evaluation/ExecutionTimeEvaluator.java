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

public class ExecutionTimeEvaluator extends AbstractEvaluator {

  @Override
  public void execute() {
    evaluateExecutionTime();
  }

  private void evaluateExecutionTime() {
    int[] vertexSizes = generateVertexSizes();
    Map<Integer, Long> avgExecutionTimeSequential = new TreeMap<>();
    Map<Integer, Long> avgExecutionTimeLocal = new TreeMap<>();
    Random rand = new Random(SEED);

    for (int size : vertexSizes) {
      long totalExecutionTimeSequential = 0;
      long totalExecutionTimeLocal = 0;

      for (int i = 0; i < NUM_EXECUTIONS; i++) {
        HyperGraph hyperGraph = HyperGraphGenerator.generateRandomHyperGraph(size, rand);
        totalExecutionTimeSequential +=
            measureExecutionTime(hyperGraph, new SequentialSearchStrategy());
        totalExecutionTimeLocal += measureExecutionTime(hyperGraph, new LocalSearchStrategy());
        System.out.println(size + " " + i);
      }

      avgExecutionTimeSequential.put(size, totalExecutionTimeSequential / NUM_EXECUTIONS);
      avgExecutionTimeLocal.put(size, totalExecutionTimeLocal / NUM_EXECUTIONS);
    }

    plotResults(avgExecutionTimeSequential, avgExecutionTimeLocal);
  }

  private long measureExecutionTime(HyperGraph hyperGraph, AllocationStrategy strategy) {
    long startTime = System.nanoTime();
    strategy.allocate(hyperGraph);
    return System.nanoTime() - startTime;
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
