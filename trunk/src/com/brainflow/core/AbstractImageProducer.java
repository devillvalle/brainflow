package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.anatomy.AnatomicalPoint1D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 25, 2007
 * Time: 4:22:25 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractImageProducer implements IImageProducer {

    private IImagePlot plot;
    
    private IImageDisplayModel model;

    private AnatomicalVolume displayAnatomy;

    private AnatomicalPoint1D slice;

    public void setModel(IImageDisplayModel model) {
        this.model = model;
    }

    public IImageDisplayModel getModel() {
        return model;
    }

    public void setDisplayAnatomy(AnatomicalVolume displayAnatomy)  {
        this.displayAnatomy = displayAnatomy;
    }

    public AnatomicalVolume getDisplayAnatomy() {
        return displayAnatomy;
    }

    public void setSlice(AnatomicalPoint1D slice) {
        this.slice = slice;

    }

    public AnatomicalPoint1D getSlice() {
        return slice;
    }
}
