package org.unifi.mecvirtualresourceallocation.graph.visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.unifi.mecvirtualresourceallocation.graph.ConflictGraph;
import org.unifi.mecvirtualresourceallocation.graph.Edge;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

/**
 * Panel for visualizing a conflict graph with vertices and edges. Extends GraphPanel and implements
 * methods for specific conflict graph visualization.
 */
public class ConflictGraphPanel extends GraphPanel {
  /** The conflict graph to be visualized. */
  private final ConflictGraph conflictGraph;

  /**
   * Constructs a ConflictGraphPanel with the specified ConflictGraph.
   *
   * @param conflictGraph The ConflictGraph object representing the graph to visualize
   */
  public ConflictGraphPanel(ConflictGraph conflictGraph) {
    this.conflictGraph = conflictGraph;
  }

  /** Calculates positions of vertices around a circle for ConflictGraph visualization. */
  @Override
  protected void calculateVertexPositions() {
    vertexPositions.clear();
    int radius = 200;
    int centerX = getWidth() / 2;
    int centerY = getHeight() / 2;
    int vertexCount = conflictGraph.getVertices().size();

    int i = 0;
    for (Vertex vertex : conflictGraph.getVertices()) {
      double angle = 2 * Math.PI * i / vertexCount;
      int x = (int) (centerX + radius * Math.cos(angle));
      int y = (int) (centerY + radius * Math.sin(angle));
      vertexPositions.put(vertex, new Point(x, y));
      i++;
    }
  }

  /**
   * Draws edges between vertices of the ConflictGraph.
   *
   * @param g2d The Graphics2D context used for drawing
   */
  @Override
  protected void drawEdges(Graphics2D g2d) {
    g2d.setColor(Color.decode("#6e276a"));
    g2d.setStroke(new BasicStroke(2f));
    for (Edge edge : conflictGraph.getEdges()) {
      Point point1 = vertexPositions.get(edge.getVertex1());
      Point point2 = vertexPositions.get(edge.getVertex2());
      g2d.drawLine(point1.x, point1.y, point2.x, point2.y);
    }
  }

  /**
   * Draws vertices of the ConflictGraph with labels.
   *
   * @param g2d The Graphics2D context used for drawing
   */
  @Override
  protected void drawVertices(Graphics2D g2d) {
    g2d.setColor(Color.decode("#71c5ce"));
    g2d.setStroke(new BasicStroke(2));
    FontMetrics fm = g2d.getFontMetrics();
    Font customFont = new Font("CMU Serif", Font.PLAIN, 14);
    g2d.setFont(customFont);
    int vertexSize = 30;

    for (Map.Entry<Vertex, Point> entry : vertexPositions.entrySet()) {
      Point point = entry.getValue();
      int x = point.x - vertexSize / 2;
      int y = point.y - vertexSize / 2;
      g2d.fillRect(x, y, vertexSize, vertexSize);
      g2d.setColor(Color.BLACK);
      g2d.drawRect(x, y, vertexSize, vertexSize);

      String vertexLabel = "p" + entry.getKey().getId();
      int textWidth = fm.stringWidth(vertexLabel);
      int textHeight = fm.getAscent();
      g2d.drawString(vertexLabel, point.x - textWidth / 2, point.y + textHeight / 2);

      g2d.setColor(Color.decode("#71c5ce"));
    }
  }

  /**
   * Saves the ConflictGraph visualization to an SVG file named "conflictgraph.svg".
   *
   * @param filePath The file path where the SVG file will be saved
   * @throws IOException If an error occurs during file writing
   */
  public void saveToSvg(String filePath) throws IOException {
    Path basePath = Paths.get(filePath);
    Path completePath = basePath.resolve("conflictgraph.svg");
    super.saveToSvg(completePath.toString());
  }
}
