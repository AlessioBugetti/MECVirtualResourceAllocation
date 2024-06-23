package org.unifi.mecvirtualresourceallocation.evaluation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.unifi.mecvirtualresourceallocation.evaluation.util.ChartUtils;

public class EnergyConsumptionReductionEvaluation extends EnergyConsumptionEvaluation {

  @Override
  public void plotResults(
      Map<Integer, BigDecimal> avgReducedWeightsSequential,
      Map<Integer, BigDecimal> avgReducedWeightsLocal) {
    Map<Integer, BigDecimal> avgReducedWeights = new HashMap<>();
    for (Integer key : avgReducedWeightsLocal.keySet()) {
      if (avgReducedWeightsSequential.containsKey(key)) {
        BigDecimal difference =
            avgReducedWeightsSequential.get(key).subtract(avgReducedWeightsLocal.get(key));
        avgReducedWeights.put(key, difference);
      }
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
