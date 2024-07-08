package org.unifi.mecvirtualresourceallocation.evaluation;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;
import org.unifi.mecvirtualresourceallocation.evaluation.util.ChartUtils;

/** Evaluator for measuring the reduction in energy consumption. */
public class EnergyConsumptionReductionEvaluator extends EnergyConsumptionEvaluator {

  /**
   * Plots the results of the energy consumption reduction evaluation.
   *
   * @param avgWeightsSequential the average reduced weights for the SequentialSearchStrategy
   * @param avgWeightsLocal the average reduced weights for the LocalSearchStrategy
   */
  @Override
  protected void plotResults(
      Map<Integer, BigDecimal> avgWeightsSequential, Map<Integer, BigDecimal> avgWeightsLocal) {

    if (avgWeightsSequential.size() != avgWeightsLocal.size()) {
      throw new IllegalArgumentException(
          "The output of Sequential Search and that of Local Search have a different number of"
              + " elements.");
    }

    if (!avgWeightsSequential.keySet().equals(avgWeightsLocal.keySet())) {
      throw new IllegalArgumentException(
          "The output of Sequential Search and that of Local Search contain different keys.");
    }

    Map<Integer, BigDecimal> avgReducedWeights = new TreeMap<>();
    for (Integer key : avgWeightsSequential.keySet()) {
      BigDecimal difference = avgWeightsSequential.get(key).subtract(avgWeightsLocal.get(key));
      avgReducedWeights.put(key, difference);
    }
    avgReducedWeights.put(0, BigDecimal.ZERO);
    ChartUtils.createAndShowChart(
        "Average Energy Consumption Reduction",
        "Number of VMs",
        "Average Energy Consumption Reduction",
        avgReducedWeights,
        "Energy Consumption Reduction");
  }
}
