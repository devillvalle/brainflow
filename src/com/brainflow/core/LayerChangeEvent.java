package com.brainflow.core;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 8:00:59 PM
 * To change this template use File | Settings | File Templates.
 */


public class LayerChangeEvent extends DisplayChangeEvent {

    private ImageLayer affectedLayer;

    private IImageDisplayModel model;

    public LayerChangeEvent(IImageDisplayModel _model, DisplayChangeType _changeType, ImageLayer layer) {
        super(_changeType);
        setChangeType(_changeType);
        model = _model;
        affectedLayer = layer;
        setLayerLindex(model.indexOf(layer));

    }


    public ImageLayer getAffectedLayer() {
        return affectedLayer;
    }

    public IImageDisplayModel getModel() {
        return model;
    }

   


}
