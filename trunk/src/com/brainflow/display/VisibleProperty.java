package com.brainflow.display;

import com.jgoodies.binding.beans.Model;
import com.brainflow.core.ImageLayer;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 31, 2006
 * Time: 1:39:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class VisibleProperty extends Model {

    private boolean visible = true;
    private ImageLayerParameters layer;

    public static final String VISIBLE_PROPERTY = "visible";


    public VisibleProperty(ImageLayerParameters _layer, boolean visible) {
        layer = _layer;
        this.visible = visible;
    }


    public ImageLayerParameters getLayerParameters() {
        return layer;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        boolean old = isVisible();
        this.visible = visible;

        firePropertyChange(VisibleProperty.VISIBLE_PROPERTY, old, this.visible);
    }

}
