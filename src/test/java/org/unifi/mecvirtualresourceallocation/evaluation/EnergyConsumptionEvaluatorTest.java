package org.unifi.mecvirtualresourceallocation.evaluation;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    reductionEvaluator.execute(10, 5);
  }

  @Test
  public void testComparisonEvaluatorExecute() {
    comparisonEvaluator.execute(10, 5);
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
