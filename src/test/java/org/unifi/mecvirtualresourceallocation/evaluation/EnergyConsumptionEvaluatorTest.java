package org.unifi.mecvirtualresourceallocation.evaluation;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unifi.mecvirtualresourceallocation.algorithm.AllocationStrategy;
import org.unifi.mecvirtualresourceallocation.evaluation.util.HyperGraphGenerator;
import org.unifi.mecvirtualresourceallocation.graph.HyperGraph;

public class EnergyConsumptionEvaluatorTest {

  private EnergyConsumptionReductionEvaluator reductionEvaluator;
  private EnergyConsumptionComparisonEvaluator comparisonEvaluator;

  @BeforeEach
  public void setUp() {
    reductionEvaluator = new EnergyConsumptionReductionEvaluator();
    comparisonEvaluator = new EnergyConsumptionComparisonEvaluator();
  }

  @Test
  public void testReductionEvaluatorExecute() {
    reductionEvaluator.execute(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 5, 3);
  }

  @Test
  public void testComparisonEvaluatorExecute() {
    comparisonEvaluator.execute(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 5, 3);
  }

  @Test
  public void testEvaluateEnergyConsumptionWithUnsupportedStrategy() {
    HyperGraph hyperGraph = HyperGraphGenerator.generateRandomHyperGraph(10, 3, new Random(42));
    AllocationStrategy unsupportedStrategy = hyperGraph1 -> Collections.emptySet();

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          Method method =
              EnergyConsumptionEvaluator.class.getDeclaredMethod(
                  "calculateWeight", HyperGraph.class, AllocationStrategy.class, int.class);
          method.setAccessible(true);
          try {
            method.invoke(reductionEvaluator, hyperGraph, unsupportedStrategy, 3);
          } catch (InvocationTargetException e) {
            throw e.getCause();
          }
        });
  }

  @Test
  public void testReductionEvaluatorPlotResults() {
    Map<Integer, BigDecimal> avgWeightsSequential = new TreeMap<>();
    Map<Integer, BigDecimal> avgWeightsLocal = new TreeMap<>();
    for (int i = 1; i <= 10; i++) {
      avgWeightsSequential.put(
          i, BigDecimal.valueOf(Math.random() * 1000).setScale(2, RoundingMode.HALF_UP));
      avgWeightsLocal.put(
          i, BigDecimal.valueOf(Math.random() * 1000).setScale(2, RoundingMode.HALF_UP));
    }
    try {
      reductionEvaluator.plotResults(avgWeightsSequential, avgWeightsLocal);
    } catch (Exception e) {
      fail("Exception should not be thrown: " + e.getMessage());
    }
  }

  @Test
  public void testReductionEvaluatorPlotResultsDifferentSize() {
    Map<Integer, BigDecimal> avgWeightsSequential = new TreeMap<>();
    Map<Integer, BigDecimal> avgWeightsLocal = new TreeMap<>();
    for (int i = 1; i <= 10; i++) {
      avgWeightsSequential.put(
          i, BigDecimal.valueOf(Math.random() * 1000).setScale(2, RoundingMode.HALF_UP));
      avgWeightsLocal.put(
          i, BigDecimal.valueOf(Math.random() * 1000).setScale(2, RoundingMode.HALF_UP));
    }
    avgWeightsSequential.put(
        11, BigDecimal.valueOf(Math.random() * 1000).setScale(2, RoundingMode.HALF_UP));
    assertThrows(
        IllegalArgumentException.class,
        () -> reductionEvaluator.plotResults(avgWeightsSequential, avgWeightsLocal),
        "Expected plotResults to throw, but it didn't");
  }

  @Test
  public void testReductionEvaluatorPlotResultsDifferentKeys() {
    Map<Integer, BigDecimal> avgWeightsSequential = new TreeMap<>();
    Map<Integer, BigDecimal> avgWeightsLocal = new TreeMap<>();
    for (int i = 1; i <= 10; i++) {
      avgWeightsSequential.put(
          i, BigDecimal.valueOf(Math.random() * 1000).setScale(2, RoundingMode.HALF_UP));
      avgWeightsLocal.put(
          i + 1, BigDecimal.valueOf(Math.random() * 1000).setScale(2, RoundingMode.HALF_UP));
    }
    assertThrows(
        IllegalArgumentException.class,
        () -> reductionEvaluator.plotResults(avgWeightsSequential, avgWeightsLocal),
        "Expected plotResults to throw, but it didn't");
  }

  @Test
  public void testComparisonEvaluatorPlotResults() {
    Map<Integer, BigDecimal> avgWeightsSequential = new TreeMap<>();
    Map<Integer, BigDecimal> avgWeightsLocal = new TreeMap<>();
    for (int i = 1; i <= 10; i++) {
      avgWeightsSequential.put(
          i, BigDecimal.valueOf(Math.random() * 1000).setScale(2, RoundingMode.HALF_UP));
      avgWeightsLocal.put(
          i, BigDecimal.valueOf(Math.random() * 1000).setScale(2, RoundingMode.HALF_UP));
    }
    try {
      comparisonEvaluator.plotResults(avgWeightsSequential, avgWeightsLocal);
    } catch (Exception e) {
      fail("Exception should not be thrown: " + e.getMessage());
    }
  }
}
