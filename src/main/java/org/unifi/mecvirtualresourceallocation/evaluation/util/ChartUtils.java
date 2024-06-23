package org.unifi.mecvirtualresourceallocation.evaluation.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
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

public class ChartUtils {

  private ChartUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
  }

  public static void createAndShowChart(
      String title,
      String xAxisLabel,
      String yAxisLabel,
      Map<Integer, BigDecimal> data1,
      String label1,
      Map<Integer, BigDecimal> data2,
      String label2) {
    XYSeries series1 = new XYSeries(label1);
    for (Map.Entry<Integer, BigDecimal> entry : data1.entrySet()) {
      series1.add(entry.getKey(), entry.getValue());
    }

    XYSeries series2 = new XYSeries(label2);
    data2.forEach((key, value) -> series2.add(key, value));

    XYSeriesCollection dataset = new XYSeriesCollection();
    dataset.addSeries(series1);
    dataset.addSeries(series2);

    JFreeChart chart =
        ChartFactory.createXYLineChart(
            title, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL, true, true, false);

    XYPlot plot = chart.getXYPlot();
    plot.setBackgroundPaint(Color.WHITE);

    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setSeriesShapesVisible(0, true);
    renderer.setSeriesShapesVisible(1, true);
    renderer.setSeriesLinesVisible(0, true);
    renderer.setSeriesLinesVisible(1, true);
    renderer.setSeriesStroke(0, new BasicStroke(2.0f));
    renderer.setSeriesShape(0, new Rectangle2D.Double(-3, -3, 6, 6));
    renderer.setSeriesStroke(1, new BasicStroke(2.0f));
    renderer.setSeriesShape(1, new Ellipse2D.Double(-3, -3, 6, 6));
    renderer.setSeriesOutlinePaint(0, Color.RED);
    renderer.setSeriesOutlinePaint(1, Color.BLUE);
    renderer.setSeriesOutlineStroke(0, new BasicStroke(2.0f));
    renderer.setSeriesOutlineStroke(1, new BasicStroke(2.0f));
    renderer.setDefaultShapesFilled(false);
    renderer.setUseOutlinePaint(true);

    plot.setRenderer(renderer);

    showChartFrame(title, chart);
  }

  public static void createAndShowChart(
      String title,
      String xAxisLabel,
      String yAxisLabel,
      Map<Integer, BigDecimal> data,
      String label) {
    XYSeries series = new XYSeries(label);
    for (Map.Entry<Integer, BigDecimal> entry : data.entrySet()) {
      series.add(entry.getKey(), entry.getValue());
    }

    XYSeriesCollection dataset = new XYSeriesCollection(series);

    JFreeChart chart =
        ChartFactory.createXYLineChart(
            title, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL, true, true, false);

    XYPlot plot = chart.getXYPlot();
    plot.setBackgroundPaint(Color.WHITE);

    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setSeriesShapesVisible(0, true);
    renderer.setSeriesLinesVisible(0, true);
    renderer.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));
    renderer.setSeriesStroke(0, new BasicStroke(2.0f));
    renderer.setSeriesOutlinePaint(0, Color.RED);
    renderer.setSeriesOutlineStroke(0, new BasicStroke(2.0f));
    renderer.setDefaultShapesFilled(false);
    renderer.setUseOutlinePaint(true);

    plot.setRenderer(renderer);

    showChartFrame(title, chart);
  }

  public static void createAndShowExecutionTimeChart(
      String title,
      String xAxisLabel,
      String yAxisLabel,
      Map<Integer, Long> data1,
      String label1,
      Map<Integer, Long> data2,
      String label2) {
    XYSeries series1 = new XYSeries(label1);
    for (Map.Entry<Integer, Long> entry : data1.entrySet()) {
      series1.add(entry.getKey(), entry.getValue());
    }

    XYSeries series2 = new XYSeries(label2);
    for (Map.Entry<Integer, Long> entry : data2.entrySet()) {
      series2.add(entry.getKey(), entry.getValue());
    }

    XYSeriesCollection dataset = new XYSeriesCollection();
    dataset.addSeries(series1);
    dataset.addSeries(series2);

    JFreeChart chart =
        ChartFactory.createXYLineChart(
            title, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL, true, true, false);

    XYPlot plot = chart.getXYPlot();
    plot.setBackgroundPaint(Color.WHITE);

    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setSeriesShapesVisible(0, true);
    renderer.setSeriesShapesVisible(1, true);
    renderer.setSeriesLinesVisible(0, true);
    renderer.setSeriesLinesVisible(1, true);
    renderer.setSeriesStroke(0, new BasicStroke(2.0f));
    renderer.setSeriesShape(0, new Rectangle2D.Double(-3, -3, 6, 6));
    renderer.setSeriesStroke(1, new BasicStroke(2.0f));
    renderer.setSeriesShape(1, new Ellipse2D.Double(-3, -3, 6, 6));
    renderer.setSeriesOutlinePaint(0, Color.RED);
    renderer.setSeriesOutlinePaint(1, Color.BLUE);
    renderer.setSeriesOutlineStroke(0, new BasicStroke(2.0f));
    renderer.setSeriesOutlineStroke(1, new BasicStroke(2.0f));
    renderer.setDefaultShapesFilled(false);
    renderer.setUseOutlinePaint(true);

    plot.setRenderer(renderer);

    NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
    NumberFormat formatter = new DecimalFormat("0.###E0");
    yAxis.setNumberFormatOverride(formatter);

    showChartFrame(title, chart);
  }

  private static void showChartFrame(String title, JFreeChart chart) {
    JFrame frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    ChartPanel chartPanel = new ChartPanel(chart);
    frame.setContentPane(chartPanel);
    frame.setVisible(true);
  }
}
