package com.brainflow.core;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 1, 2007
 * Time: 3:24:09 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageLayerListener {

    
    public void thresholdChanged(ImageLayerEvent event);

    public void colorMapChanged(ImageLayerEvent event);

    public void opacityChanged(ImageLayerEvent event);

    public void interpolationMethodChanged(ImageLayerEvent event);

    public void visibilityChanged(ImageLayerEvent event);



}
