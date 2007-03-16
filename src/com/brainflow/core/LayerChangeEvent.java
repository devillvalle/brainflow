package com.brainflow.core;

import com.brainflow.display.Property;

import javax.swing.event.ChangeEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 8:00:59 PM
 * To change this template use File | Settings | File Templates.
 */


public class LayerChangeEvent extends ChangeEvent {

    private ImageLayer affectedLayer;

    private DisplayChangeType changeType;

    private IImageDisplayModel model;

    public LayerChangeEvent(IImageDisplayModel _model, DisplayChangeType _changeType, ImageLayer _layer) {
        super(_model);
        model = _model;
        affectedLayer = _layer;
        changeType = _changeType;
    }

    public DisplayChangeType getChangeType() {
        return changeType;
    }

    public IImageDisplayModel getModel() {
        return model;
    }

    public ImageLayer getAffectedLayer() {
        return affectedLayer;
    }


}
