package com.jidesoft.docking;

public class DockContext {
    public static final int DOCK_SIDE_WEST = 1;
    public static final int DOCK_SIDE_EAST = 2;
    public static final int DOCK_SIDE_NORTH = 4;
    public static final int DOCK_SIDE_SOUTH = 8;
    public static final int STATE_FRAMEDOCKED = 0;
    public static final int STATE_AUTOHIDE = 1;

    public static DockContext DOCK_SIDE_WEST_CONTEXT; // Just a guess, might be used as static field
}
