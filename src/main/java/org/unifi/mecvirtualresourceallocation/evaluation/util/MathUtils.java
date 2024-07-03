package org.unifi.mecvirtualresourceallocation.evaluation.util;

/** Utility class for mathematical operations. */
public final class MathUtils {

  /** Private constructor to prevent instantiation of this utility class. */
  private MathUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
  }

  /**
   * Calculates the sum of binomial coefficients from 1 to k for a given n.
   *
   * @param n the number of elements
   * @param k the upper limit for binomial coefficients
   * @return the sum of binomial coefficients from 1 to k for a given n
   */
  public static int sumOfBinomials(int n, int k) {
    int sum = 0;
    for (int i = 0; i <= k; i++) {
      sum += binomialCoefficient(n, i);
    }
    return sum;
  }

  /**
   * Calculates the binomial coefficient "n choose k".
   *
   * @param n the number of elements
   * @param k the number of selections
   * @return the binomial coefficient "n choose k"
   */
  public static int binomialCoefficient(int n, int k) {
    if (k > n - k) {
      k = n - k;
    }
    int result = 1;
    for (int i = 1; i <= k; i++) {
      result *= (n - i + 1);
      result /= i;
    }
    return result;
  }
}
