package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.axis.ImageAxis;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.BaseProperty;

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
        initCursorListener();
    }

    
    protected void initCursorListener() {
        BeanContainer.get().addListener(imageView.cursorPos, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                AnatomicalPoint3D oldval = (AnatomicalPoint3D)oldValue;
                AnatomicalPoint3D newval = (AnatomicalPoint3D)newValue;

                if (!oldval.equals(newval)) {
                    imageView.getSelectedPlot().setSlice(newval);
                    IImagePlot selectedPlot = imageView.getSelectedPlot();
                    selectedPlot.getComponent().repaint();

                }
            }
        });
    }


    public AnatomicalPoint3D getSlice() {
        return imageView.getCursorPos();

    }

    public AnatomicalPoint1D getSlice(IImagePlot plot) {
        return imageView.getCursorPos().getValue(plot.getDisplayAnatomy().ZAXIS);

    }

    public void setSlice(AnatomicalPoint3D slice) {

        slice = slice.snapToBounds();

        if (!slice.equals(imageView.cursorPos.get())) {
            imageView.cursorPos.set(slice);
        }

        
    }

    protected ImageView getView() {
        return imageView;

    }

    public void nextSlice() {

        AnatomicalPoint3D slice = getSlice();

        Axis axis = imageView.getViewport().getBounds().findAxis(imageView.getSelectedPlot().getDisplayAnatomy().ZAXIS);
        ImageAxis iaxis = imageView.getModel().getImageAxis(axis);

        AnatomicalPoint1D pt = slice.getValue(iaxis.getAnatomicalAxis());
        pt = new AnatomicalPoint1D(pt.getAnatomy(), pt.getValue() + iaxis.getSpacing());

        //todo check bounds

        imageView.cursorPos.set(slice.replace(pt));


    }

    public void previousSlice() {
        AnatomicalPoint3D slice = getSlice();

        Axis axis = imageView.getViewport().getBounds().findAxis(imageView.getSelectedPlot().getDisplayAnatomy().ZAXIS);
        ImageAxis iaxis = imageView.getModel().getImageAxis(axis);

        AnatomicalPoint1D pt = slice.getValue(iaxis.getAnatomicalAxis());
        pt = new AnatomicalPoint1D(pt.getAnatomy(), pt.getValue() - iaxis.getSpacing());

        //todo check bounds

        imageView.cursorPos.set(slice.replace(pt));


    }

    public void pageBack() {
        AnatomicalPoint3D slice = getSlice();

        Axis axis = imageView.getViewport().getBounds().findAxis(imageView.getSelectedPlot().getDisplayAnatomy().ZAXIS);
        ImageAxis iaxis = imageView.getModel().getImageAxis(axis);

        AnatomicalPoint1D pt = slice.getValue(iaxis.getAnatomicalAxis());
        pt = new AnatomicalPoint1D(pt.getAnatomy(), pt.getValue() - iaxis.getExtent() * pageStep);

        //todo check bounds

        imageView.cursorPos.set(slice.replace(pt));


    }

    public void pageForward() {
        AnatomicalPoint3D slice = getSlice();

        Axis axis = imageView.getViewport().getBounds().findAxis(imageView.getSelectedPlot().getDisplayAnatomy().ZAXIS);
        ImageAxis iaxis = imageView.getModel().getImageAxis(axis);

        AnatomicalPoint1D pt = slice.getValue(iaxis.getAnatomicalAxis());
        pt = new AnatomicalPoint1D(pt.getAnatomy(), pt.getValue() + iaxis.getExtent() * pageStep);

        //todo check bounds

        imageView.cursorPos.set(slice.replace(pt));


    }
}
