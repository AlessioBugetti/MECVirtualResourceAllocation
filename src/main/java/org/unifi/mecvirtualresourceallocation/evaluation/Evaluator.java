package org.unifi.mecvirtualresourceallocation.evaluation;

public interface Evaluator {
  int MAX_VERTEX_SIZE = 15;
  int NUM_EXECUTIONS = 30;
  long SEED = 42;

  void execute();

  default int[] generateVertexSizes() {
    int[] vertexSizes = new int[MAX_VERTEX_SIZE];
    for (int i = 0; i < MAX_VERTEX_SIZE; i++) {
      vertexSizes[i] = i + 1;
    }
    return vertexSizes;
  }
}
