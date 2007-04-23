package com.brainflow.core;

import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 8:00:59 PM
 * To change this template use File | Settings | File Templates.
 */


public class ImageLayerEvent extends EventObject {

    private AbstractLayer affectedLayer;

    private IImageDisplayModel model;

    public ImageLayerEvent(IImageDisplayModel _model, AbstractLayer layer) {
        super(_model);
        model = _model;
        affectedLayer = layer;

    }

    public int getLayerIndex() {
        return model.indexOf(affectedLayer);
    }


    public AbstractLayer getAffectedLayer() {
        return affectedLayer;
    }

    public IImageDisplayModel getModel() {
        return model;
    }

   


}
