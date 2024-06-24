package org.unifi.mecvirtualresourceallocation.evaluation;

import java.math.BigDecimal;
import java.util.Map;
import org.unifi.mecvirtualresourceallocation.evaluation.util.ChartUtils;

public class EnergyConsumptionComparisonEvaluator extends EnergyConsumptionEvaluator {

  @Override
  protected void plotResults(
      Map<Integer, BigDecimal> avgReducedWeightsSequential,
      Map<Integer, BigDecimal> avgReducedWeightsLocal) {
    avgReducedWeightsSequential.put(0, BigDecimal.ZERO);
    avgReducedWeightsLocal.put(0, BigDecimal.ZERO);
    ChartUtils.createAndShowChart(
        "Sequential vs Local Search",
        "Number of VMs",
        "Average Energy Consumption",
        avgReducedWeightsSequential,
        "Sequential Search Algorithm",
        avgReducedWeightsLocal,
        "Local Search Algorithm");
  }
}
