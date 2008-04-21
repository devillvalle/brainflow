package com.brainflow.core;

import com.brainflow.image.anatomy.Anatomy3D;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 7, 2007
 * Time: 4:15:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class OrthoPlotLayout extends ImagePlotLayout {

    public enum ORIENTATION {
        HORIZONTAL,
        VERTICAL,
        TRIANGULAR
    }

    private ORIENTATION orientation = ORIENTATION.HORIZONTAL;

    private LinkedSliceController sliceController = null;

    public OrthoPlotLayout(ImageView view) {
        super(view);
    }

    public OrthoPlotLayout(ImageView view, ORIENTATION orientation) {
        super(view);
        this.orientation = orientation;
    }

    protected List<IImagePlot> createPlots() {

        List<IImagePlot> plots = new ArrayList<IImagePlot>();
        plots.add(super.createPlot(Anatomy3D.getCanonicalAxial()));
        plots.add(super.createPlot(Anatomy3D.getCanonicalSagittal()));
        plots.add(super.createPlot(Anatomy3D.getCanonicalCoronal()));
        return plots;


    }

    public LinkedSliceController createSliceController() {
        if (sliceController == null) {
            sliceController = new LinkedSliceController(getView());
        }

        return sliceController;

    }

    public Dimension getPreferredSize() {
        switch (orientation) {
            case HORIZONTAL:
                return new Dimension(3 * 256, 256);
            case TRIANGULAR:
                return new Dimension(2 * 256, 2 * 256);
            case VERTICAL:
                return new Dimension(1 * 256, 3 * 256);
            default:
                return new Dimension(256, 256);
        }


    }

    public List<IImagePlot> layoutPlots() {
        plots = createPlots();
        getView().removeAll();
        switch (orientation) {
            case HORIZONTAL:
                BoxLayout layout1 = new BoxLayout(getView(), BoxLayout.X_AXIS);
                getView().setLayout(layout1);
                for (IImagePlot plot : plots) {
                    getView().add(plot.getComponent());
                }

                break;

            case TRIANGULAR:
                FormLayout layout2a = new FormLayout("p:grow(.85), p:grow(.15)", "p:g, 1dlu:g, p:g, 1dlu:g");
                layout2a.setColumnGroups(new int[][]{{1, 2}});
                getView().setLayout(layout2a);
                CellConstraints cc = new CellConstraints();
                getView().add(plots.get(0).getComponent(), cc.xywh(1, 1, 1, 4));
                getView().add(plots.get(1).getComponent(), cc.xywh(2, 1, 1, 2));
                getView().add(plots.get(2).getComponent(), cc.xywh(2, 3, 1, 2));
                break;

            case VERTICAL:
                BoxLayout layout3 = new BoxLayout(getView(), BoxLayout.Y_AXIS);
                getView().setLayout(layout3);
                for (IImagePlot plot : plots) {
                    getView().add(plot.getComponent());
                }

                break;


            default:
                throw new AssertionError();
        }

        return plots;

    }

    public IImagePlot whichPlot(Point p) {

        for (IImagePlot plot : plots) {
            Point point = SwingUtilities.convertPoint(getView(), p, plot.getComponent());

            boolean inplot = !((point.x < 0) || (point.y < 0) || (point.x > plot.getComponent().getWidth()) || (point.y > plot.getComponent().getHeight()));

            if (inplot) {
                return plot;
            }
        }

        return null;


    }
}
