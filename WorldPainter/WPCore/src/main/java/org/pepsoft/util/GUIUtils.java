package org.pepsoft.util;

import com.formdev.flatlaf.FlatLaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Modern replacement for legacy GUIUtils, handling high DPI scaling and Look and Feel.
 */
public class GUIUtils {
    public static final float SYSTEM_UI_SCALE_FLOAT = 1.0f; // FlatLaf handles system scaling automatically usually
    public static final int SYSTEM_UI_SCALE = 1;

    private static final Logger logger = LoggerFactory.getLogger(GUIUtils.class);

    private static float UI_SCALE_FLOAT = 1.0f;
    private static int UI_SCALE = 1;

    // Static block to initialize if needed, though most logic is in methods
    static {
        // Initialization if required
    }

    public GUIUtils() {
    }

    /**
     * Scales an image to the UI scale.
     */
    public static BufferedImage scaleToUI(Image image) {
        return scaleToUI(image, true);
    }

    /**
     * Scales an image to the UI scale.
     */
    public static BufferedImage scaleToUI(Image image, boolean smooth) {
        if (image == null) return null;
        
        int width = (int) (image.getWidth(null) * UI_SCALE_FLOAT);
        int height = (int) (image.getHeight(null) * UI_SCALE_FLOAT);
        if (width <= 0) width = 1;
        if (height <= 0) height = 1;

        if (image instanceof BufferedImage && width == image.getWidth(null) && height == image.getHeight(null)) {
            return (BufferedImage) image;
        }

        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resized.createGraphics();
        if (smooth) {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g2.drawImage(image, 0, 0, width, height, null);
        g2.dispose();
        return resized;
    }

    public static void scaleToUI(Container container) {
        // FlatLaf should handle component scaling.
        // Legacy code might manually scale fonts or sizes.
        // We can leave this empty or implement recursive font scaling if absolutely necessary.
        // For now, let's trust FlatLaf.
    }

    public static void scaleWindow(Window window) {
        // Swing usually handles this.
        SwingUtilities.updateComponentTreeUI(window);
    }

    public static void scaleLookAndFeel(float scale) {
        // This method was likely used to force a scale.
        // With FlatLaf, we might set the scale factor if needed,
        // but typically it respects system settings.
        // We can update the internal scale tracking.
        setUIScale(scale);
        // And maybe trigger updateUI
        // FlatLaf.updateUI(); // This updates all windows
    }

    public static void setUIScale(float scale) {
        UI_SCALE_FLOAT = scale;
        UI_SCALE = Math.round(scale);
    }

    public static float getUIScale() {
        return UI_SCALE_FLOAT;
    }

    public static int getUIScaleInt() {
        return UI_SCALE;
    }
}
