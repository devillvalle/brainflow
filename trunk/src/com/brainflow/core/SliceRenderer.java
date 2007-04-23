package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.space.ImageSpace2D;
import com.brainflow.image.space.ICoordinateSpace;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 21, 2007
 * Time: 7:06:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SliceRenderer {

    public ICoordinateSpace getImageSpace();

    public Anatomy3D getDisplayAnatomy();

    public void setDisplayAnatomy(Anatomy3D anatomy);

    public void setSlice(AnatomicalPoint1D slice);

    public AnatomicalPoint1D getSlice();

    public BufferedImage render();

    public void renderUnto(Rectangle2D frame, Graphics2D g2);

    public void flush();

    public boolean isVisible();

    public AbstractLayer getLayer();
}
