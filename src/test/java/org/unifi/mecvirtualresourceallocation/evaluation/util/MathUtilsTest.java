package org.unifi.mecvirtualresourceallocation.evaluation.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

public class MathUtilsTest {

  @Test
  public void testPrivateConstructor() throws Exception {
    Constructor<MathUtils> constructor = MathUtils.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException thrown =
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    assertEquals(UnsupportedOperationException.class, thrown.getCause().getClass());
  }

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
