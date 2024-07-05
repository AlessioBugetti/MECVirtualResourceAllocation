package org.unifi.mecvirtualresourceallocation.evaluation;

/** Interface for evaluating performance metrics. */
public interface Evaluator {
  int MAX_VERTEX_SIZE = 75;
  int NUM_EXECUTIONS = 30;
  long SEED = 42;

  /** Executes the evaluation. */
  void execute();

  /**
   * Generates an array from 1 to MAX_VERTEX_SIZE.
   *
   * @return an array from 1 to MAX_VERTEX_SIZE
   */
  default int[] generateVertexSizes() {
    int[] vertexSizes = new int[MAX_VERTEX_SIZE];
    for (int i = 0; i < MAX_VERTEX_SIZE; i++) {
      vertexSizes[i] = i + 1;
    }
    return vertexSizes;
  }
}
