package com.brainflow.core;

import com.brainflow.display.ICrosshair;
import com.brainflow.image.anatomy.AnatomicalPoint1D;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 8, 2007
 * Time: 1:03:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinkedSliceController extends SimpleSliceController {

    public LinkedSliceController(ImageView imageView) {
        super(imageView);
    }



    public void setSlice(AnatomicalPoint1D slice) {

        IImagePlot selPlot = getView().getSelectedPlot();
        ICrosshair cross = getView().getCrosshair();
        AnatomicalPoint1D zslice = getSlice(selPlot);

        cross.setValue(slice);
        selPlot.setSlice(slice);
        
        assert slice.getAnatomy() == selPlot.getDisplayAnatomy().ZAXIS;

        Iterator<IImagePlot> iter = getView().plotIterator();
        while (iter.hasNext()) {
             IImagePlot plot = iter.next();
             if (plot == selPlot) continue;
             zslice = getView().getCrosshair().getValue(plot.getDisplayAnatomy().ZAXIS);
             plot.setSlice(zslice);           
        }

    }
}
