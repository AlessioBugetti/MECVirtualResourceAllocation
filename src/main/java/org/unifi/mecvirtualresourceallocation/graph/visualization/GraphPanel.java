package org.unifi.mecvirtualresourceallocation.graph.visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.unifi.mecvirtualresourceallocation.graph.Vertex;

/**
 * Base class for panels visualizing graphs with vertices and edges. This is an abstract class and
 * should be subclassed to implement specific graph visualization logic.
 */
public abstract class GraphPanel extends JPanel {

  /** Map to store positions of vertices. */
  protected final Map<Vertex, Point> vertexPositions;

  /** Constructs a GraphPanel initializing vertexPositions as an empty HashMap. */
  public GraphPanel() {
    this.vertexPositions = new HashMap<>();
  }

  /** Abstract method to be implemented by subclasses for calculating vertex positions. */
  protected abstract void calculateVertexPositions();

  /**
   * Abstract method to be implemented by subclasses for drawing edges between vertices.
   *
   * @param g2d The Graphics2D context used for drawing
   */
  protected abstract void drawEdges(Graphics2D g2d);

  /**
   * Abstract method to be implemented by subclasses for drawing vertices with labels.
   *
   * @param g2d The Graphics2D context used for drawing
   */
  protected abstract void drawVertices(Graphics2D g2d);

  /**
   * Overrides JPanel's paintComponent method to draw the graph.
   *
   * @param graphics The Graphics context used for painting
   */
  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);

    Graphics2D g2d = (Graphics2D) graphics;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    calculateVertexPositions();
    drawEdges(g2d);
    drawVertices(g2d);
  }

  /**
   * Calculates the size of the graph based on the positions of its vertices. It determines the
   * minimum bounding box that contains all vertices and adds padding.
   *
   * @return A Dimension object representing the width and height of the graph including padding
   */
  public Dimension getGraphSize() {
    calculateVertexPositions();

    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;

    for (Point p : vertexPositions.values()) {
      if (p.x < minX) {
        minX = p.x;
      }
      if (p.y < minY) {
        minY = p.y;
      }
      if (p.x > maxX) {
        maxX = p.x;
      }
      if (p.y > maxY) {
        maxY = p.y;
      }
    }

    int padding = 20;
    int width = maxX - minX + padding * 2;
    int height = maxY - minY + padding * 2;

    return new Dimension(width, height);
  }

  /**
   * Saves the current graph as an SVG file at the specified file path. It uses an SVGGraphics2D
   * context to render the graph and writes the SVG content to the file.
   *
   * @param filePath The file path where the SVG file will be saved
   * @throws IOException If an error occurs during file writing
   */
  protected void saveToSvg(String filePath) throws IOException {
    SVGGraphics2D svgGraphics = new SVGGraphics2D(getWidth(), getHeight());
    svgGraphics.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    svgGraphics.setBackground(new Color(255, 255, 255, 0));
    paintComponent(svgGraphics);
    String svgElement = svgGraphics.getSVGElement();
    Files.write(Paths.get(filePath), svgElement.getBytes());
  }
}
