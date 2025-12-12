package com.jidesoft.docking;

import java.awt.Container;
import java.io.InputStream;

public class DefaultDockingManager implements DockingManager {
    public DefaultDockingManager(DockableHolder holder, Container contentContainer) {}

    @Override
    public Workspace getWorkspace() { return new Workspace(); }
    @Override
    public void setGroupAllowedOnSidePane(boolean allowed) {}
    @Override
    public void setTabbedPaneCustomizer(TabbedPaneCustomizer customizer) {}
    @Override
    public javax.swing.JComponent getMainContainer() { return null; }
    @Override
    public void addFrame(DockableFrame frame) {}
    @Override
    public void removeFrame(String key) {}
    @Override
    public DockableFrame getFrame(String key) { return null; }
    @Override
    public void showFrame(String key) {}
    @Override
    public void dockFrame(String key, int side, int index) {}
    @Override
    public void activateFrame(String key) {}
    @Override
    public byte[] getLayoutRawData() { return new byte[0]; }
    @Override
    public void loadLayoutFrom(InputStream in) {}
    @Override
    public void resetToDefault() {}
    @Override
    public void setOutlineMode(int mode) {}
}
