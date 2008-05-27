package com.brainflow.core;

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



    public void setSlice(AnatomicalPoint3D slice) {

        System.out.println("linked slice!!!!" + slice);

        IImagePlot selPlot = getView().getSelectedPlot();
        AnatomicalPoint3D cursor = getView().getCursorPos();


         if (!slice.equals(cursor)) {
            getView().cursorPos.set(cursor);
        }

        selPlot.setSlice(slice);
        

        Iterator<IImagePlot> iter = getView().plotIterator();
        while (iter.hasNext()) {
             IImagePlot plot = iter.next();
             if (plot == selPlot) continue;
             AnatomicalPoint3D crossSlice = getView().getCursorPos();
             plot.setSlice(crossSlice);           
        }

    }
}
