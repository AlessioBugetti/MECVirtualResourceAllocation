package org.unifi.mecvirtualresourceallocation.evaluation;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unifi.mecvirtualresourceallocation.algorithm.AllocationStrategy;
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
    evaluator.execute(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 5);
  }

  @Test
  public void testEvaluateExecutionTime() {
    try {
      Method method =
          ExecutionTimeEvaluator.class.getDeclaredMethod(
              "evaluateExecutionTime", List.class, int.class, int.class);
      method.setAccessible(true);
      method.invoke(evaluator, List.of(10), 5, 3);
    } catch (Exception e) {
      fail("Exception should not be thrown: " + e.getMessage());
    }
  }

  @Test
  public void testMeasureExecutionTimeWithUnsupportedStrategy() {
    HyperGraph hyperGraph = HyperGraphGenerator.generateRandomHyperGraph(10, 3, new Random(42));
    AllocationStrategy unsupportedStrategy = hyperGraph1 -> Collections.emptySet();

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          Method method =
              ExecutionTimeEvaluator.class.getDeclaredMethod(
                  "measureExecutionTime", HyperGraph.class, AllocationStrategy.class, int.class);
          method.setAccessible(true);
          try {
            method.invoke(evaluator, hyperGraph, unsupportedStrategy, 3);
          } catch (InvocationTargetException e) {
            throw e.getCause();
          }
        });
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
      Method method =
          ExecutionTimeEvaluator.class.getDeclaredMethod("plotResults", Map.class, Map.class);
      method.setAccessible(true);
      method.invoke(evaluator, avgExecutionTimeSequential, avgExecutionTimeLocal);
      assertTrue(avgExecutionTimeSequential.containsKey(0), "Sequential map should contain key 0");
      assertTrue(avgExecutionTimeLocal.containsKey(0), "Local map should contain key 0");
    } catch (Exception e) {
      fail("Exception should not be thrown: " + e.getMessage());
    }
  }
}
