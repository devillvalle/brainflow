package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.anatomy.AnatomicalPoint1D;

import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 25, 2007
 * Time: 4:18:03 PM
 * To change this template use File | Settings | File Templates.
 */


public interface IImageProducer {

    public void setPlot(IImagePlot plot);

    public IImagePlot getPlot();
    
    public void setModel(IImageDisplayModel model);

    public IImageDisplayModel getModel();

    public AnatomicalVolume getDisplayAnatomy();

    public void setSlice(AnatomicalPoint1D slice);

    public AnatomicalPoint1D getSlice();
    
    public void updateImage(DisplayChangeEvent event);

    public BufferedImage getImage();
   


}
