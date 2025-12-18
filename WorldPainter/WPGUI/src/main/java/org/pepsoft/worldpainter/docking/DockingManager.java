package org.pepsoft.worldpainter.docking;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Map;

public interface DockingManager {
    void addFrame(DockableFrame frame);
    void removeFrame(String key);
    DockableFrame getFrame(String key);
    void showFrame(String key);
    JComponent getMainContainer();
    Workspace getWorkspace();
    void setOutlineMode(int mode);
    void setGroupAllowedOnSidePane(boolean allowed);
    void setTabbedPaneCustomizer(TabbedPaneCustomizer customizer);
    void loadLayoutFrom(InputStream in);
    byte[] getLayoutRawData();
    void resetToDefault();
    void activateFrame(String key);

    int MIX_OUTLINE_MODE = 0;

    interface TabbedPaneCustomizer {
        void customize(JTabbedPane tabbedPane);
    }
}
