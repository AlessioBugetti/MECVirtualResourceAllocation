package org.unifi.mecvirtualresourceallocation.evaluation.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Dimension;
import java.awt.Frame;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.fest.swing.fixture.FrameFixture;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class ChartUtilsTest {

  @AfterEach
  public void cleanup() {
    File dir = new File(".");
    File[] files = dir.listFiles((d, name) -> name.startsWith("test"));
    if (files != null) {
      for (File file : files) {
        if (file.isFile() && !file.delete()) {
          System.err.println("Failed to delete file: " + file.getAbsolutePath());
        }
      }
    }
  }

  @Test
  public void testPrivateConstructor() throws Exception {
    Constructor<ChartUtils> constructor = ChartUtils.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException thrown =
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    assertEquals(UnsupportedOperationException.class, thrown.getCause().getClass());
  }

  @Test
  public void testCreateAndShowChartSingleSeries() {
    Map<Integer, BigDecimal> data = Map.of(1, BigDecimal.valueOf(1.0), 2, BigDecimal.valueOf(2.0));
    FrameFixture frameFixture = null;
    try {
      SwingUtilities.invokeAndWait(
          () ->
              ChartUtils.createAndShowChart(
                  "Test Chart Single Series", "X Axis", "Y Axis", data, "Single Series"));

      JFrame hyperGraphFrame = null;
      for (Frame frame : JFrame.getFrames()) {
        if (frame instanceof JFrame && frame.getTitle().equals("Test Chart Single Series")) {
          hyperGraphFrame = (JFrame) frame;
          break;
        }
      }

      assertNotNull(hyperGraphFrame, "Test Chart Single Series frame should be present");

      frameFixture = new FrameFixture(hyperGraphFrame);
      frameFixture.requireSize(new Dimension(800, 600));
      frameFixture.requireVisible();
      assertEquals("Test Chart Single Series", hyperGraphFrame.getTitle());

      ChartPanel chartPanel = (ChartPanel) hyperGraphFrame.getContentPane();
      JFreeChart chart = chartPanel.getChart();
      XYPlot plot = chart.getXYPlot();

      assertEquals("X Axis", plot.getDomainAxis().getLabel());
      assertEquals("Y Axis", plot.getRangeAxis().getLabel());

      XYSeriesCollection dataset = (XYSeriesCollection) plot.getDataset();
      assertEquals(1, dataset.getSeriesCount());

      XYSeries series = dataset.getSeries(0);
      assertEquals("Single Series", series.getKey());
      assertEquals(2, series.getItemCount());
      assertEquals(1, series.getX(0));
      assertEquals(BigDecimal.valueOf(1.0), series.getY(0));
      assertEquals(2, series.getX(1));
      assertEquals(BigDecimal.valueOf(2.0), series.getY(1));
    } catch (InvocationTargetException | InterruptedException e) {
      fail("Test failed due to exception: " + e.getMessage());
    } finally {
      if (frameFixture != null) {
        frameFixture.cleanUp();
      }
    }
  }

  @Test
  public void testCreateAndShowChartTwoSeries() {
    Map<Integer, BigDecimal> data1 = Map.of(1, BigDecimal.valueOf(1.0), 2, BigDecimal.valueOf(2.0));
    Map<Integer, BigDecimal> data2 = Map.of(1, BigDecimal.valueOf(1.5), 2, BigDecimal.valueOf(2.5));

    FrameFixture frameFixture = null;
    try {
      SwingUtilities.invokeAndWait(
          () ->
              ChartUtils.createAndShowChart(
                  "Test Chart Two Series",
                  "X Axis",
                  "Y Axis",
                  data1,
                  "Series 1",
                  data2,
                  "Series 2"));

      JFrame hyperGraphFrame = null;
      for (Frame frame : JFrame.getFrames()) {
        if (frame instanceof JFrame && frame.getTitle().equals("Test Chart Two Series")) {
          hyperGraphFrame = (JFrame) frame;
          break;
        }
      }

      assertNotNull(hyperGraphFrame, "Test Chart Two Series frame should be present");

      frameFixture = new FrameFixture(hyperGraphFrame);
      frameFixture.requireSize(new Dimension(800, 600));
      frameFixture.requireVisible();
      assertEquals("Test Chart Two Series", hyperGraphFrame.getTitle());

      ChartPanel chartPanel = (ChartPanel) hyperGraphFrame.getContentPane();
      JFreeChart chart = chartPanel.getChart();
      XYPlot plot = chart.getXYPlot();

      assertEquals("X Axis", plot.getDomainAxis().getLabel());
      assertEquals("Y Axis", plot.getRangeAxis().getLabel());

      XYSeriesCollection dataset = (XYSeriesCollection) plot.getDataset();
      assertEquals(2, dataset.getSeriesCount());

      XYSeries series1 = dataset.getSeries(0);
      assertEquals("Series 1", series1.getKey());
      assertEquals(2, series1.getItemCount());
      assertEquals(1, series1.getX(0));
      assertEquals(BigDecimal.valueOf(1.0), series1.getY(0));
      assertEquals(2, series1.getX(1));
      assertEquals(BigDecimal.valueOf(2.0), series1.getY(1));

      XYSeries series2 = dataset.getSeries(1);
      assertEquals("Series 2", series2.getKey());
      assertEquals(2, series2.getItemCount());
      assertEquals(1, series2.getX(0));
      assertEquals(BigDecimal.valueOf(1.5), series2.getY(0));
      assertEquals(2, series2.getX(1));
      assertEquals(BigDecimal.valueOf(2.5), series2.getY(1));
    } catch (InvocationTargetException | InterruptedException e) {
      fail("Test failed due to exception: " + e.getMessage());
    } finally {
      if (frameFixture != null) {
        frameFixture.cleanUp();
      }
    }
  }

  @Test
  public void testCreateAndShowExecutionTimeChart() {
    Map<Integer, Long> data1 = Map.of(1, 1000L, 2, 2000L);
    Map<Integer, Long> data2 = Map.of(1, 1500L, 2, 2500L);

    FrameFixture frameFixture = null;
    try {
      SwingUtilities.invokeAndWait(
          () ->
              ChartUtils.createAndShowExecutionTimeChart(
                  "Test Execution Time Chart",
                  "X Axis",
                  "Y Axis",
                  data1,
                  "Execution 1",
                  data2,
                  "Execution 2"));

      JFrame hyperGraphFrame = null;
      for (Frame frame : JFrame.getFrames()) {
        if (frame instanceof JFrame && frame.getTitle().equals("Test Execution Time Chart")) {
          hyperGraphFrame = (JFrame) frame;
          break;
        }
      }

      assertNotNull(hyperGraphFrame, "Test Execution Time Chart frame should be present");

      frameFixture = new FrameFixture(hyperGraphFrame);
      frameFixture.requireSize(new Dimension(800, 600));
      frameFixture.requireVisible();
      assertEquals("Test Execution Time Chart", hyperGraphFrame.getTitle());

      ChartPanel chartPanel = (ChartPanel) hyperGraphFrame.getContentPane();
      JFreeChart chart = chartPanel.getChart();
      XYPlot plot = chart.getXYPlot();

      assertEquals("X Axis", plot.getDomainAxis().getLabel());
      assertEquals("Y Axis", plot.getRangeAxis().getLabel());

      NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
      assertInstanceOf(DecimalFormat.class, yAxis.getNumberFormatOverride());

      XYSeriesCollection dataset = (XYSeriesCollection) plot.getDataset();
      assertEquals(2, dataset.getSeriesCount());

      XYSeries series1 = dataset.getSeries(0);
      assertEquals("Execution 1", series1.getKey());
      assertEquals(2, series1.getItemCount());
      assertEquals(1, series1.getX(0).longValue());
      assertEquals(1000L, series1.getY(0).longValue());
      assertEquals(2, series1.getX(1).longValue());
      assertEquals(2000L, series1.getY(1).longValue());

      XYSeries series2 = dataset.getSeries(1);
      assertEquals("Execution 2", series2.getKey());
      assertEquals(2, series2.getItemCount());
      assertEquals(1, series2.getX(0).longValue());
      assertEquals(1500L, series2.getY(0).longValue());
      assertEquals(2, series2.getX(1).longValue());
      assertEquals(2500L, series2.getY(1).longValue());
    } catch (InvocationTargetException | InterruptedException e) {
      fail("Test failed due to exception: " + e.getMessage());
    } finally {
      if (frameFixture != null) {
        frameFixture.cleanUp();
      }
    }
  }

  @Test
  public void testSaveToSvg() {
    Map<Integer, BigDecimal> data1 = Map.of(1, BigDecimal.valueOf(1.0), 2, BigDecimal.valueOf(2.0));
    Map<Integer, BigDecimal> data2 = Map.of(1, BigDecimal.valueOf(1.5), 2, BigDecimal.valueOf(2.5));

    try {
      SwingUtilities.invokeAndWait(
          () ->
              ChartUtils.createAndShowChart(
                  "test", "X Axis", "Y Axis", data1, "Series 1", data2, "Series 2"));
    } catch (InvocationTargetException | InterruptedException e) {
      fail("Test failed due to exception: " + e.getMessage());
    }
    File svgFile = new File("test.svg");
    assertTrue(svgFile.exists(), "SVG file should be created");
    assertTrue(svgFile.length() > 0, "SVG file should not be empty");
  }
}
