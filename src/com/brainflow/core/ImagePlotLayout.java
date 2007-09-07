package com.brainflow.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 5, 2007
 * Time: 11:27:56 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ImagePlotLayout {

    private ImageView view;

    private List<IImagePlot> plots = new ArrayList<IImagePlot>();

    protected ImagePlotLayout(ImageView view) {
        this.view = view;
    }

    protected ImageView getView() {
        return view;
    }


    public List<IImagePlot> getPlots() {
        return Collections.unmodifiableList(plots);
    }


    protected abstract List<IImagePlot> createPlots();

    public void clearPlots() {
        plots.clear();
    }
    
    public abstract void layoutPlots();


    public abstract IImagePlot whichPlot(Point p);




}
