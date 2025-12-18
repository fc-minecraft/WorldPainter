package org.pepsoft.worldpainter.util;

import com.formdev.flatlaf.extras.components.FlatTriStateCheckBox;
import javax.swing.ButtonModel;
import javax.swing.Icon;

/**
 * Modern replacement for TristateCheckBox using FlatLaf Extras.
 */
public final class TristateCheckBox extends FlatTriStateCheckBox {
    // Legacy constants mapping to FlatTriStateCheckBox.State
    // Values derived from typical legacy implementations (e.g. JIDE):
    // 0 = UNSELECTED, 1 = SELECTED, 2 = MIXED
    public static final int STATE_UNSELECTED = 0;
    public static final int STATE_SELECTED = 1;
    public static final int STATE_MIXED = 2;

    public TristateCheckBox() {
        super();
        setAllowIndeterminate(true);
    }
    
    public TristateCheckBox(String text) {
        super(text);
        setAllowIndeterminate(true);
    }

    public TristateCheckBox(String text, Icon icon) {
        super(text);
        setIcon(icon);
        setAllowIndeterminate(true);
    }

    public void setTristateMode(boolean tristateMode) {
        setAllowIndeterminate(tristateMode);
        if (!tristateMode && getState() == State.INDETERMINATE) {
            setState(State.UNSELECTED);
        }
    }
    
    public boolean isTristateMode() {
        return isAllowIndeterminate();
    }

    public void setState(int state) {
        switch (state) {
            case STATE_SELECTED:
                setState(State.SELECTED);
                break;
            case STATE_MIXED:
                if (isAllowIndeterminate()) {
                    setState(State.INDETERMINATE);
                } else {
                    setState(State.SELECTED); // Fallback
                }
                break;
            case STATE_UNSELECTED:
            default:
                setState(State.UNSELECTED);
                break;
        }
    }

    public int getIntState() {
        State s = getState();
        if (s == State.SELECTED) return STATE_SELECTED;
        if (s == State.INDETERMINATE) return STATE_MIXED;
        return STATE_UNSELECTED;
    }

    public void setMixed(boolean mixed) {
        if (mixed) setState(State.INDETERMINATE);
        else if (getState() == State.INDETERMINATE) setState(State.UNSELECTED);
    }

    public boolean isMixed() {
        return getState() == State.INDETERMINATE;
    }
    
    private static final long serialVersionUID = 1L;
}
