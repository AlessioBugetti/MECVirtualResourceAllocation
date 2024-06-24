package org.unifi.mecvirtualresourceallocation.evaluation;

public abstract class AbstractEvaluator implements Evaluation {
  protected static final int MAX_VERTEX_SIZE = 50;
  protected static final int NUM_EXECUTIONS = 50;
  protected static final long SEED = 42;

  protected int[] generateVertexSizes() {
    int[] vertexSizes = new int[MAX_VERTEX_SIZE];
    for (int i = 0; i < MAX_VERTEX_SIZE; i++) {
      vertexSizes[i] = i + 1;
    }
    return vertexSizes;
  }
}
