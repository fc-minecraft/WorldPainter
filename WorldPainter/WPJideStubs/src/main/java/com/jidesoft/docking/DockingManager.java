package com.jidesoft.docking;

import java.awt.Container;
import java.io.InputStream;

public interface DockingManager {
    int MIX_OUTLINE_MODE = 0;

    Workspace getWorkspace();
    void setGroupAllowedOnSidePane(boolean allowed);
    void setTabbedPaneCustomizer(TabbedPaneCustomizer customizer);
    javax.swing.JComponent getMainContainer();
    void addFrame(DockableFrame frame);
    void removeFrame(String key);
    DockableFrame getFrame(String key);
    void showFrame(String key);
    void dockFrame(String key, int side, int index);
    void activateFrame(String key);
    byte[] getLayoutRawData();
    void loadLayoutFrom(InputStream in);
    void resetToDefault();
    void setOutlineMode(int mode);

    public interface TabbedPaneCustomizer {
        void customize(javax.swing.JTabbedPane tabbedPane);
    }
}
