package com.brainflow.core;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 5, 2007
 * Time: 11:27:56 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ImageViewLayout {

    private ImageView view;

    protected ImageViewLayout(ImageView view) {
        this.view = view;
    }

    public abstract List<IImagePlot> createPlots();

    public abstract void layout();
    


}
