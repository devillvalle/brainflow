package com.brainflow.core;


import com.brainflow.core.annotations.AxisLabelAnnotation;
import com.brainflow.core.annotations.ColorBarAnnotation;
import com.brainflow.core.annotations.CrosshairAnnotation;
import com.brainflow.core.annotations.SelectedPlotAnnotation;
import com.brainflow.display.ICrosshair;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint2D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.axis.AxisRange;
import com.jgoodies.binding.list.SelectionInList;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import java.awt.*;
import java.awt.geom.Rectangle2D;
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


    public SimpleImageView(ImageView source, AnatomicalVolume _displayAnatomy) {
        super(source.getModel());
        setDisplayAnatomy(_displayAnatomy);
        initView();

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

        CrosshairAnnotation crosshairAnnotation = new CrosshairAnnotation(getCrosshair());
        //addAnnotation(new AxisLabelAnnotation());
        addAnnotation(crosshairAnnotation);
        //addAnnotation(new ColorBarAnnotation(getModel()));
        addAnnotation(new SelectedPlotAnnotation(imagePlot));

        getModel().addChangeListener(dirtListener);
        AnatomicalVolume displayAnatomy = getDisplayAnatomy();
        AxisRange xrange = getModel().getImageAxis(displayAnatomy.XAXIS).getRange();
        AxisRange yrange = getModel().getImageAxis(displayAnatomy.YAXIS).getRange();

        procurer = new DefaultImageProcurer(getModel(), displayAnatomy);
        procurer.setSlice(getCrosshair().getValue(displayAnatomy.ZAXIS));

        compositor = new DefaultImageCompositor();
        plotRenderer = new ImagePlotRenderer(compositor, procurer);

        imagePlot = new BasicImagePlot(displayAnatomy, xrange, yrange, plotRenderer);
        imagePlot.setName(displayAnatomy.XY_PLANE.getOrientation().toString());


        imagePlot.setAnnotations(getAnnotations());

        ipane = new ImagePane(imagePlot);
        scheduler = new ImagePlotPaintScheduler(ipane, procurer, compositor);

        setLayout(new BorderLayout());
        add(ipane, BorderLayout.CENTER);

        getPlotSelection().setSelectionIndex(0);

    }


    public RenderedImage captureImage() {
        return ipane.captureImage();
    }


    public SelectionInList getPlotSelection() {
        return new SelectionInList(new Object[]{imagePlot});
    }

    public IImagePlot getSelectedPlot() {
        return imagePlot;
    }

    public AnatomicalPoint3D getAnatomicalLocation(Component source, Point p) {

        // fix me this is ugly

        Point panePoint = SwingUtilities.convertPoint(source, p, ipane);

        AnatomicalPoint2D apoint = ipane.translateScreenToValue(panePoint);
        IImagePlot plot = ipane.getImagePlot();

        AnatomicalVolume displayAnatomy = getDisplayAnatomy();
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

            ICrosshair cross = getCrosshair();

            AnatomicalPoint1D xpt = cross.getValue(imagePlot.getDisplayAnatomy().XAXIS);
            AnatomicalPoint1D ypt = cross.getValue(imagePlot.getDisplayAnatomy().YAXIS);

            double percentX = (xpt.getX() - plot.getXAxisRange().getBeginning().getX()) / plot.getXAxisRange().getInterval();
            double percentY = (ypt.getX() - plot.getYAxisRange().getBeginning().getX()) / plot.getYAxisRange().getInterval();

            Rectangle2D plotArea = ipane.getImagePlot().getPlotArea();

            double screenX = (percentX * plotArea.getWidth()) + plotArea.getX();
            double screenY = (percentY * plotArea.getHeight()) + plotArea.getY();

            Point location = new Point((int) Math.round(screenX), (int) Math.round(screenY));
            return SwingUtilities.convertPoint(ipane, location, this);

        } else {
            throw new IllegalArgumentException("This View does not contain plot supplied as argument.");
        }


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
        return "SimpleImageView -- " + getModel().getName();
    }


}
