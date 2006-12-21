package com.brainflow.functional;

/**
 * Title:        LCBR Home Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 *
 * @author Brad Buchsbaum
 * @version 1.0
 */

public class Level {

    private String label;
    private int code = 0;

    public Level(String _label, int _code) {
        label = _label;
        code = _code;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return new String(label);
    }

    public void setLabel(String _label) {
        label = _label;
    }

    public boolean isEquals(Object other) {
        if (!(other instanceof Level))
            return false;

        Level otherLevel = (Level) other;

        if ((otherLevel.code == code) && (otherLevel.label == label))
            return true;

        return false;
    }


}