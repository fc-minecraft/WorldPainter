/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pepsoft.worldpainter.threedeeview;

import org.pepsoft.minecraft.Direction;
import org.pepsoft.util.IconUtils;
import org.pepsoft.util.ProgressReceiver;
import org.pepsoft.util.ProgressReceiver.OperationCancelled;
import org.pepsoft.util.swing.ProgressDialog;
import org.pepsoft.util.swing.ProgressTask;
import org.pepsoft.worldpainter.App;
import org.pepsoft.worldpainter.ColourScheme;
import org.pepsoft.worldpainter.Dimension;
import org.pepsoft.worldpainter.Tile;
import org.pepsoft.worldpainter.biomeschemes.CustomBiomeManager;
import org.pepsoft.worldpainter.layers.Layer;
import org.pepsoft.worldpainter.threedeeview.Tile3DRenderer.LayerVisibilityMode;
import org.pepsoft.worldpainter.util.BetterAction;
import org.pepsoft.worldpainter.util.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.pepsoft.util.GUIUtils.scaleToUI;
import static org.pepsoft.util.swing.MessageUtils.beepAndShowError;
import static org.pepsoft.worldpainter.App.INT_NUMBER_FORMAT;
import static org.pepsoft.worldpainter.Constants.DIM_NORMAL;
import static org.pepsoft.worldpainter.Dimension.Anchor.NORMAL_DETAIL;
import static org.pepsoft.worldpainter.threedeeview.Tile3DRenderer.LayerVisibilityMode.*;
import static org.pepsoft.worldpainter.util.LayoutUtils.setDefaultSizeAndLocation;

/**
 *
 * @author pepijn
 */
public class ThreeDeeFrame extends JFrame implements WindowListener {
    public ThreeDeeFrame(Dimension dimension, ColourScheme colourScheme, CustomBiomeManager customBiomeManager, Point initialCoords) throws HeadlessException {
        super("WorldPainter - 3D View");
        setIconImage(App.ICON);
        this.colourScheme = colourScheme;
        this.customBiomeManager = customBiomeManager;
        this.coords = initialCoords;
        
        scrollPane = new JScrollPane();
        
        // Modern view handles its own mouse events
        
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        alwaysOnTopButton.setToolTipText("Set the 3D view window to be always on top");
        alwaysOnTopButton.addActionListener(e -> {
            if (alwaysOnTopButton.isSelected()) {
                ThreeDeeFrame.this.setAlwaysOnTop(true);
            } else {
                ThreeDeeFrame.this.setAlwaysOnTop(false);
            }
        });
        
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.add(alwaysOnTopButton);
        toolBar.addSeparator();
        // Actions might need to be disabled or adapted for modern view if not applicable
        // toolBar.add(ROTATE_LEFT_ACTION);
        // toolBar.add(ROTATE_RIGHT_ACTION);
        // toolBar.addSeparator();
        // toolBar.add(ZOOM_OUT_ACTION);
        // toolBar.add(RESET_ZOOM_ACTION);
        // toolBar.add(ZOOM_IN_ACTION);
        // toolBar.addSeparator();
        // toolBar.add(EXPORT_IMAGE_ACTION);
        // toolBar.addSeparator();
        toolBar.add(MOVE_TO_SPAWN_ACTION);
        toolBar.add(MOVE_TO_ORIGIN_ACTION);
        toolBar.addSeparator();
        // toolBar.add(new JLabel("Visible layers:"));
        // final JRadioButton radioButtonLayersNone = new JRadioButton(NO_LAYERS_ACTION);
        // layerVisibilityButtonGroup.add(radioButtonLayersNone);
        // toolBar.add(radioButtonLayersNone);
        // final JRadioButton radioButtonLayersSync = new JRadioButton(SYNC_LAYERS_ACTION);
        // layerVisibilityButtonGroup.add(radioButtonLayersSync);
        // toolBar.add(radioButtonLayersSync);
        // final JRadioButton radioButtonLayersAll = new JRadioButton(SURFACE_LAYERS_ACTION);
        // layerVisibilityButtonGroup.add(radioButtonLayersAll);
        // toolBar.add(radioButtonLayersAll);
        getContentPane().add(toolBar, BorderLayout.NORTH);

        glassPane = new GlassPane();
        setGlassPane(glassPane);
        getGlassPane().setVisible(true);
        
        setSize(800, 600);
        scaleToUI(this);
        setDefaultSizeAndLocation(this, 60);
        
        setDimension(dimension);
        
        addWindowListener(this);
    }

    public final Dimension getDimension() {
        return dimension;
    }

    public final void setDimension(Dimension dimension) {
        this.dimension = dimension;
        if (dimension != null) {
            modernThreeDeeView = new ModernThreeDeeView(dimension, colourScheme, customBiomeManager, coords);
            modernThreeDeeView.setHiddenLayers(hiddenLayers);
            scrollPane.setViewportView(modernThreeDeeView);
            MOVE_TO_SPAWN_ACTION.setEnabled(dimension.getAnchor().equals((dimension.getWorld().getSpawnPointDimension() == null) ? NORMAL_DETAIL : dimension.getWorld().getSpawnPointDimension()));
        }
    }

    public void setHiddenLayers(Set<Layer> hiddenLayers) {
        this.hiddenLayers = hiddenLayers;
        if (modernThreeDeeView != null) {
            modernThreeDeeView.setHiddenLayers(hiddenLayers);
        }
    }

    public void resetAlwaysOnTop() {
        if (isAlwaysOnTop()) {
            setAlwaysOnTop(false);
            alwaysOnTopButton.setSelected(false);
        }
    }

    public void moveTo(Point coords) {
        this.coords = coords;
        if (modernThreeDeeView != null) {
            modernThreeDeeView.moveTo(coords);
        }
    }

    public void refresh(boolean clear) {
        if (modernThreeDeeView != null) {
            modernThreeDeeView.repaint();
        }
    }

    // WindowListener

    @Override
    public void windowOpened(WindowEvent e) {
        moveTo(coords);
    }

    @Override public void windowClosing(WindowEvent e) {}
    @Override public void windowClosed(WindowEvent e) {}
    @Override public void windowIconified(WindowEvent e) {}
    @Override public void windowDeiconified(WindowEvent e) {}
    @Override public void windowActivated(WindowEvent e) {}
    @Override public void windowDeactivated(WindowEvent e) {}
    
    private final Action ROTATE_LEFT_ACTION = new BetterAction("rotate3DViewLeft", "Rotate left", ICON_ROTATE_LEFT) {
        @Override
        public void performAction(ActionEvent e) {
            // No-op for modern view
        }
    };
    
    private final Action ROTATE_RIGHT_ACTION = new BetterAction("rotate3DViewRight", "Rotate right", ICON_ROTATE_RIGHT) {
        @Override
        public void performAction(ActionEvent e) {
            // No-op
        }
    };

    private final Action EXPORT_IMAGE_ACTION = new BetterAction("export3DViewImage", "Export image", ICON_EXPORT_IMAGE) {
        @Override
        public void performAction(ActionEvent e) {
             // No-op
        }
    };
    
    private final Action MOVE_TO_SPAWN_ACTION = new BetterAction("move3DViewToSpawn", "Move to spawn", ICON_MOVE_TO_SPAWN) {
        {
            setShortDescription("Move the view to the spawn location");
        }
        
        @Override
        public void performAction(ActionEvent e) {
            if (dimension.getAnchor().dim == DIM_NORMAL) {
                Point spawn = dimension.getWorld().getSpawnPoint();
                if (modernThreeDeeView != null) {
                    modernThreeDeeView.moveTo(spawn.x, spawn.y);
                }
            }
        }
    };
    
    private final Action MOVE_TO_ORIGIN_ACTION = new BetterAction("move3DViewToOrigin", "Move to origin", ICON_MOVE_TO_ORIGIN) {
        {
            setShortDescription("Move the view to the origin (coordinates 0,0)");
        }
        
        @Override
        public void performAction(ActionEvent e) {
            if (modernThreeDeeView != null) {
                modernThreeDeeView.moveTo(0, 0);
            }
        }
    };
    
    private final Action ZOOM_IN_ACTION = new BetterAction("zoom3DViewIn", "Zoom in", ICON_ZOOM_IN) {
        @Override
        public void performAction(ActionEvent e) {
            // No-op
        }
    };
    
    private final Action RESET_ZOOM_ACTION = new BetterAction("reset3DViewZoom", "Reset zoom", ICON_RESET_ZOOM) {
        @Override
        public void performAction(ActionEvent e) {
            // No-op
        }
    };
    
    private final Action ZOOM_OUT_ACTION = new BetterAction("zoom3DViewOut", "Zoom out", ICON_ZOOM_OUT) {
        @Override
        public void performAction(ActionEvent e) {
            // No-op
        }
    };

    private final Action NO_LAYERS_ACTION = new BetterAction("layerVisibilityNone", "None") {
        @Override
        protected void performAction(ActionEvent e) {
            // No-op
        }
    };

    private final Action SYNC_LAYERS_ACTION = new BetterAction("layerVisibilitySync", "Sync") {
        @Override
        protected void performAction(ActionEvent e) {
            // No-op
        }
    };

    private final Action SURFACE_LAYERS_ACTION = new BetterAction("layerVisibilitySurface", "Surface") {
        @Override
        protected void performAction(ActionEvent e) {
            // No-op
        }
    };

    private final JScrollPane scrollPane;
    private final GlassPane glassPane;
    private final CustomBiomeManager customBiomeManager;
    private final ButtonGroup layerVisibilityButtonGroup = new ButtonGroup();
    final JToggleButton alwaysOnTopButton = new JToggleButton(ICON_ALWAYS_ON_TOP);
    private Dimension dimension;
    private ModernThreeDeeView modernThreeDeeView;
    private ColourScheme colourScheme;
    private int rotation = 3, zoom = 1;
    private Point coords;
    private LayerVisibilityMode layerVisibility = SURFACE;
    private Set<Layer> hiddenLayers;
    
    private static final Direction[] DIRECTIONS = {Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH};
    
    private static final Icon ICON_ROTATE_LEFT    = IconUtils.loadScaledIcon("org/pepsoft/worldpainter/icons/arrow_rotate_anticlockwise.png");
    private static final Icon ICON_ROTATE_RIGHT   = IconUtils.loadScaledIcon("org/pepsoft/worldpainter/icons/arrow_rotate_clockwise.png");
    private static final Icon ICON_EXPORT_IMAGE   = IconUtils.loadScaledIcon("org/pepsoft/worldpainter/icons/picture_save.png");
    private static final Icon ICON_MOVE_TO_SPAWN  = IconUtils.loadScaledIcon("org/pepsoft/worldpainter/icons/spawn_red.png");
    private static final Icon ICON_MOVE_TO_ORIGIN = IconUtils.loadScaledIcon("org/pepsoft/worldpainter/icons/arrow_in.png");
    private static final Icon ICON_ALWAYS_ON_TOP  = IconUtils.loadScaledIcon("org/pepsoft/worldpainter/icons/lock.png");
    private static final Icon ICON_ZOOM_IN        = IconUtils.loadScaledIcon("org/pepsoft/worldpainter/icons/magnifier_zoom_in.png");
    private static final Icon ICON_RESET_ZOOM     = IconUtils.loadScaledIcon("org/pepsoft/worldpainter/icons/magnifier.png");
    private static final Icon ICON_ZOOM_OUT       = IconUtils.loadScaledIcon("org/pepsoft/worldpainter/icons/magnifier_zoom_out.png");
    
    private static final int MIN_ZOOM = -2;
    private static final int MAX_ZOOM = 4;
    
    private static final long serialVersionUID = 1L;
}
