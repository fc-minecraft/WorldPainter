package org.pepsoft.worldpainter.theme;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.pepsoft.util.GUIUtils;
import org.pepsoft.util.IconUtils;
import org.pepsoft.worldpainter.Configuration;
import org.pepsoft.worldpainter.layers.renderers.VoidRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Centralized manager for UI Theme, Fonts, Colors, and Layout constants.
 * This class handles the initialization of Look and Feel and provides access to standard UI metrics.
 */
public class ThemeManager {
    private static final Logger logger = LoggerFactory.getLogger(ThemeManager.class);

    // --- Font Settings ---
    public static final String FONT_NAME = Font.SANS_SERIF;
    public static final int FONT_SIZE_BASE = 16;

    // --- Layout Metrics ---
    public static final int BUTTON_ARC = 999; // Capsule style for more modern look
    public static final int SCROLLBAR_WIDTH = 16;
    public static final int ROW_HEIGHT = 28;
    public static final int TAB_HEIGHT = 32;
    public static final Insets BUTTON_MARGINS = new Insets(6, 12, 6, 12);

    // --- Icon Sizes ---
    public static final int ICON_SIZE_SMALL = 24;
    public static final int ICON_SIZE_MEDIUM = 32;
    public static final int ICON_SIZE_LARGE = 48;
    public static final int ICON_SIZE_EXTRA_LARGE = 64;

    /**
     * Initializes the application theme based on configuration.
     * Call this before creating any Swing components.
     */
    public static void initTheme(Configuration config) {
        if (config.isSafeMode()) {
            GUIUtils.setUIScale(1.0f);
            logger.info("[SAFE MODE] Not installing visual theme");
            return;
        }

        try {
            applyGlobalDefaults();

            Configuration.LookAndFeel lookAndFeel = (config.getLookAndFeel() != null) ? config.getLookAndFeel() : Configuration.LookAndFeel.SYSTEM;

            switch (lookAndFeel) {
                case SYSTEM:
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                case METAL:
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    break;
                case NIMBUS:
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    break;
                case DARK_METAL:
                case DARK_NIMBUS:
                    FlatDarkLaf.setup();
                    IconUtils.setTheme("dark_metal");
                    patchDarkTheme();
                    break;
                default:
                    FlatLightLaf.setup();
                    break;
            }

            if (GUIUtils.getUIScale() != 1.0f) {
                GUIUtils.scaleLookAndFeel(GUIUtils.getUIScale());
            }
        } catch (Exception e) {
            logger.warn("Could not install selected look and feel", e);
        }
    }

    private static void applyGlobalDefaults() {
        // Core Font
        UIManager.put("defaultFont", new Font(FONT_NAME, Font.PLAIN, FONT_SIZE_BASE));

        // Components
        UIManager.put("Button.margin", BUTTON_MARGINS);
        UIManager.put("ToggleButton.margin", BUTTON_MARGINS); // Ensure ToggleButtons match
        UIManager.put("Button.arc", BUTTON_ARC);
        UIManager.put("ToggleButton.arc", BUTTON_ARC);
        UIManager.put("ScrollBar.width", SCROLLBAR_WIDTH);
        UIManager.put("Tree.rowHeight", ROW_HEIGHT);
        UIManager.put("Table.rowHeight", ROW_HEIGHT);
        UIManager.put("List.rowHeight", ROW_HEIGHT);

        // TabbedPane
        UIManager.put("TabbedPane.tabHeight", TAB_HEIGHT);
        UIManager.put("TabbedPane.showTabSeparators", true);

        // General
        UIManager.put("Component.arrowType", "chevron");
        UIManager.put("Slider.paintValue", Boolean.FALSE); // Cleaner sliders
    }

    private static void patchDarkTheme() {
        Color panelBg = UIManager.getColor("Panel.background");
        if (panelBg != null) {
            VoidRenderer.setColour(panelBg.getRGB());
        }
    }
}
