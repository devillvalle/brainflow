package com.brainflow.core.pipeline;

import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.DisplayChangeType;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalVolume;

import java.util.List;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 1, 2007
 * Time: 5:27:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class StageFerry {

    private int affectedLayer = -1;

    private IImageDisplayModel model;

    private AnatomicalPoint1D slice;

    private AnatomicalVolume displayAnatomy;

    private DisplayChangeType displayChangeType;

    private List<PipelineLayer> layers;


    //stage 5
    private BufferedImage compositeImage;

    //stage 6
    private BufferedImage croppedImage;

    //stage 7
    private BufferedImage resizedImage;


    public StageFerry(IImageDisplayModel model, AnatomicalPoint1D slice,
                      AnatomicalVolume displayAnatomy, DisplayChangeType type) {
        this.model = model;
        this.slice = slice;
        this.displayAnatomy = displayAnatomy;
        this.displayChangeType = type;
    }

    public StageFerry(IImageDisplayModel model, AnatomicalPoint1D slice,
                      AnatomicalVolume displayAnatomy, DisplayChangeType type, int affectedLayer) {

        this.model = model;
        this.slice = slice;
        this.displayAnatomy = displayAnatomy;
        this.displayChangeType = type;
        this.affectedLayer = affectedLayer;
    }


    public DisplayChangeType getDisplayChangeType() {
        return displayChangeType;
    }

    public IImageDisplayModel getModel() {
        return model;
    }


    public List<PipelineLayer> getLayers() {
        return layers;
    }


    public void setLayers(List<PipelineLayer> layers) {
        this.layers = layers;
    }

    public int getAffectedLayer() {
        return affectedLayer;
    }

    public AnatomicalVolume getDisplayAnatomy() {
        return displayAnatomy;
    }

    public AnatomicalPoint1D getSlice() {
        return slice;
    }


    public BufferedImage getCompositeImage() {
        return compositeImage;
    }

    public void setCompositeImage(BufferedImage compositeImage) {
        this.compositeImage = compositeImage;
    }

    public BufferedImage getResizedImage() {
        return resizedImage;
    }

    public void setResizedImage(BufferedImage resizedImage) {
        this.resizedImage = resizedImage;
    }


    public BufferedImage getCroppedImage() {
        return croppedImage;
    }

    public void setCroppedImage(BufferedImage croppedImage) {
        this.croppedImage = croppedImage;
    }
}
