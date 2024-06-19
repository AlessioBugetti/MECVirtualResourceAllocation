package org.unifi.mecvirtualresourceallocation;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel for visualizing a hypergraph with vertices and hyperedges.
 * Extends GraphPanel and implements methods for specific hypergraph
 * visualization.
 */
public class HyperGraphPanel extends GraphPanel {

  private final HyperGraph hyperGraph;
  private final Map<HyperEdge, Color> edgeColors;

  /**
   * Constructs a HyperGraphPanel with the specified HyperGraph.
   *
   * @param hyperGraph The HyperGraph object representing the hypergraph to
   *     visualize.
   */
  public HyperGraphPanel(HyperGraph hyperGraph) {
    this.hyperGraph = hyperGraph;
    this.edgeColors = assignEdgeColors();
  }

  /**
   * Assigns colors to each hyperedge based on its position in the hypergraph.
   *
   * @return A map associating each HyperEdge with a Color.
   */
  private Map<HyperEdge, Color> assignEdgeColors() {
    Map<HyperEdge, Color> colors = new HashMap<>();
    List<HyperEdge> edges = hyperGraph.getEdges();
    float hueIncrement = 1.0f / edges.size();
    float hue = 0.0f;

    for (HyperEdge edge : edges) {
      colors.put(edge, Color.getHSBColor(hue, 0.8f, 0.8f));
      hue += hueIncrement;
    }

    return colors;
  }

  /**
   * Calculates positions of vertices around a circle for HyperGraph
   * visualization.
   */
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
      int x = (int)(centerX + radius * Math.cos(angle));
      int y = (int)(centerY + radius * Math.sin(angle));
      vertexPositions.put(vertex, new Point(x, y));
      i++;
    }
  }

  /**
   * Draws hyperedges between vertices of the HyperGraph.
   *
   * @param g2d The Graphics2D context used for drawing.
   */
  @Override
  protected void drawEdges(Graphics2D g2d) {
    List<HyperEdge> edges = hyperGraph.getEdges();
    g2d.setStroke(new BasicStroke(2f));

    for (HyperEdge edge : edges) {
      Color color = edgeColors.get(edge);
      g2d.setColor(color);
      List<Vertex> vertices = edge.getVertices();
      for (int i = 0; i < vertices.size() - 1; i++) {
        Vertex vertex1 = vertices.get(i);
        Vertex vertex2 = vertices.get(i + 1);

        Point point1 = vertexPositions.get(vertex1);
        Point point2 = vertexPositions.get(vertex2);

        g2d.drawLine(point1.x, point1.y, point2.x, point2.y);
      }
    }
  }

  /**
   * Draws vertices of the HyperGraph with labels.
   *
   * @param g2d The Graphics2D context used for drawing.
   */
  @Override
  protected void drawVertices(Graphics2D g2d) {
    g2d.setColor(Color.decode("#b093b9"));
    g2d.setStroke(new BasicStroke(1));
    FontMetrics fm = g2d.getFontMetrics();
    int vertexSize = 30;

    for (Map.Entry<Vertex, Point> entry : vertexPositions.entrySet()) {
      Point point = entry.getValue();
      int x = point.x - vertexSize / 2;
      int y = point.y - vertexSize / 2;
      g2d.fillOval(x, y, vertexSize, vertexSize);
      g2d.setColor(Color.BLACK);
      g2d.drawOval(x, y, vertexSize, vertexSize);

      String vertexLabel = "v" + entry.getKey().getId();
      int textWidth = fm.stringWidth(vertexLabel);
      int textHeight = fm.getAscent();
      g2d.drawString(vertexLabel, point.x - textWidth / 2,
                     point.y + textHeight / 2);

      g2d.setColor(Color.decode("#71c5ce"));
    }
  }
}
