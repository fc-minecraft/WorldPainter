package org.pepsoft.worldpainter.ui;

import org.pepsoft.worldpainter.App;

import javax.swing.*;

/**
 * Factory for creating the application toolbar.
 */
public class ToolbarFactory {
    public static JToolBar create(App app) {
        return app.createToolBar();
    }
}
