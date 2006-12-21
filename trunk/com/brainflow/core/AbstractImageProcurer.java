/*
 * AbstractImageProcurer.java
 *
 * Created on June 30, 2006, 2:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.utils.NumberUtils;

import java.util.List;

/**
 * @author buchs
 */
public abstract class AbstractImageProcurer {

    private IImageDisplayModel displayModel;
    private AnatomicalVolume displayAnatomy;
    private AnatomicalPoint1D displaySlice;
    private ImageSpace3D imageSpace;


    public AbstractImageProcurer(IImageDisplayModel _displayModel, AnatomicalVolume _displayAnatomy) {
        displayModel = _displayModel;
        displayAnatomy = _displayAnatomy;
        imageSpace = displayModel.getCompositeImageSpace();

    }

    public synchronized void setSlice(AnatomicalPoint1D _displaySlice) {
        assert displayAnatomy.ZAXIS == _displaySlice.getAnatomy();

        if (displaySlice == null) {
            displaySlice = _displaySlice;
            reset();
            return;
        } else if (!NumberUtils.equals(displaySlice.getX(), _displaySlice.getX(), .001)) {
            reset();
            displaySlice = _displaySlice;
        } else {
            displaySlice = _displaySlice;
            // no action required
        }
    }

    public AnatomicalPoint1D getSlice() {
        return displaySlice;
    }

    protected ImageSpace3D getImageSpace() {
        return imageSpace;
    }

    public synchronized void setImageSpace(ImageSpace3D _imageSpace) {
        imageSpace = _imageSpace;
        reset();
    }

    public AnatomicalVolume getDisplayAnatomy() {
        return displayAnatomy;
    }

    protected IImageDisplayModel getModel() {
        return displayModel;
    }

    public AnatomicalPoint1D getDisplaySlice() {
        return displaySlice;
    }

    public abstract void reset();


    public abstract List<ImageLayer2D> procure();


}
