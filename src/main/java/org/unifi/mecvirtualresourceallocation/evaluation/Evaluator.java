package org.unifi.mecvirtualresourceallocation.evaluation;

/** Interface for evaluating performance metrics. */
public interface Evaluator {
  /** The seed value used for random number generation. */
  long SEED = 42;

  /**
   * Executes the evaluation.
   *
   * @param maxVertexSize the maximum size of vertices in the hypergraph
   * @param numExecutions the number of times the evaluation is executed
   * @param delta the δ value used for generating hypergraphs
   */
  void execute(int maxVertexSize, int numExecutions, int delta);

  /**
   * Executes the evaluation with a default δ value of 3.
   *
   * @param maxVertexSize the maximum size of vertices in the hypergraph
   * @param numExecutions the number of times the evaluation is executed
   */
  default void execute(int maxVertexSize, int numExecutions) {
    execute(maxVertexSize, numExecutions, 3);
  }

  /**
   * Generates an array from 1 to maxVertexSize.
   *
   * @param maxVertexSize the maximum size of vertices in the hypergraph
   * @return an array from 1 to maxVertexSize
   */
  default int[] generateVertexSizes(int maxVertexSize) {
    int[] vertexSizes = new int[maxVertexSize];
    for (int i = 0; i < maxVertexSize; i++) {
      vertexSizes[i] = i + 1;
    }
    return vertexSizes;
  }
}
