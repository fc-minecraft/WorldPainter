package org.pepsoft.worldpainter.docking;

import javax.swing.*;
import java.awt.*;

public class DockableFrame extends JPanel {
    private String key;
    private String title;
    private int side; // DOCK_SIDE_*
    private boolean scrollable;
    private boolean expanded;
    private Icon icon;

    public static final int DOCK_SIDE_EAST = 1;
    public static final int DOCK_SIDE_WEST = 2;
    public static final int DOCK_SIDE_SOUTH = 4;
    public static final int DOCK_SIDE_NORTH = 8;

    // Legacy constants matching JIDE
    public static final int STATE_FRAMED = 1;
    public static final int STATE_AUTOHIDE = 2;
    public static final int STATE_HIDDEN = 4;

    public DockableFrame(String title, Icon icon) {
        super(new BorderLayout());
        this.title = title;
        this.icon = icon;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setFrameIcon(Icon icon) {
        this.icon = icon;
    }

    public Icon getFrameIcon() {
        return icon;
    }

    public boolean isShowing() {
        return super.isShowing();
    }
}
