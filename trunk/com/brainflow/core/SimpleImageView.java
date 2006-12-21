package com.brainflow.core;


import com.brainflow.colormap.ColorTable;
import com.brainflow.core.annotations.CrosshairAnnotation;
import com.brainflow.display.ImageLayerParameters;
import com.brainflow.display.InterpolationHint;
import com.brainflow.display.InterpolationProperty;
import com.brainflow.image.anatomy.AnatomicalPoint2D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.AnalyzeIO;
import org.apache.commons.lang.math.DoubleRange;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
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

    private AnatomicalVolume displayAnatomy = AnatomicalVolume.getCanonicalAxial();

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
        displayAnatomy = _displayAnatomy;
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

        imagePlot.addAnnotation(crosshairAnnotation);

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


    public static void main(String[] args) {
        try {
            IImageData data = AnalyzeIO.readAnalyzeImage("C:/code/icbm/icbm452_atlas_probability_white.hdr");
            IImageDisplayModel model = new ImageDisplayModel("model-1");
            double min = data.getMinValue();
            double max = data.getMaxValue();
            model.addLayer(new ImageLayer(data, new ImageLayerParameters(ColorTable.SPECTRUM, new DoubleRange(min, max))));
            model.getImageLayer(0).getImageLayerParameters().getScreenInterpolation().setParameter(new InterpolationProperty(InterpolationHint.CUBIC));
            model.getImageLayer(0).getImageLayerParameters().getResampleInterpolation().setParameter(new InterpolationProperty(InterpolationHint.CUBIC));


            SimpleImageView view = new SimpleImageView(model);
            ImagePlotRenderer renderer = view.getPlotRenderer();

            //JFrame frame = new JFrame("image test");
            //frame.add(view, BorderLayout.CENTER);
            //frame.setSize(300,300);
            //frame.setVisible(true);


            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 200; i++) {
                System.out.println(i);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gd = ge.getDefaultScreenDevice();
                GraphicsConfiguration gf = gd.getDefaultConfiguration();
                Rectangle2D frameBounds = new Rectangle2D.Double(0, 0, 300 + i, 300 + i);

                BufferedImage sourceImage = gf.createCompatibleImage((int) frameBounds.getWidth(), (int) frameBounds.getHeight());
                Graphics2D g2 = sourceImage.createGraphics();

                renderer.paint(g2, frameBounds, view.getPlots().get(0));

            }

            long endTime = System.currentTimeMillis();
            System.out.println("time = " + (endTime - startTime));
            System.out.println("avg time = " + (endTime - startTime) / 200);


        } catch (BrainflowException e) {
            e.printStackTrace();
        }


    }


}
