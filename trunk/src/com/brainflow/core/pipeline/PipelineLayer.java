package com.brainflow.core.pipeline;

import com.brainflow.core.ImageLayer2D;
import com.brainflow.image.data.RGBAImage;

import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2007
 * Time: 4:44:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class PipelineLayer {

    private ImageLayer2D layer;

    private RGBAImage coloredImage;

    private BufferedImage rawImage;

    private BufferedImage resampledImage;

    public PipelineLayer(ImageLayer2D _layer) {
        layer = _layer;
    }

    public void clear() {
        layer = null;
        coloredImage = null;
        rawImage = null;
        resampledImage = null;
    }

    public boolean isVisible() {
        return layer.isVisible();
    }

    public ImageLayer2D getLayer() {
        return layer;
    }

    public float getOpacity() {
        return layer.getOpacity();
    }


    public RGBAImage getColoredImage() {
        return coloredImage;
    }

    public void setColoredImage(RGBAImage coloredImage) {
        this.coloredImage = coloredImage;
    }

    public BufferedImage getRawImage() {
        return rawImage;
    }

    public void setRawImage(BufferedImage rawImage) {
        this.rawImage = rawImage;
    }

    public BufferedImage getResampledImage() {
        return resampledImage;
    }

    public void setResampledImage(BufferedImage resampledImage) {
        this.resampledImage = resampledImage;
    }
}
