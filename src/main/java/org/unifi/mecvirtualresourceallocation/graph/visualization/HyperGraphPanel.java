package org.unifi.mecvirtualresourceallocation.graph.visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.unifi.mecvirtualresourceallocation.graph.HyperEdge;
import org.unifi.mecvirtualresourceallocation.graph.HyperGraph;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

/**
 * Panel for visualizing a hypergraph with vertices and hyperedges. Extends GraphPanel and
 * implements methods for specific hypergraph visualization.
 */
public class HyperGraphPanel extends GraphPanel {
  /** The hypergraph to be visualized. */
  private final HyperGraph hyperGraph;

  /** The map of hyperedges to their assigned colors. */
  private final Map<HyperEdge, Color> edgeColors;

  /**
   * Constructs a HyperGraphPanel with the specified HyperGraph.
   *
   * @param hyperGraph The HyperGraph object representing the hypergraph to visualize
   */
  public HyperGraphPanel(HyperGraph hyperGraph) {
    this.hyperGraph = hyperGraph;
    this.edgeColors = assignEdgeColors();
  }

  /**
   * Assigns colors to each hyperedge based on its position in the hypergraph.
   *
   * @return A map associating each HyperEdge with a Color
   */
  private Map<HyperEdge, Color> assignEdgeColors() {
    Map<HyperEdge, Color> colors = new HashMap<>();
    Set<HyperEdge> edges = hyperGraph.getHyperEdges();
    float hueIncrement = 1.0f / edges.size();
    float hue = 0.0f;

    for (HyperEdge edge : edges) {
      colors.put(edge, Color.getHSBColor(hue, 0.8f, 0.8f));
      hue += hueIncrement;
    }

    return colors;
  }

  /** Calculates positions of vertices around a circle for HyperGraph visualization. */
  @Override
  protected void calculateVertexPositions() {
    vertexPositions.clear();
    int radius = 200;
    int centerX = getWidth() / 2;
    int centerY = getHeight() / 2;
    int vertexCount = hyperGraph.getVertices().size();

    int i = 0;
    for (Vertex vertex : hyperGraph.getVertices()) {
      double angle = 2 * Math.PI * i / vertexCount;
      int x = (int) (centerX + radius * Math.cos(angle));
      int y = (int) (centerY + radius * Math.sin(angle));
      vertexPositions.put(vertex, new Point(x, y));
      i++;
    }
  }

  /**
   * Draws hyperedges between vertices of the HyperGraph.
   *
   * @param g2d The Graphics2D context used for drawing
   */
  @Override
  protected void drawEdges(Graphics2D g2d) {
    Set<HyperEdge> edges = hyperGraph.getHyperEdges();
    g2d.setStroke(new BasicStroke(2f));

    for (HyperEdge edge : edges) {
      Color color = edgeColors.get(edge);
      g2d.setColor(color);
      Set<Vertex> vertices = edge.getVertices();
      Iterator<Vertex> iterator = vertices.iterator();
      Vertex vertex1 = iterator.next();
      while (iterator.hasNext()) {
        Vertex vertex2 = iterator.next();

        Point point1 = vertexPositions.get(vertex1);
        Point point2 = vertexPositions.get(vertex2);

        g2d.drawLine(point1.x, point1.y, point2.x, point2.y);

        vertex1 = vertex2;
      }
    }
  }

  /**
   * Draws vertices of the HyperGraph with labels.
   *
   * @param g2d The Graphics2D context used for drawing
   */
  @Override
  protected void drawVertices(Graphics2D g2d) {
    FontMetrics fm = g2d.getFontMetrics();
    Font customFont = new Font("CMU Serif", Font.PLAIN, 14);
    g2d.setFont(customFont);
    int vertexSize = 30;

    for (Map.Entry<Vertex, Point> entry : vertexPositions.entrySet()) {
      Point point = entry.getValue();
      int x = point.x - vertexSize / 2;
      int y = point.y - vertexSize / 2;

      g2d.setColor(Color.decode("#b093b9"));
      g2d.setStroke(new BasicStroke(1));
      g2d.fillOval(x, y, vertexSize, vertexSize);

      g2d.setColor(Color.BLACK);
      g2d.drawOval(x, y, vertexSize, vertexSize);

      String vertexLabel = "v" + entry.getKey().getId();
      int textWidth = fm.stringWidth(vertexLabel);
      int textHeight = fm.getAscent();
      g2d.drawString(vertexLabel, point.x - textWidth / 2, point.y + textHeight / 2);
    }
  }

  /**
   * Saves the HyperGraph visualization to an SVG file named "hypergraph.svg".
   *
   * @throws IOException if an I/O error occurs during file writing
   */
  public void saveToSvg() throws IOException {
    super.saveToSvg("hypergraph.svg");
  }
}
