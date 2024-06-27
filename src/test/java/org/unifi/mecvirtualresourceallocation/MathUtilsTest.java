package org.unifi.mecvirtualresourceallocation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.unifi.mecvirtualresourceallocation.evaluation.util.MathUtils;

public class MathUtilsTest {

  @Test
  public void testBinomialCoefficient() {
    assertEquals(1, MathUtils.binomialCoefficient(0, 0));
    assertEquals(1, MathUtils.binomialCoefficient(5, 0));
    assertEquals(1, MathUtils.binomialCoefficient(5, 5));
    assertEquals(5, MathUtils.binomialCoefficient(5, 1));
    assertEquals(10, MathUtils.binomialCoefficient(5, 2));
    assertEquals(10, MathUtils.binomialCoefficient(5, 3));
    assertEquals(5, MathUtils.binomialCoefficient(5, 4));
    assertEquals(252, MathUtils.binomialCoefficient(10, 5));
  }

  @Test
  public void testSumOfBinomials() {
    assertEquals(31, MathUtils.sumOfBinomials(5, 4));
    assertEquals(1, MathUtils.sumOfBinomials(0, 0));
    assertEquals(1, MathUtils.sumOfBinomials(5, 0));
    assertEquals(6, MathUtils.sumOfBinomials(5, 1));
    assertEquals(11, MathUtils.sumOfBinomials(4, 2));
    assertEquals(32, MathUtils.sumOfBinomials(5, 5));
    assertEquals(63, MathUtils.sumOfBinomials(6, 5));
  }
}
