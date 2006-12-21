package com.brainflow.core;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Dec 20, 2004
 * Time: 10:10:49 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageLayerRenderer {


    public void drawLayers(Graphics2D g2, IImageDisplayModel dset, IImagePlot iplot, Rectangle2D screenArea);


}
