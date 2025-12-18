package org.pepsoft.worldpainter.util.swing;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple CheckBoxTree implementation.
 */
public class CheckBoxTree extends JTree {
    private final CheckBoxTreeSelectionModel checkBoxSelectionModel;

    public CheckBoxTree() {
        setCellRenderer(new CheckBoxTreeCellRenderer());
        checkBoxSelectionModel = new CheckBoxTreeSelectionModel(this);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = getRowForLocation(e.getX(), e.getY());
                if (row != -1) {
                    TreePath path = getPathForRow(row);
                    if (path != null) {
                         // Only toggle if the checkbox area is clicked (left 20 pixels approx)
                         // Getting actual bounds of checkbox would be better but requires complex logic.
                         Rectangle bounds = getRowBounds(row);
                         if (e.getX() < bounds.x + 20) { // Approximate checkbox width
                             checkBoxSelectionModel.toggleSelection(path);
                             repaint();
                             e.consume(); // Prevent default tree expansion/selection if checkbox clicked
                         }
                    }
                }
            }
        });
    }

    public CheckBoxTreeSelectionModel getCheckBoxTreeSelectionModel() {
        return checkBoxSelectionModel;
    }

    public static class CheckBoxTreeCellRenderer extends DefaultTreeCellRenderer {
        private final JCheckBox checkBox = new JCheckBox();
        private TreeCellRenderer actualTreeRenderer;

        public CheckBoxTreeCellRenderer() {
            checkBox.setOpaque(false);
        }

        public void setActualTreeRenderer(TreeCellRenderer renderer) {
             this.actualTreeRenderer = renderer;
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
             JPanel panel = new JPanel(new BorderLayout());
             panel.setOpaque(false);
             if (tree instanceof CheckBoxTree) {
                 CheckBoxTree cbTree = (CheckBoxTree) tree;
                 if (cbTree.getCheckBoxTreeSelectionModel().isPathSelected(tree.getPathForRow(row))) {
                     checkBox.setSelected(true);
                 } else {
                     checkBox.setSelected(false);
                 }
             }
             panel.add(checkBox, BorderLayout.WEST);

             Component comp;
             if (actualTreeRenderer != null) {
                 comp = actualTreeRenderer.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
             } else {
                 comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
             }
             panel.add(comp, BorderLayout.CENTER);
             return panel;
        }
    }

    public static class CheckBoxTreeSelectionModel extends DefaultTreeSelectionModel {
        private final JTree tree;
        private final Set<TreePath> selectedPaths = new HashSet<>();

        public CheckBoxTreeSelectionModel(JTree tree) {
            this.tree = tree;
        }

        public void setModel(TreeModel model) {
            // No-op for now
        }

        public void addSelectionPath(TreePath path) {
            if (path != null) {
                selectedPaths.add(path);
                // Select all children
                Object node = path.getLastPathComponent();
                TreeModel model = tree.getModel();
                int childCount = model.getChildCount(node);
                for (int i=0; i<childCount; i++) {
                     Object child = model.getChild(node, i);
                     addSelectionPath(path.pathByAddingChild(child));
                }
            }
        }

        public boolean isPathSelected(TreePath path) {
            return selectedPaths.contains(path);
        }

        public void toggleSelection(TreePath path) {
            if (selectedPaths.contains(path)) {
                removeSelectionPath(path);
            } else {
                addSelectionPath(path);
            }
        }

        public void removeSelectionPath(TreePath path) {
             selectedPaths.remove(path);
             // Deselect children
             Object node = path.getLastPathComponent();
             TreeModel model = tree.getModel();
             int childCount = model.getChildCount(node);
             for (int i=0; i<childCount; i++) {
                  Object child = model.getChild(node, i);
                  removeSelectionPath(path.pathByAddingChild(child));
             }
        }

        @Override
        public TreePath[] getSelectionPaths() {
             return selectedPaths.toArray(new TreePath[0]);
        }

        @Override
        public int getSelectionCount() {
             return selectedPaths.size();
        }
    }
}
