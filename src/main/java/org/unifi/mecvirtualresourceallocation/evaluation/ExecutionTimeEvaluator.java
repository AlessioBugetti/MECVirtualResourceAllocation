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

/** Evaluator for measuring the execution time of different allocation strategies. */
public class ExecutionTimeEvaluator implements Evaluator {

  /**
   * Executes the evaluation of execution time.
   *
   * @param maxVertexSize the maximum size of vertices in the hypergraph
   * @param numExecutions the number of times the evaluation is executed
   * @param delta the δ value used for generating hypergraphs
   */
  @Override
  public void execute(int maxVertexSize, int numExecutions, int delta) {
    evaluateExecutionTime(maxVertexSize, numExecutions, delta);
  }

  /**
   * Evaluates the execution time of the allocation strategies.
   *
   * @param maxVertexSize the maximum size of vertices in the hypergraph
   * @param numExecutions the number of times the evaluation is executed
   * @param delta the δ value used for generating hypergraphs
   */
  private void evaluateExecutionTime(int maxVertexSize, int numExecutions, int delta) {
    int[] vertexSizes = generateVertexSizes(maxVertexSize);
    Map<Integer, Long> avgExecutionTimeSequential = new TreeMap<>();
    Map<Integer, Long> avgExecutionTimeLocal = new TreeMap<>();
    Random rand = new Random(SEED);

    for (int size : vertexSizes) {
      long totalExecutionTimeSequential = 0;
      long totalExecutionTimeLocal = 0;

      for (int i = 0; i < numExecutions; i++) {
        HyperGraph hyperGraph = HyperGraphGenerator.generateRandomHyperGraph(size, delta, rand);
        totalExecutionTimeSequential +=
            measureExecutionTime(hyperGraph, new SequentialSearchStrategy());
        totalExecutionTimeLocal += measureExecutionTime(hyperGraph, new LocalSearchStrategy());
      }

      avgExecutionTimeSequential.put(size, totalExecutionTimeSequential / numExecutions);
      avgExecutionTimeLocal.put(size, totalExecutionTimeLocal / numExecutions);
    }

    plotResults(avgExecutionTimeSequential, avgExecutionTimeLocal);
  }

  /**
   * Measures the execution time of the given allocation strategy on the given hypergraph.
   *
   * @param hyperGraph the hypergraph to allocate resources for
   * @param strategy the allocation strategy to be measured
   * @return the execution time in nanoseconds
   */
  private long measureExecutionTime(HyperGraph hyperGraph, AllocationStrategy strategy) {
    long startTime = System.nanoTime();
    strategy.allocate(hyperGraph);
    return System.nanoTime() - startTime;
  }

  /**
   * Plots the results of the execution time evaluation.
   *
   * @param avgExecutionTimeSequential the average execution time for the SequentialSearchStrategy
   * @param avgExecutionTimeLocal the average execution time for the LocalSearchStrategy
   */
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
