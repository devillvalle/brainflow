package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.axis.AxisRange;

import java.awt.image.BufferedImage;
import java.awt.*;

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
    
  
    public IImageDisplayModel getModel();

    public AnatomicalVolume getDisplayAnatomy();

    public void setSlice(AnatomicalPoint1D slice);

    public AnatomicalPoint1D getSlice();

    public void setScreenSize(Rectangle rect);

    public void setYAxis(AxisRange yaxis);

    public void setXAxis(AxisRange xaxis);

    public void reset();

    public AxisRange getXAxis();

    public AxisRange getYAxis();

    public Rectangle getScreenSize();

    public BufferedImage getImage();
   


}
