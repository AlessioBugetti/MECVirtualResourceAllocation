package org.unifi.mecvirtualresourceallocation.evaluation.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.RenderingHints;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/** Utility class for creating and displaying charts using the JFreeChart library. */
public final class ChartUtils {

  /** Private constructor to prevent instantiation of this utility class. */
  private ChartUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
  }

  /**
   * Creates and displays a chart with a single series of data.
   *
   * @param title the title of the chart
   * @param xAxisLabel the label for the X-axis
   * @param yAxisLabel the label for the Y-axis
   * @param data the data to be displayed in the chart, where the key is the X value and the value
   *     is the Y value
   * @param label the label for the data series
   */
  public static void createAndShowChart(
      String title,
      String xAxisLabel,
      String yAxisLabel,
      Map<Integer, BigDecimal> data,
      String label) {
    XYSeries series = new XYSeries(label);
    data.forEach(series::add);

    XYSeriesCollection dataset = new XYSeriesCollection(series);
    JFreeChart chart =
        ChartFactory.createXYLineChart(
            title, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL, true, true, false);

    customizeChart(chart);
    showChartFrame(title, chart);
  }

  /**
   * Creates and displays a chart with two series of data.
   *
   * @param title the title of the chart
   * @param xAxisLabel the label for the X-axis
   * @param yAxisLabel the label for the Y-axis
   * @param data1 the first data series to be displayed in the chart
   * @param label1 the label for the first data series
   * @param data2 the second data series to be displayed in the chart
   * @param label2 the label for the second data series
   */
  public static void createAndShowChart(
      String title,
      String xAxisLabel,
      String yAxisLabel,
      Map<Integer, BigDecimal> data1,
      String label1,
      Map<Integer, BigDecimal> data2,
      String label2) {
    XYSeries series1 = new XYSeries(label1);
    data1.forEach(series1::add);

    XYSeries series2 = new XYSeries(label2);
    data2.forEach(series2::add);

    XYSeriesCollection dataset = new XYSeriesCollection();
    dataset.addSeries(series1);
    dataset.addSeries(series2);

    JFreeChart chart =
        ChartFactory.createXYLineChart(
            title, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL, true, true, false);

    customizeChart(chart);
    showChartFrame(title, chart);
  }

  /**
   * Creates and displays a chart with two series of execution time data.
   *
   * @param title the title of the chart
   * @param xAxisLabel the label for the X-axis
   * @param yAxisLabel the label for the Y-axis
   * @param data1 the first data series of execution times to be displayed in the chart
   * @param label1 the label for the first data series
   * @param data2 the second data series of execution times to be displayed in the chart
   * @param label2 the label for the second data series
   */
  public static void createAndShowExecutionTimeChart(
      String title,
      String xAxisLabel,
      String yAxisLabel,
      Map<Integer, Long> data1,
      String label1,
      Map<Integer, Long> data2,
      String label2) {
    XYSeries series1 = new XYSeries(label1);
    data1.forEach(series1::add);

    XYSeries series2 = new XYSeries(label2);
    data2.forEach(series2::add);

    XYSeriesCollection dataset = new XYSeriesCollection();
    dataset.addSeries(series1);
    dataset.addSeries(series2);

    JFreeChart chart =
        ChartFactory.createXYLineChart(
            title, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL, true, true, false);

    customizeChart(chart);
    customizeYAxis(chart);
    showChartFrame(title, chart);
  }

  /**
   * Customizes the appearance of the chart.
   *
   * @param chart the chart to be customized
   */
  private static void customizeChart(JFreeChart chart) {
    chart
        .getRenderingHints()
        .put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    XYPlot plot = chart.getXYPlot();
    plot.setBackgroundPaint(Color.WHITE);
    plot.setDomainGridlinesVisible(true);
    plot.setRangeGridlinesVisible(true);
    plot.setDomainGridlinePaint(Color.decode("#e2e2e2"));
    plot.setRangeGridlinePaint(Color.decode("#e2e2e2"));
    plot.setDomainGridlineStroke(new BasicStroke(0.5f));
    plot.setRangeGridlineStroke(new BasicStroke(0.5f));

    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setSeriesStroke(0, new BasicStroke(2.0f));
    renderer.setSeriesShapesVisible(0, false);

    if (plot.getDataset().getSeriesCount() == 2) {
      renderer.setSeriesStroke(1, new BasicStroke(2.0f));
      renderer.setSeriesShapesVisible(1, false);
    }

    plot.setRenderer(renderer);
  }

  /**
   * Customizes the Y-axis of the chart to use scientific notation.
   *
   * @param chart the chart to be customized
   */
  private static void customizeYAxis(JFreeChart chart) {
    XYPlot plot = chart.getXYPlot();
    NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
    NumberFormat formatter = new DecimalFormat("0.###E0");
    yAxis.setNumberFormatOverride(formatter);
  }

  /**
   * Displays the chart in a JFrame.
   *
   * @param title the title of the JFrame
   * @param chart the chart to be displayed
   */
  private static void showChartFrame(String title, JFreeChart chart) {
    JFrame frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    ChartPanel chartPanel = new ChartPanel(chart);
    frame.setContentPane(chartPanel);
    frame.setVisible(true);
  }
}
