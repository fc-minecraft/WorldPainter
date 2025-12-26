package org.pepsoft.worldpainter.ui;

import org.pepsoft.worldpainter.App;
import org.pepsoft.worldpainter.theme.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Modern Status Bar implementation.
 */
public class StatusBarFactory {
    private static final ResourceBundle strings = ResourceBundle.getBundle("org.pepsoft.worldpainter.resources.strings");

    public static JPanel create(App app) {
        JPanel statusBar = new JPanel(new GridBagLayout());
        statusBar.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.insets = new Insets(0, 10, 0, 10);

        // Location (Coordinates)
        // Give it weight to push others to the right
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.WEST;
        app.locationLabel = new JLabel(strings.getString("info.location"));
        statusBar.add(app.locationLabel, c);

        c.weightx = 0.0;
        c.anchor = GridBagConstraints.CENTER;

        statusBar.add(createSeparator(), c);

        app.heightLabel = new JLabel(strings.getString("info.height"));
        statusBar.add(app.heightLabel, c);

        statusBar.add(createSeparator(), c);

        app.slopeLabel = new JLabel(strings.getString("info.slope"));
        statusBar.add(app.slopeLabel, c);

        statusBar.add(createSeparator(), c);

        app.materialLabel = new JLabel(strings.getString("info.terrain"));
        statusBar.add(app.materialLabel, c);

        statusBar.add(createSeparator(), c);

        app.biomeLabel = new JLabel(strings.getString("info.biome"));
        statusBar.add(app.biomeLabel, c);

        statusBar.add(createSeparator(), c);

        app.zoomLabel = new JLabel(strings.getString("info.zoom"));
        statusBar.add(app.zoomLabel, c);

        return statusBar;
    }

    private static JComponent createSeparator() {
        JSeparator s = new JSeparator(JSeparator.VERTICAL);
        s.setPreferredSize(new Dimension(2, 20));
        return s;
    }
}
