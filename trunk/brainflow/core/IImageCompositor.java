/*
 * IIImageCompositor.java
 *
 * Created on June 30, 2006, 2:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.core;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.List;

/**
 * @author buchs
 */
public interface IImageCompositor {

    public void setDirty();

    public BufferedImage compose();

    public RenderedImage scale(float sx, float sy);

    public RenderedImage crop(Rectangle2D rect);

    public void setImageList(List<ImageLayer2D> images);

}
