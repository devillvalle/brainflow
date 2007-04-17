package com.brainflow.display;

import com.jgoodies.binding.beans.Model;
import com.brainflow.core.ImageLayerProperties;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 31, 2006
 * Time: 1:39:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Visibility extends Model {

    private boolean visible = true;
    
    private ImageLayerProperties layer;

    public static final String VISIBLE_PROPERTY = "visible";


    public Visibility(ImageLayerProperties _layer, boolean visible) {
        layer = _layer;
        this.visible = visible;
    }


    public ImageLayerProperties getLayerParameters() {
        return layer;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        boolean old = isVisible();
        this.visible = visible;

        firePropertyChange(VISIBLE_PROPERTY, old, this.visible);
    }

}
