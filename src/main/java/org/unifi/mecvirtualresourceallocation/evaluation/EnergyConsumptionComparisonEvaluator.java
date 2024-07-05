package org.unifi.mecvirtualresourceallocation.evaluation;

import java.math.BigDecimal;
import java.util.Map;
import org.unifi.mecvirtualresourceallocation.evaluation.util.ChartUtils;

/** Evaluator for comparing energy consumption between different allocation strategies. */
public class EnergyConsumptionComparisonEvaluator extends EnergyConsumptionEvaluator {

  /**
   * Plots the results of the energy consumption comparison.
   *
   * @param avgWeightsSequential the average weights for the SequentialSearchStrategy
   * @param avgWeightsLocal the average weights for the LocalSearchStrategy
   */
  @Override
  protected void plotResults(
      Map<Integer, BigDecimal> avgWeightsSequential, Map<Integer, BigDecimal> avgWeightsLocal) {
    avgWeightsSequential.put(0, BigDecimal.ZERO);
    avgWeightsLocal.put(0, BigDecimal.ZERO);
    ChartUtils.createAndShowChart(
        "Sequential vs Local Search",
        "Number of VMs",
        "Average Energy Consumption",
        avgWeightsSequential,
        "Sequential Search Algorithm",
        avgWeightsLocal,
        "Local Search Algorithm");
  }
}
