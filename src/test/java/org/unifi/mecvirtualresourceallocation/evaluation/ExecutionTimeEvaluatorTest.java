package org.unifi.mecvirtualresourceallocation.evaluation;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unifi.mecvirtualresourceallocation.algorithm.AllocationStrategy;
import org.unifi.mecvirtualresourceallocation.algorithm.LocalSearchStrategy;
import org.unifi.mecvirtualresourceallocation.algorithm.SequentialSearchStrategy;
import org.unifi.mecvirtualresourceallocation.evaluation.util.HyperGraphGenerator;
import org.unifi.mecvirtualresourceallocation.graph.HyperGraph;

public class ExecutionTimeEvaluatorTest {

  private ExecutionTimeEvaluator evaluator;

  @BeforeEach
  public void setUp() {
    evaluator = new ExecutionTimeEvaluator();
  }

  @Test
  public void testExecute() {
    evaluator.execute(10, 5);
  }

  @Test
  public void testEvaluateExecutionTime() {
    try {
      java.lang.reflect.Method method =
          ExecutionTimeEvaluator.class.getDeclaredMethod(
              "evaluateExecutionTime", int.class, int.class);
      method.setAccessible(true);
      method.invoke(evaluator, 10, 5);
    } catch (Exception e) {
      fail("Exception should not be thrown: " + e.getMessage());
    }
  }

  @Test
  public void testMeasureExecutionTime() {
    HyperGraph hyperGraph =
        HyperGraphGenerator.generateRandomHyperGraph(10, new java.util.Random(42));
    AllocationStrategy sequentialStrategy = new SequentialSearchStrategy();
    AllocationStrategy localStrategy = new LocalSearchStrategy();

    long sequentialTime = measureExecutionTime(hyperGraph, sequentialStrategy);
    long localTime = measureExecutionTime(hyperGraph, localStrategy);

    assertTrue(sequentialTime > 0, "Sequential strategy execution time should be greater than 0");
    assertTrue(localTime > 0, "Local strategy execution time should be greater than 0");
  }

  @Test
  public void testPlotResults() {
    Map<Integer, Long> avgExecutionTimeSequential = new TreeMap<>();
    Map<Integer, Long> avgExecutionTimeLocal = new TreeMap<>();

    for (int i = 1; i <= 10; i++) {
      avgExecutionTimeSequential.put(i, (long) (Math.random() * 1000));
      avgExecutionTimeLocal.put(i, (long) (Math.random() * 1000));
    }

    try {
      java.lang.reflect.Method method =
          ExecutionTimeEvaluator.class.getDeclaredMethod("plotResults", Map.class, Map.class);
      method.setAccessible(true);
      method.invoke(evaluator, avgExecutionTimeSequential, avgExecutionTimeLocal);
      assertTrue(avgExecutionTimeSequential.containsKey(0), "Sequential map should contain key 0");
      assertTrue(avgExecutionTimeLocal.containsKey(0), "Local map should contain key 0");
    } catch (Exception e) {
      fail("Exception should not be thrown: " + e.getMessage());
    }
  }

  private long measureExecutionTime(HyperGraph hyperGraph, AllocationStrategy strategy) {
    long startTime = System.nanoTime();
    strategy.allocate(hyperGraph);
    long endTime = System.nanoTime();
    long executionTime = endTime - startTime;
    assertTrue(executionTime > 0, "Execution time should be greater than 0");
    return executionTime;
  }
}
