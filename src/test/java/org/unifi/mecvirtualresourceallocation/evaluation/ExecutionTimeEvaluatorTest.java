package org.unifi.mecvirtualresourceallocation.evaluation;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
      Method method =
          ExecutionTimeEvaluator.class.getDeclaredMethod(
              "evaluateExecutionTime", int.class, int.class, int.class);
      method.setAccessible(true);
      method.invoke(evaluator, 10, 5, 3);
    } catch (Exception e) {
      fail("Exception should not be thrown: " + e.getMessage());
    }
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
