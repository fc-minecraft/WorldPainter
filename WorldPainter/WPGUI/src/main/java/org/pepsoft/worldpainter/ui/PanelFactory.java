package org.pepsoft.worldpainter.ui;

import org.pepsoft.util.IconUtils;
import org.pepsoft.worldpainter.App;
import org.pepsoft.worldpainter.biomeschemes.CustomBiomeManager;
import org.pepsoft.worldpainter.layers.Layer;
import org.pepsoft.worldpainter.layers.LayerManager;
import org.pepsoft.worldpainter.layers.Biome;
import org.pepsoft.worldpainter.layers.Annotations;
import org.pepsoft.worldpainter.panels.*;
import org.pepsoft.worldpainter.util.swing.WrapLayout;
import org.pepsoft.worldpainter.theme.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Factory for creating application panels.
 */
public class PanelFactory {
    private static final ResourceBundle strings = ResourceBundle.getBundle("org.pepsoft.worldpainter.resources.strings");

    public static JTabbedPane createPalette(App app) {
        JTabbedPane sidePanel = new JTabbedPane(JTabbedPane.LEFT);
        sidePanel.addTab(null, Icons.load(Icons.KEY_TOOLS), app.createToolPanel(), strings.getString("dock.tools"));
        sidePanel.addTab(null, Icons.load(Icons.KEY_SETTINGS), app.createToolSettingsPanel(), strings.getString("dock.tool.settings"));
        sidePanel.addTab(null, Icons.load(Icons.KEY_LAYERS), app.createLayerPanel(), strings.getString("dock.layers"));
        sidePanel.addTab(null, Icons.load(Icons.KEY_TERRAIN), app.createTerrainPanel(), strings.getString("dock.terrain"));
        sidePanel.addTab(null, Icons.load(Icons.KEY_BIOMES), app.createBiomesPanelContainer(), strings.getString("dock.biomes"));
        sidePanel.addTab(null, Icons.load(Icons.KEY_ANNOTATIONS), app.createAnnotationsPanel(), strings.getString("dock.annotations"));
        return sidePanel;
    }

    public static JPanel createBrushPanel(App app) {
        return app.createBrushPanel();
    }
}
