package org.unifi.mecvirtualresourceallocation.evaluation;

import java.util.List;

/** Interface for evaluating performance metrics. */
public interface Evaluator {
  /** The seed value used for random number generation. */
  long SEED = 42;

  /**
   * Executes the evaluation.
   *
   * @param numVertices a list containing the number of vertices for each hypergraph to be evaluated
   * @param numExecutions the number of times the evaluation is executed
   * @param delta the delta (δ) value used for generating hypergraphs
   */
  void execute(List<Integer> numVertices, int numExecutions, int delta);

  /**
   * Executes the evaluation with a default delta (δ) value of 3.
   *
   * @param numVertices a list containing the number of vertices for each hypergraph to be evaluated
   * @param numExecutions the number of times the evaluation is executed
   */
  default void execute(List<Integer> numVertices, int numExecutions) {
    execute(numVertices, numExecutions, 3);
  }
}
