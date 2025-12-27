package org.pepsoft.worldpainter.theme;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatInspector;
import org.pepsoft.util.GUIUtils;
import org.pepsoft.util.IconUtils;
import org.pepsoft.worldpainter.Configuration;
import org.pepsoft.worldpainter.layers.renderers.VoidRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Centralized manager for UI Theme, Fonts, Colors, and Layout constants.
 * This class handles the initialization of Look and Feel and provides access to standard UI metrics.
 *
 * DESIGN GOALS:
 * - Modern, Minimalist, Child-friendly
 * - Large touch-friendly targets
 * - High readability (Large fonts)
 * - Clean layouts with ample whitespace
 */
public class ThemeManager {
    private static final Logger logger = LoggerFactory.getLogger(ThemeManager.class);

    // --- Font Settings ---
    public static final String FONT_NAME = Font.SANS_SERIF;
    public static final int FONT_SIZE_BASE = 14; // Standard desktop readable size
    public static final int FONT_SIZE_HEADER = 18;

    // --- Layout Metrics ---
    public static final int BUTTON_ARC = 8; // Modern rounded corners
    public static final int COMPONENT_ARC = 8; // Consistent rounded corners
    public static final int SCROLLBAR_WIDTH = 12; // Standard width
    public static final int ROW_HEIGHT = 24; // Standard list height
    public static final int TAB_HEIGHT = 28; // Standard tab height
    public static final int GAP_SMALL = 4;
    public static final int GAP_MEDIUM = 8;
    public static final int GAP_LARGE = 12;

    public static final Insets BUTTON_MARGINS = new Insets(2, 6, 2, 6);
    public static final Insets DIALOG_PADDING = new Insets(12, 12, 12, 12);

    // --- Icon Sizes ---
    public static final int ICON_SIZE_SMALL = 16;
    public static final int ICON_SIZE_MEDIUM = 24;
    public static final int ICON_SIZE_LARGE = 32;
    public static final int ICON_SIZE_EXTRA_LARGE = 48;

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
            // Apply scale factor if needed (FlatLaf handles this, but we might want extra scaling)
            // For now, we rely on FlatLaf's auto-scaling + our large base sizes.

            applyGlobalDefaults();

            // Enforce Dark Mode or Light Mode based on "Child Friendly" (Light usually preferred)
            // or "Modern" (Dark is popular). Let's stick to system preference but default to a soft Light theme if unset.
            // For now, respect config but override defaults.

            Configuration.LookAndFeel lookAndFeel = (config.getLookAndFeel() != null) ? config.getLookAndFeel() : Configuration.LookAndFeel.SYSTEM;

            switch (lookAndFeel) {
                case DARK_METAL:
                case DARK_NIMBUS:
                    FlatDarkLaf.setup();
                    IconUtils.setTheme("dark_metal");
                    patchDarkTheme();
                    break;
                case SYSTEM:
                case METAL:
                case NIMBUS:
                default:
                    FlatLightLaf.setup();
                    break;
            }

            if (GUIUtils.getUIScale() != 1.0f) {
                GUIUtils.scaleLookAndFeel(GUIUtils.getUIScale());
            }

            // Enable FlatInspector with Ctrl+Shift+Alt+X
            FlatInspector.install("ctrl shift alt X");

        } catch (Exception e) {
            logger.warn("Could not install selected look and feel", e);
        }
    }

    private static void applyGlobalDefaults() {
        // Localization for standard dialogs
        ResourceBundle strings = ResourceBundle.getBundle("org.pepsoft.worldpainter.resources.strings");
        UIManager.put("OptionPane.yesButtonText", strings.getString("option.yes"));
        UIManager.put("OptionPane.noButtonText", strings.getString("option.no"));
        UIManager.put("OptionPane.cancelButtonText", strings.getString("option.cancel"));
        UIManager.put("OptionPane.okButtonText", strings.getString("option.ok"));

        // Core Font
        Font baseFont = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE_BASE);
        UIManager.put("defaultFont", baseFont);

        // Buttons
        UIManager.put("Button.font", baseFont);
        UIManager.put("Button.margin", BUTTON_MARGINS);
        UIManager.put("ToggleButton.margin", BUTTON_MARGINS);
        UIManager.put("Button.arc", BUTTON_ARC);
        UIManager.put("ToggleButton.arc", BUTTON_ARC);

        // Inputs
        UIManager.put("Component.arc", COMPONENT_ARC);
        UIManager.put("TextComponent.arc", COMPONENT_ARC);

        // Scrollbars
        UIManager.put("ScrollBar.width", SCROLLBAR_WIDTH);
        UIManager.put("ScrollBar.thumbArc", 999);

        // Trees, Tables, Lists
        UIManager.put("Tree.rowHeight", ROW_HEIGHT);
        UIManager.put("Table.rowHeight", ROW_HEIGHT);
        UIManager.put("List.rowHeight", ROW_HEIGHT);
        UIManager.put("Tree.font", baseFont);
        UIManager.put("Table.font", baseFont);
        UIManager.put("List.font", baseFont);

        // TabbedPane
        UIManager.put("TabbedPane.tabHeight", TAB_HEIGHT);
        UIManager.put("TabbedPane.font", baseFont.deriveFont(Font.BOLD));
        UIManager.put("TabbedPane.showTabSeparators", true);

        // Menus
        UIManager.put("Menu.font", baseFont);
        UIManager.put("MenuItem.font", baseFont);
        UIManager.put("MenuItem.selectionArc", 10);

        // General
        UIManager.put("Component.arrowType", "chevron");
        UIManager.put("Slider.paintValue", Boolean.FALSE); // Cleaner sliders

        // Tooltips
        UIManager.put("ToolTip.font", baseFont);
    }

    private static void patchDarkTheme() {
        Color panelBg = UIManager.getColor("Panel.background");
        if (panelBg != null) {
            VoidRenderer.setColour(panelBg.getRGB());
        }
    }
}
