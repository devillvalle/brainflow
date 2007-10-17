package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.display.ICrosshair;

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


    public AnatomicalPoint1D getSlice() {
        return imageView.getCrosshair().getValue(imageView.getSelectedPlot().getDisplayAnatomy().ZAXIS);

    }

    public AnatomicalPoint1D getSlice(IImagePlot plot) {
         return imageView.getCrosshair().getValue(plot.getDisplayAnatomy().ZAXIS);

    }

    public void setSlice(AnatomicalPoint1D slice) {
        ICrosshair cross = imageView.getCrosshair();
        AnatomicalPoint1D crossSlice = getSlice();
      
        if (!slice.equals(crossSlice)) {
            cross.setValue(slice);
           
        }

        imageView.getSelectedPlot().setSlice(slice);

    }

    protected ImageView getView() {
        return imageView;

    }

    public void nextSlice() {
        AnatomicalPoint1D slice = getSlice();
        ICrosshair cross = imageView.getCrosshair();

        Axis axis = imageView.getViewport().getBounds().findAxis(imageView.getSelectedPlot().getDisplayAnatomy().ZAXIS);
        ImageAxis iaxis = imageView.getModel().getImageAxis(axis);

        int sample = iaxis.nearestSample(slice);
        int nsample = sample + 1;
        if (nsample >= 0 && nsample < iaxis.getNumSamples()) {
            cross.setValue(iaxis.valueOf(nsample));
        }
    }

    public void previousSlice() {
        AnatomicalPoint1D slice = getSlice();
        ICrosshair cross = imageView.getCrosshair();


        Axis axis = imageView.getViewport().getBounds().findAxis(imageView.getSelectedPlot().getDisplayAnatomy().ZAXIS);
        ImageAxis iaxis = imageView.getModel().getImageAxis(axis);

        int sample = iaxis.nearestSample(slice);
        int nsample = sample - 1;
        if (nsample >= 0 && nsample < iaxis.getNumSamples()) {
            cross.setValue(iaxis.valueOf(nsample));
        }

    }

    public void pageBack() {
        AnatomicalPoint1D slice = getSlice();
        ICrosshair cross = imageView.getCrosshair();


        Axis axis = imageView.getViewport().getBounds().findAxis(imageView.getSelectedPlot().getDisplayAnatomy().ZAXIS);
        ImageAxis iaxis = imageView.getModel().getImageAxis(axis);

        int sample = iaxis.nearestSample(slice);
        double page = iaxis.getExtent() * pageStep;
        int jump = (int) (page / iaxis.getSpacing());

        // ensure we move at least one sample
        jump = Math.max(jump, 1);
        int nsample = sample - jump;

        if (nsample >= 0 && nsample < iaxis.getNumSamples()) {
            cross.setValue(iaxis.valueOf(nsample));
        }


    }

    public void pageForward() {
        AnatomicalPoint1D slice = getSlice();
        ICrosshair cross = imageView.getCrosshair();


        Axis axis = imageView.getViewport().getBounds().findAxis(imageView.getSelectedPlot().getDisplayAnatomy().ZAXIS);
        ImageAxis iaxis = imageView.getModel().getImageAxis(axis);


        int sample = iaxis.nearestSample(slice);

        double page = iaxis.getExtent() * pageStep;

        int jump = (int) (page / iaxis.getSpacing());

        // ensure we move at least one sample
        jump = Math.max(jump, 1);
        int nsample = sample + jump;


        if (nsample >= 0 && nsample < iaxis.getNumSamples()) {
            cross.setValue(iaxis.valueOf(nsample));
        }

    }
}
