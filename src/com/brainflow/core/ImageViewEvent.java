package com.brainflow.core;

import com.brainflow.core.Viewport3D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;

import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 25, 2007
 * Time: 4:56:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewEvent extends EventObject {


    private ImageView view;

    public ImageViewEvent(ImageView view) {
        super(view);
        this.view = view;
        
    }

    public AnatomicalPoint3D getCrosshair() {
        return view.getCursorPos();
    }

    public Viewport3D getViewport() {
        return view.getViewport();
    }

    public ImageView getView() {
        return view;
    }

    public IImageDisplayModel getModel() {
        return view.getModel();
    }

    public IImagePlot getSelectedPlot() {
        return view.getSelectedPlot();
    }
}