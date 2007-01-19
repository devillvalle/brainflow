package com.brainflow.core;


import com.brainflow.core.annotations.AxisLabelAnnotation;
import com.brainflow.core.annotations.CrosshairAnnotation;
import com.brainflow.image.anatomy.AnatomicalPoint2D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.axis.AxisRange;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 21, 2004
 * Time: 7:24:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleImageView extends ImageView {

    private ImagePane ipane;

    private IImageCompositor compositor;

    private AbstractImageProcurer procurer;

    private ImagePlotRenderer plotRenderer;

    private ChangeListener dirtListener = new DirtListener();

    private final static Logger log = Logger.getLogger(SimpleImageView.class.getName());

    private IImagePlot imagePlot = null;

    private ImagePlotPaintScheduler scheduler;

    private CrosshairAnnotation crosshairAnnotation;

    public SimpleImageView() {
    }


    public SimpleImageView(IImageDisplayModel dset) {
        super(dset);
        initView();
    }

    public void scheduleRepaint(DisplayChangeEvent e) {
        scheduler.displayChanged(e);

    }

    public SimpleImageView(IImageDisplayModel dset, AnatomicalVolume _displayAnatomy) {
        super(dset);
        setDisplayAnatomy(_displayAnatomy);
        initView();
    }

    private void initView() {

        getImageDisplayModel().addChangeListener(dirtListener);

        AxisRange xrange = getImageDisplayModel().getImageAxis(displayAnatomy.XAXIS).getRange();
        AxisRange yrange = getImageDisplayModel().getImageAxis(displayAnatomy.YAXIS).getRange();

        procurer = new DefaultImageProcurer(getImageDisplayModel(), displayAnatomy);
        procurer.setSlice(getCrosshair().getValue(displayAnatomy.ZAXIS));

        compositor = new DefaultImageCompositor();
        plotRenderer = new ImagePlotRenderer(compositor, procurer);

        imagePlot = new BasicImagePlot(displayAnatomy, xrange, yrange, plotRenderer);

        crosshairAnnotation = new CrosshairAnnotation(getImageDisplayModel().getDisplayParameters().getCrosshair());


        addAnnotation(new AxisLabelAnnotation());

        addAnnotation(crosshairAnnotation);

        imagePlot.setAnnotations(getAnnotations());

        ipane = new ImagePane(imagePlot);
        scheduler = new ImagePlotPaintScheduler(ipane, procurer, compositor);

        setLayout(new BorderLayout());
        add(ipane, BorderLayout.CENTER);

    }


    public RenderedImage captureImage() {
        return ipane.captureImage();
    }

    public AnatomicalPoint3D getAnatomicalLocation(Component source, Point p) {

        // fix me this is ugly

        Point panePoint = SwingUtilities.convertPoint(source, p, ipane);

        AnatomicalPoint2D apoint = ipane.translateScreenToValue(panePoint);
        IImagePlot plot = ipane.getImagePlot();

        AnatomicalPoint3D ap3d = new AnatomicalPoint3D(
                AnatomicalVolume.matchAnatomy(
                        plot.getXAxisRange().getAnatomicalAxis(),
                        plot.getYAxisRange().getAnatomicalAxis(),
                        plot.getDisplayAnatomy().ZAXIS),
                apoint.getX(), apoint.getY(),
                getCrosshair().getValue(displayAnatomy.ZAXIS).getX());


        return ap3d;
    }


    public Point getCrosshairLocation(IImagePlot plot) {
        if (ipane.getImagePlot() == plot) {
            return SwingUtilities.convertPoint(ipane, crosshairAnnotation.getLocation(), this);
        } else {
            return null;
            //throw new IllegalArgumentException("This View does not contain plot supplied as argument.");
        }


    }

    private Point convertToPlotPoint(Component source, int x, int y) {
        Point p1 = SwingUtilities.convertPoint(source, x, y, ipane);
        Component c = ipane.getComponentAt(p1);
        return SwingUtilities.convertPoint(ipane, p1, c);
    }

    public ImagePlotRenderer getPlotRenderer() {
        return plotRenderer;
    }

    public List<IImagePlot> getPlots() {
        List<IImagePlot> lst = new ArrayList<IImagePlot>();
        lst.add(imagePlot);
        return lst;
    }


    public IImagePlot whichPlot(Point p) {
        if (ipane.pointInPlot(this, p)) {
            return imagePlot;
        } else {
            return null;
        }
    }


    public Dimension getPreferredSize() {
        return ipane.getPreferredSize();
    }

    protected void listChanged(ListDataEvent e) {
        procurer.reset();
    }


    public String toString() {
        return "SimpleImageView -- " + getImageDisplayModel().getName();
    }


}
