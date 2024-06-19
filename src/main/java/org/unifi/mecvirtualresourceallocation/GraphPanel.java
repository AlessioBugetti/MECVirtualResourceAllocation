package org.unifi.mecvirtualresourceallocation;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Panel for visualizing a graph with vertices and edges.
 */
public class GraphPanel extends JPanel {

    private final ConflictGraph conflictGraph;

    /**
     * Constructs a GraphPanel with a given ConflictGraph.
     *
     * @param conflictGraph The ConflictGraph to be visualized.
     */
    public GraphPanel(ConflictGraph conflictGraph) {
        this.conflictGraph = conflictGraph;
    }

    /**
     * Overrides JPanel's paintComponent method to paint the graph.
     *
     * @param g The Graphics context used for painting.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Map<Vertex, Point> vertexPositions = calculateVertexPositions();
        drawEdges(g2d, vertexPositions);
        drawVertices(g2d, vertexPositions);
    }

    /**
     * Calculates the positions of vertices.
     *
     * @return A map of vertices to their positions (Points).
     */
    private Map<Vertex, Point> calculateVertexPositions() {
        Map<Vertex, Point> vertexPositions = new HashMap<>();
        int radius = 200;  // radius of the circle where vertices are placed
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

        return vertexPositions;
    }

    /**
     * Draws edges between vertices based on their positions.
     *
     * @param g2d            The Graphics2D context used for drawing.
     * @param vertexPositions A map of vertices to their positions.
     */
    private void drawEdges(Graphics2D g2d, Map<Vertex, Point> vertexPositions) {
        g2d.setColor(Color.decode("#6e276a"));
        for (Edge edge : conflictGraph.getEdges()) {
            Point p1 = vertexPositions.get(edge.getV1());
            Point p2 = vertexPositions.get(edge.getV2());
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    /**
     * Draws vertices as squares with labels inside.
     *
     * @param g2d            The Graphics2D context used for drawing.
     * @param vertexPositions A map of vertices to their positions.
     */
    private void drawVertices(Graphics2D g2d, Map<Vertex, Point> vertexPositions) {
        g2d.setColor(Color.decode("#71c5ce"));
        g2d.setStroke(new BasicStroke(2));  // thicker stroke for vertex border
        FontMetrics fm = g2d.getFontMetrics();  // get font metrics for text positioning
        int vertexSize = 30;  // size of the square vertex

        for (Map.Entry<Vertex, Point> entry : vertexPositions.entrySet()) {
            Point point = entry.getValue();
            int x = point.x - vertexSize / 2;
            int y = point.y - vertexSize / 2;
            g2d.fillRect(x, y, vertexSize, vertexSize);  // draw filled square
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, vertexSize, vertexSize);  // draw black border

            // Draw vertex label centered within the square
            String vertexLabel = "p" + entry.getKey().getId();
            int textWidth = fm.stringWidth(vertexLabel);
            int textHeight = fm.getAscent();
            g2d.drawString(vertexLabel, point.x - textWidth / 2, point.y + textHeight / 2);

            g2d.setColor(Color.decode("#71c5ce"));  // restore color for next vertex
        }
    }
}
