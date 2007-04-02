package com.brainflow.core;

import com.brainflow.display.ICrosshair;
import com.brainflow.display.Viewport3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 1, 2007
 * Time: 5:18:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageViewListener {

    public void crosshairChanged(ImageViewEvent event);

    public void viewportChanged(ImageViewEvent event);

    public void selectedPlotChanged(ImageViewEvent event);

    

    


}
