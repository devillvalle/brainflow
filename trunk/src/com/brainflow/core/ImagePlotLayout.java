package com.brainflow.core;

import com.brainflow.image.anatomy.Anatomy3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 5, 2007
 * Time: 11:27:56 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ImagePlotLayout {

    private ImageView view;

    protected List<IImagePlot> plots = new ArrayList<IImagePlot>();



    protected ImagePlotLayout(ImageView view) {
        this.view = view;
    }

    public ImageView getView() {
        return view;
    }

    public void setView(ImageView view) {
        this.view = view;
        plots.clear();
    }

    protected IImagePlot createPlot(Anatomy3D displayAnatomy) {

        IImagePlot plot = ImagePlotFactory.createComponentPlot(view.getModel(), displayAnatomy);
        
        plot.setSlice(getView().getCursorPos());
        plot.setScreenInterpolation(getView().getScreenInterpolation());
        return plot;


    }

    public abstract SliceController createSliceController();

    public boolean containsPlot(IImagePlot plot) {
        return plots.contains(plot);
    }

    public List<IImagePlot> getPlots() {
        return Collections.unmodifiableList(plots);
    }

    public ListIterator<IImagePlot> iterator() {
        return plots.listIterator();
    }

    public int getNumPlots() {
        return plots.size();
    }

    protected abstract List<IImagePlot> createPlots();
    
    public abstract List<IImagePlot> layoutPlots();

    public abstract IImagePlot whichPlot(Point p);

    public abstract Dimension getPreferredSize();




}
