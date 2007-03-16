package com.brainflow.core.pipeline;

import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageLayer2D;
import com.brainflow.core.DisplayChangeType;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.data.IImageData2D;
import com.brainflow.image.data.RGBAImage;

import java.util.List;
import java.util.ArrayList;
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

    // stage 1
    private List<ImageLayer2D> imageLayerStack;

    // stage 2
    private List<RGBAImage> RGBAImageLayerStack;

    // stage 3
    private List<BufferedImage> BufferedImageStack;

    //stage 4
    private List<BufferedImage> resampledImageStack;

    //stage 5
    private BufferedImage compositedImage;

    //stage 6
    private BufferedImage croppedImage;


    //stage 7
    private BufferedImage screenImage;


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


    public int getAffectedLayer() {
        return affectedLayer;
    }

    public AnatomicalVolume getDisplayAnatomy() {
        return displayAnatomy;
    }

    public AnatomicalPoint1D getSlice() {
        return slice;
    }


    public List<RGBAImage> getRGBAImageLayerStack() {
        return RGBAImageLayerStack;
    }

    public void setRGBAImageLayerStack(List<RGBAImage> RGBAImageLayerStack) {
        this.RGBAImageLayerStack = RGBAImageLayerStack;
    }

    public List<ImageLayer2D> getImageLayerStack() {
        return imageLayerStack;
    }

    public void setImageLayerStack(List<ImageLayer2D> imageLayerStack) {
        this.imageLayerStack = imageLayerStack;
    }

    public List<BufferedImage> getBufferedImageStack() {
        return BufferedImageStack;
    }

    public void setBufferedImageStack(List<BufferedImage> bufferedImageStack) {
        BufferedImageStack = bufferedImageStack;
    }


    public List<BufferedImage> getResampledImageStack() {
        return resampledImageStack;
    }

    public void setResampledImageStack(List<BufferedImage> resampledImageStack) {
        this.resampledImageStack = resampledImageStack;
    }

    public BufferedImage getCompositedImage() {
        return compositedImage;
    }

    public void setCompositedImage(BufferedImage compositedImage) {
        this.compositedImage = compositedImage;
    }

    public BufferedImage getScreenImage() {
        return screenImage;
    }

    public void setScreenImage(BufferedImage screenImage) {
        this.screenImage = screenImage;
    }
}
