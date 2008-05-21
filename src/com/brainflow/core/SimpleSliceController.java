package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 7, 2007
 * Time: 2:03:58 PM
 * To change this template use File | Settings | File Templates.
 */
class SimpleSliceController implements SliceController {

    private double pageStep = .12;

    private ImageView imageView;

    public SimpleSliceController(ImageView imageView) {
        this.imageView = imageView;
    }


    public AnatomicalPoint3D getSlice() {
        return imageView.getCursorPos();

    }

    public AnatomicalPoint1D getSlice(IImagePlot plot) {
         return imageView.getCursorPos().getValue(plot.getDisplayAnatomy().ZAXIS);

    }

    public void setSlice(AnatomicalPoint3D slice) {
        AnatomicalPoint3D cursor = imageView.getCursorPos();

        if (!slice.equals(cursor)) {
            System.out.println("updating cursor slice to " + slice);
            imageView.cursorPos.set(slice);
           
        }

        System.out.println("setting slice to " + slice);

        imageView.getSelectedPlot().setSlice(slice);

    }

    protected ImageView getView() {
        return imageView;

    }

    public void nextSlice() {
        /*AnatomicalPoint1D slice = getSlice();
        AnatomicalPoint3D cursor = imageView.getCursorPos();

        Axis axis = imageView.getViewport().getBounds().findAxis(imageView.getSelectedPlot().getDisplayAnatomy().ZAXIS);
        ImageAxis iaxis = imageView.getModel().getImageAxis(axis);

        int sample = iaxis.nearestSample(slice);
        int nsample = sample + 1;

        if (nsample >= 0 && nsample < iaxis.getNumSamples()) {
            cursor.setValue(iaxis.valueOf(nsample));
            imageView.cursorPos.set(cursor);
        }

        */
    }

    public void previousSlice() {
        /*AnatomicalPoint1D slice = getSlice();
        AnatomicalPoint3D cursor = imageView.getCursorPos();

        Axis axis = imageView.getViewport().getBounds().findAxis(imageView.getSelectedPlot().getDisplayAnatomy().ZAXIS);
        ImageAxis iaxis = imageView.getModel().getImageAxis(axis);

        int sample = iaxis.nearestSample(slice);
        int nsample = sample - 1;
        if (nsample >= 0 && nsample < iaxis.getNumSamples()) {
            cursor.setValue(iaxis.valueOf(nsample));
            imageView.cursorPos.set(cursor);
        }

        */

    }

    public void pageBack() {
        /*AnatomicalPoint1D slice = getSlice();

        AnatomicalPoint3D cursor = imageView.getCursorPos();

        Axis axis = imageView.getViewport().getBounds().findAxis(imageView.getSelectedPlot().getDisplayAnatomy().ZAXIS);
        ImageAxis iaxis = imageView.getModel().getImageAxis(axis);

        int sample = iaxis.nearestSample(slice);
        double page = iaxis.getExtent() * pageStep;
        int jump = (int) (page / iaxis.getSpacing());

        // ensure we move at least one sample
        jump = Math.max(jump, 1);
        int nsample = sample - jump;

        if (nsample >= 0 && nsample < iaxis.getNumSamples()) {
            cursor.setValue(iaxis.valueOf(nsample));
            imageView.cursorPos.set(cursor);
        }

      */
    }

    public void pageForward() {
        /*AnatomicalPoint1D slice = getSlice();

        AnatomicalPoint3D cursor = imageView.getCursorPos();

        Axis axis = imageView.getViewport().getBounds().findAxis(imageView.getSelectedPlot().getDisplayAnatomy().ZAXIS);
        ImageAxis iaxis = imageView.getModel().getImageAxis(axis);


        int sample = iaxis.nearestSample(slice);

        double page = iaxis.getExtent() * pageStep;

        int jump = (int) (page / iaxis.getSpacing());

        // ensure we move at least one sample
        jump = Math.max(jump, 1);
        int nsample = sample + jump;


        if (nsample >= 0 && nsample < iaxis.getNumSamples()) {
            cursor.setValue(iaxis.valueOf(nsample));
            imageView.cursorPos.set(cursor);
        }

        */

    }
}
