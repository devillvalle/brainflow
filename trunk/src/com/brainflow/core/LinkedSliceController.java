package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;

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
        AnatomicalPoint3D cursor = getView().getCursorPos();
        AnatomicalPoint1D crossSlice = getSlice(selPlot);

         if (!slice.equals(crossSlice)) {
            cursor.setValue(slice);
            getView().cursorPos.set(cursor);
        }

        selPlot.setSlice(slice);
        
        assert slice.getAnatomy() == selPlot.getDisplayAnatomy().ZAXIS;

        Iterator<IImagePlot> iter = getView().plotIterator();
        while (iter.hasNext()) {
             IImagePlot plot = iter.next();
             if (plot == selPlot) continue;
             crossSlice = getView().getCursorPos().getValue(plot.getDisplayAnatomy().ZAXIS);
             plot.setSlice(crossSlice);           
        }

    }
}
