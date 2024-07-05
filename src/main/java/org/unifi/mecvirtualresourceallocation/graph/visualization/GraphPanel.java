package org.unifi.mecvirtualresourceallocation.graph.visualization;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
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
}
