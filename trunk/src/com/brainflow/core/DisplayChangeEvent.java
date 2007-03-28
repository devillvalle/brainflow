package com.brainflow.core;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 25, 2007
 * Time: 4:53:58 PM
 * To change this template use File | Settings | File Templates.
 */
public  class DisplayChangeEvent {

    private DisplayChangeType changeType;

    private int layerLindex = -1;


    public DisplayChangeEvent(DisplayChangeType changeType) {
        this.changeType = changeType;
    }


    public int getLayerLindex() {
        return layerLindex;
    }

    public void setLayerLindex(int layerLindex) {
        this.layerLindex = layerLindex;
    }

    public DisplayChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(DisplayChangeType changeType) {
        this.changeType = changeType;
    }
}
