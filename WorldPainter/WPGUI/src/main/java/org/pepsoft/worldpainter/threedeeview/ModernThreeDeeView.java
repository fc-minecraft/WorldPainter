package org.pepsoft.worldpainter.threedeeview;

import org.pepsoft.worldpainter.ColourScheme;
import org.pepsoft.worldpainter.Dimension;
import org.pepsoft.worldpainter.Tile;
import org.pepsoft.worldpainter.biomeschemes.CustomBiomeManager;
import org.pepsoft.worldpainter.layers.Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.vecmath.*;

public class ModernThreeDeeView extends JComponent implements Dimension.Listener, Tile.Listener {
    private Dimension dimension;
    private ColourScheme colourScheme;
    private CustomBiomeManager customBiomeManager;
    private Set<Layer> hiddenLayers;

    // Camera parameters
    private float yaw = 45.0f;
    private float pitch = 45.0f;
    private float zoom = 1.0f;
    private Point3d center = new Point3d(0, 0, 0);
    private float distance = 200.0f;

    private Point lastMousePos;
    private Point dragStartPos;

    // Rendering cache
    // We can't render the whole world every frame. We might need a chunking system.
    // For now, let's just try to render a grid around the center.
    private static final int RENDER_RADIUS = 128; // Blocks radius to render around center

    public ModernThreeDeeView(Dimension dimension, ColourScheme colourScheme, CustomBiomeManager customBiomeManager, Point initialFocus) {
        this.dimension = dimension;
        this.colourScheme = colourScheme;
        this.customBiomeManager = customBiomeManager;
        if (initialFocus != null) {
            int z = (int) dimension.getHeightAt(initialFocus.x, initialFocus.y);
            this.center = new Point3d(initialFocus.x, z, initialFocus.y);
        }

        dimension.addDimensionListener(this);
        for (Tile tile: dimension.getTiles()) {
            tile.addListener(this);
        }

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMousePos = e.getPoint();
                dragStartPos = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastMousePos == null) return;
                int dx = e.getX() - lastMousePos.x;
                int dy = e.getY() - lastMousePos.y;

                if (SwingUtilities.isLeftMouseButton(e)) {
                    // Rotate
                    yaw += dx * 0.5f;
                    pitch += dy * 0.5f;
                    if (pitch > 89) pitch = 89;
                    if (pitch < -89) pitch = -89;
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    // Pan (simplified: moves center relative to yaw)
                    double rad = Math.toRadians(yaw);
                    double cos = Math.cos(rad);
                    double sin = Math.sin(rad);

                    double moveX = (-dx * cos - dy * sin) * (distance / 500.0);
                    double moveZ = (-dx * sin + dy * cos) * (distance / 500.0);

                    center.x += moveX;
                    center.z += moveZ;

                    // Update Y (height) based on new X/Z
                    center.y = dimension.getHeightAt((int)center.x, (int)center.z);
                }

                lastMousePos = e.getPoint();
                repaint();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                distance *= Math.pow(1.1, e.getWheelRotation());
                if (distance < 10) distance = 10;
                if (distance > 2000) distance = 2000;
                repaint();
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);

        setBackground(new Color(135, 206, 235)); // Sky blue
    }

    public void setHiddenLayers(Set<Layer> hiddenLayers) {
        this.hiddenLayers = hiddenLayers;
        repaint();
    }

    public void moveTo(Point p) {
        this.center.x = p.x;
        this.center.z = p.y;
        this.center.y = dimension.getHeightAt(p.x, p.y);
        repaint();
    }

    public void moveTo(int x, int y) {
        moveTo(new Point(x, y));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Fill background (Sky)
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        // Simple Projector
        // Calculate camera position
        double yawRad = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);
        double dist = distance;

        double camX = center.x - Math.sin(yawRad) * Math.cos(pitchRad) * dist;
        double camY = center.y + Math.sin(pitchRad) * dist;
        double camZ = center.z - Math.cos(yawRad) * Math.cos(pitchRad) * dist;

        // Render simple mesh
        // We iterate around center

        int range = (int)(100 * (distance / 100.0)); // LOD/Range scaling
        if (range < 32) range = 32;
        if (range > 200) range = 200; // Cap to avoid lag

        int step = 1;
        if (range > 64) step = 2;
        if (range > 128) step = 4;

        int startX = (int)center.x - range;
        int endX = (int)center.x + range;
        int startZ = (int)center.z - range;
        int endZ = (int)center.z + range;

        // Ensure even steps for mesh connectivity
        startX = (startX / step) * step;
        startZ = (startZ / step) * step;

        // Z-sorting: Painfully slow in Java2D if we do polygon sorting.
        // Instead, we can try to draw from back to front relative to camera.
        // Or just accept some glitches.
        // But for a terrain, drawing back-to-front is usually sufficient if we iterate correctly.

        // Determine iteration order based on yaw
        // We want to draw farthest chunks first.

        // Simplified approach: use the "Painter's Algorithm" by sorting polygons?
        // Collecting all polygons and sorting is slow.
        // Better: iterate loops such that we draw from far to near.

        // Vector from camera to center
        double dirX = center.x - camX;
        double dirZ = center.z - camZ;

        boolean reverseX = (camX > center.x); // Actually depends on viewing angle vs grid
        boolean reverseZ = (camZ > center.z);

        // This simple heuristic for loop direction works for heightmaps generally
        // if we are outside the grid. If we are inside, it's trickier.

        // Let's collect quads and sort them. It's safer for rotation.
        java.util.List<Quad> quads = new java.util.ArrayList<>();

        for (int x = startX; x < endX; x += step) {
            for (int z = startZ; z < endZ; z += step) {
                if (!dimension.isTilePresent(x, z)) continue;

                int h1 = (int) dimension.getHeightAt(x, z);
                int h2 = (int) dimension.getHeightAt(x + step, z);
                int h3 = (int) dimension.getHeightAt(x + step, z + step);
                int h4 = (int) dimension.getHeightAt(x, z + step);

                int c = dimension.getColourAt(x, z, colourScheme);

                // Simple avg height for sorting
                double avgX = x + step * 0.5;
                double avgZ = z + step * 0.5;
                double avgY = (h1 + h2 + h3 + h4) / 4.0;

                double distSq = (avgX - camX)*(avgX - camX) + (avgY - camY)*(avgY - camY) + (avgZ - camZ)*(avgZ - camZ);

                quads.add(new Quad(x, z, step, h1, h2, h3, h4, c, distSq));
            }
        }

        quads.sort((q1, q2) -> Double.compare(q2.distSq, q1.distSq)); // Far to near

        for (Quad q : quads) {
            Point p1 = project(q.x, q.h1, q.z, camX, camY, camZ, yawRad, pitchRad, cx, cy);
            Point p2 = project(q.x + q.step, q.h2, q.z, camX, camY, camZ, yawRad, pitchRad, cx, cy);
            Point p3 = project(q.x + q.step, q.h3, q.z + q.step, camX, camY, camZ, yawRad, pitchRad, cx, cy);
            Point p4 = project(q.x, q.h4, q.z + q.step, camX, camY, camZ, yawRad, pitchRad, cx, cy);

            if (p1 != null && p2 != null && p3 != null && p4 != null) {
                int[] px = {p1.x, p2.x, p3.x, p4.x};
                int[] py = {p1.y, p2.y, p3.y, p4.y};
                g2.setColor(new Color(q.color));
                g2.fillPolygon(px, py, 4);
                // g2.setColor(g2.getColor().darker());
                // g2.drawPolygon(px, py, 4); // Wireframe for definition
            }
        }
    }

    private Point project(double x, double y, double z, double cx, double cy, double cz, double yaw, double pitch, int ox, int oy) {
        double dx = x - cx;
        double dy = y - cy;
        double dz = z - cz;

        // Rotate Y (Yaw)
        double dx_rot = dx * Math.cos(-yaw) - dz * Math.sin(-yaw);
        double dz_rot = dx * Math.sin(-yaw) + dz * Math.cos(-yaw);

        // Rotate X (Pitch)
        double dy_rot = dy * Math.cos(-pitch) - dz_rot * Math.sin(-pitch);
        double dz_rot2 = dy * Math.sin(-pitch) + dz_rot * Math.cos(-pitch);

        if (dz_rot2 <= 0) return null; // Behind camera

        double fov = 500 + zoom * 100; // Field of view
        int screenX = (int) (ox + (dx_rot * fov) / dz_rot2);
        int screenY = (int) (oy - (dy_rot * fov) / dz_rot2);

        return new Point(screenX, screenY);
    }

    private static class Quad {
        int x, z, step;
        int h1, h2, h3, h4;
        int color;
        double distSq;

        public Quad(int x, int z, int step, int h1, int h2, int h3, int h4, int color, double distSq) {
            this.x = x; this.z = z; this.step = step;
            this.h1 = h1; this.h2 = h2; this.h3 = h3; this.h4 = h4;
            this.color = color;
            this.distSq = distSq;
        }
    }

    // Listener stubs
    @Override public void tilesAdded(Dimension dimension, Set<Tile> tiles) { repaint(); }
    @Override public void tilesRemoved(Dimension dimension, Set<Tile> tiles) { repaint(); }
    @Override public void overlayAdded(Dimension dimension, int index, org.pepsoft.worldpainter.Overlay overlay) {}
    @Override public void overlayRemoved(Dimension dimension, int index, org.pepsoft.worldpainter.Overlay overlay) {}
    @Override public void heightMapChanged(Tile tile) { repaint(); }
    @Override public void terrainChanged(Tile tile) { repaint(); }
    @Override public void waterLevelChanged(Tile tile) { repaint(); }
    @Override public void seedsChanged(Tile tile) { repaint(); }
    @Override public void layerDataChanged(Tile tile, Set<Layer> changedLayers) { repaint(); }
    @Override public void allBitLayerDataChanged(Tile tile) { repaint(); }
    @Override public void allNonBitlayerDataChanged(Tile tile) { repaint(); }
}
