package org.pepsoft.worldpainter.ui;

import org.pepsoft.util.IconUtils;
import org.pepsoft.worldpainter.theme.ThemeManager;

import javax.swing.*;
import java.awt.*;

/**
 * Centralized Icon management.
 * Ensures all icons are loaded at consistent sizes.
 */
public class Icons {
    // Icon Keys
    public static final String KEY_SETTINGS = "wrench";
    public static final String KEY_LAYERS = "layers";
    public static final String KEY_TERRAIN = "edit_selection";
    public static final String KEY_BIOMES = "deciduous_trees_pattern";
    public static final String KEY_ANNOTATIONS = "annotations";
    public static final String KEY_BRUSHES = "paint_brush";
    public static final String KEY_INFO = "information";
    public static final String KEY_TOOLS = "plugin";

    public static Icon load(String name) {
        // Load at medium size by default
        return IconUtils.loadScaledIcon("org/pepsoft/worldpainter/icons/" + name + ".png");
    }

    public static Icon loadSmall(String name) {
        return IconUtils.loadScaledIcon("org/pepsoft/worldpainter/icons/" + name + ".png");
    }
}
