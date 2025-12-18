package com.jidesoft.docking;

import javax.swing.JComponent;
import javax.swing.Icon;
import java.awt.Component;

public class DockableFrame extends JComponent {
    public static final int BUTTON_FLOATING = 1;
    public static final int BUTTON_AUTOHIDE = 2;
    public static final int BUTTON_HIDE_AUTOHIDE = 4;

    public DockableFrame(String title, Icon icon) {}
    public DockableFrame(String title) {}

    public String getKey() { return ""; }
    public void setContext(DockContext context) {}
    public DockContext getContext() { return null; }

    public void setAvailableButtons(int buttons) {}

    public void setKey(String key) {}
    public void setTitle(String title) {}
    public void setTabTitle(String title) {}
    public void setSideTitle(String title) {}
    public void setFrameIcon(Icon icon) {}

    public void setAutohideWidth(int width) {}
    public void setDockedWidth(int width) {}
    public void setDockedHeight(int height) {}
    public void setUndockedBounds(java.awt.Rectangle bounds) {}

    public void setHidable(boolean hidable) {}
    public void setShowContextMenu(boolean show) {}
    public void setInitMode(int mode) {}
    public void setInitSide(int side) {}
    public void setInitIndex(int index) {}
    public void setAutohideWhenActive(boolean autohide) {}
    public void setMaximizable(boolean maximizable) {}

    // Usage in App.java suggests it acts like a Container
    public java.awt.Container getContentPane() { return this; }
}
