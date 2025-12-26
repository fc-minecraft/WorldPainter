package org.pepsoft.worldpainter.ui;

import org.pepsoft.worldpainter.App;
import org.pepsoft.worldpainter.util.DockableFrameBuilder;
import org.pepsoft.worldpainter.docking.DockingManager;
import org.pepsoft.worldpainter.util.swing.WrapLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

import static org.pepsoft.worldpainter.docking.DockContext.DOCK_SIDE_EAST;
import static org.pepsoft.worldpainter.docking.DockContext.DOCK_SIDE_WEST;

/**
 * Main Layout Manager for WorldPainter.
 * Defines the overall structure: Toolbar (N), Sidebar (W), Workspace (C), Status (S).
 * Replaces the complex direct DockingManager manipulation in App.java for the primary layout.
 */
public class ModernMainLayout {
    private static final ResourceBundle strings = ResourceBundle.getBundle("org.pepsoft.worldpainter.resources.strings");

    public static void apply(App app, Container contentContainer) {
        // 1. Toolbar (North)
        app.getContentPane().add(ToolbarFactory.create(app), BorderLayout.NORTH);

        // 2. Status Bar (South)
        app.getContentPane().add(StatusBarFactory.create(app), BorderLayout.SOUTH);

        // 3. Central Workspace & Docking
        // We still use DockingManager for the "content" area to support persistence and the specific WP view logic,
        // but we constrain the layout significantly.
        DockingManager dockingManager = app.getDockingManager();

        // Palette (West)
        // Consolidated JTabbedPane
        JTabbedPane palette = PanelFactory.createPalette(app);
        dockingManager.addFrame(new DockableFrameBuilder(palette, "Palette", DOCK_SIDE_WEST, 1).expand().build());

        // Brushes (East)
        // We still allow brushes to be on the right
        dockingManager.addFrame(new DockableFrameBuilder(PanelFactory.createBrushPanel(app), strings.getString("dock.brushes"), DOCK_SIDE_EAST, 1).build());

        // Settings (East)
        // TODO: Move Brush Settings here if not already handled by App
    }
}
