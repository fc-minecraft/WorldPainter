package org.pepsoft.worldpainter.docking;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A simplified implementation of DockingManager using standard Swing JSplitPane and JTabbedPane.
 */
public class DefaultDockingManager implements DockingManager {
    private final JFrame frame;
    private final Container container;
    private final Workspace workspace;
    private final Map<String, DockableFrame> frames = new HashMap<>();

    // Layout components
    private final JSplitPane rootSplit;
    private final JTabbedPane leftTabs;
    private final JTabbedPane rightTabs;

    public DefaultDockingManager(JFrame frame, Container container) {
        this.frame = frame;
        this.container = container;
        this.workspace = new Workspace();

        leftTabs = new JTabbedPane();
        leftTabs.setMinimumSize(new Dimension(100, 50));
        rightTabs = new JTabbedPane();
        rightTabs.setMinimumSize(new Dimension(100, 50));

        JSplitPane rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, workspace, rightTabs);
        rightSplit.setResizeWeight(1.0); // Give space to workspace
        rightSplit.setContinuousLayout(true);

        rootSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftTabs, rightSplit);
        rootSplit.setResizeWeight(0.0); // Give initial space to left tabs
        rootSplit.setDividerLocation(360); // Set reasonable default width for palette
        rootSplit.setContinuousLayout(true);

        container.add(rootSplit, BorderLayout.CENTER);
    }

    @Override
    public void addFrame(DockableFrame frame) {
        frames.put(frame.getKey(), frame);
        JComponent component = frame;
        if (frame.isScrollable()) {
            component = new JScrollPane(frame);
        }

        if (frame.getSide() == DockableFrame.DOCK_SIDE_WEST) {
            leftTabs.addTab(frame.getTitle(), frame.getFrameIcon(), component);
        } else if (frame.getSide() == DockableFrame.DOCK_SIDE_EAST) {
            rightTabs.addTab(frame.getTitle(), frame.getFrameIcon(), component);
        } else {
             // Default to left if unknown or other sides not implemented yet
             leftTabs.addTab(frame.getTitle(), frame.getFrameIcon(), component);
        }
    }

    @Override
    public void removeFrame(String key) {
        DockableFrame frame = frames.remove(key);
        if (frame != null) {
            leftTabs.remove(frame);
            rightTabs.remove(frame);
            // Also remove wrapped scrollpanes if necessary - this simplified version might leak components in tabs if not careful
            // For now assume we just remove by component instance if added directly, or iterate tabs
            // to find the wrapper.
            // TODO: Better tab removal logic.
        }
    }

    @Override
    public DockableFrame getFrame(String key) {
        return frames.get(key);
    }

    @Override
    public void showFrame(String key) {
        DockableFrame frame = frames.get(key);
        if (frame != null) {
             // Select the tab containing this frame
             // Need to search tabs
             selectTabForComponent(leftTabs, frame);
             selectTabForComponent(rightTabs, frame);
        }
    }

    private void selectTabForComponent(JTabbedPane tabs, Component comp) {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            Component c = tabs.getComponentAt(i);
            if (c == comp || (c instanceof JScrollPane && ((JScrollPane)c).getViewport().getView() == comp)) {
                tabs.setSelectedIndex(i);
                return;
            }
        }
    }

    @Override
    public JComponent getMainContainer() {
        return (JComponent) container;
    }

    @Override
    public Workspace getWorkspace() {
        return workspace;
    }

    @Override
    public void setOutlineMode(int mode) {
        // No-op in simplified version
    }

    @Override
    public void setGroupAllowedOnSidePane(boolean allowed) {
        // No-op
    }

    @Override
    public void setTabbedPaneCustomizer(TabbedPaneCustomizer customizer) {
        customizer.customize(leftTabs);
        customizer.customize(rightTabs);
    }

    @Override
    public void loadLayoutFrom(InputStream in) {
        // No-op
    }

    @Override
    public byte[] getLayoutRawData() {
        return new byte[0];
    }

    @Override
    public void resetToDefault() {
        // No-op
    }

    @Override
    public void activateFrame(String key) {
        showFrame(key);
    }
}
