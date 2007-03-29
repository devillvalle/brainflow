package com.brainflow.core;


import com.brainflow.core.annotations.CrosshairAnnotation;
import com.brainflow.core.annotations.SelectedPlotAnnotation;
import com.brainflow.core.annotations.IAnnotation;
import com.brainflow.display.ICrosshair;
import com.brainflow.display.Viewport3D;
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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;


/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 21, 2004
 * Time: 7:24:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleImageView extends ImageView {

    private ImagePane ipane;

    private final static Logger log = Logger.getLogger(SimpleImageView.class.getName());

    private IImagePlot imagePlot = null;

    private IImageProducer producer = null;


    public SimpleImageView(ImageView source, AnatomicalVolume _displayAnatomy) {
        super(source.getModel());
        setDisplayAnatomy(_displayAnatomy);
        initView();

    }

   

    public SimpleImageView(IImageDisplayModel dset) {
        super(dset);
        initView();
    }


    public SimpleImageView(IImageDisplayModel dset, AnatomicalVolume _displayAnatomy) {
        super(dset);
        setDisplayAnatomy(_displayAnatomy);
        initView();
    }

    private void initView() {

        CrosshairAnnotation crosshairAnnotation = new CrosshairAnnotation(getCrosshair().getProperty());


        AnatomicalVolume displayAnatomy = getDisplayAnatomy();
        AxisRange xrange = getModel().getImageAxis(displayAnatomy.XAXIS).getRange();
        AxisRange yrange = getModel().getImageAxis(displayAnatomy.YAXIS).getRange();

        producer = new CompositeImageProducer(imagePlot,getModel(), getDisplayAnatomy());

        imagePlot = new ComponentImagePlot(getModel(), producer, displayAnatomy, xrange, yrange);
        imagePlot.setName(displayAnatomy.XY_PLANE.getOrientation().toString());


        setAnnotation(imagePlot, CrosshairAnnotation.ID, crosshairAnnotation);
        setAnnotation(imagePlot, SelectedPlotAnnotation.ID, new SelectedPlotAnnotation(imagePlot));
        


        ipane = new ImagePane(imagePlot);

        setLayout(new BorderLayout());
        add(ipane, BorderLayout.CENTER);
        getPlotSelection().setSelectionIndex(0);

        getCrosshair().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                ICrosshair cross = (ICrosshair)evt.getSource();
                AnatomicalPoint1D slice = cross.getLocation().getValue(SimpleImageView.this.getSelectedPlot().getDisplayAnatomy().ZAXIS);
                if (!producer.getSlice().equals(slice)) {
                    producer.setSlice(slice);
                    producer.updateImage(new ViewChangeEvent(SimpleImageView.this, DisplayChangeType.SLICE_CHANGE));
                
                }
                
                imagePlot.getComponent().repaint();
            }
        });

        getViewport().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                Viewport3D viewport = (Viewport3D)evt.getSource();
                imagePlot.setXAxisRange(viewport.getRange(imagePlot.getDisplayAnatomy().XAXIS));
                imagePlot.setYAxisRange(viewport.getRange(imagePlot.getDisplayAnatomy().YAXIS));
                producer.updateImage(new ViewChangeEvent(SimpleImageView.this, DisplayChangeType.VIEWPORT_CHANGE));
                imagePlot.getComponent().repaint();
            }
        });

    }


    public void clearAnnotations() {
        imagePlot.clearAnnotations();
    }

    public IAnnotation getAnnotation(IImagePlot plot, String name) {
        if (plot != imagePlot) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }

        return imagePlot.getAnnotation(name);
    }

    public void removeAnnotation(IImagePlot plot, String name) {
        if (plot != imagePlot) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }


        imagePlot.removeAnnotation(name);

    }

    public void setAnnotation(IImagePlot plot, String name, IAnnotation annotation) {
        if (plot != imagePlot) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }

        imagePlot.setAnnotation(name, annotation);
       

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
                getCrosshair().getProperty().getValue(displayAnatomy.ZAXIS).getX());


        return ap3d;
    }


    public Point getCrosshairLocation(IImagePlot plot) {
        if (ipane.getImagePlot() == plot) {

            ICrosshair cross = getCrosshair().getProperty();

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


    public void intervalAdded(ListDataEvent e) {
        producer.updateImage(new ViewChangeEvent(this, DisplayChangeType.STRUCTURE_CHANGE));
    }

    public void intervalRemoved(ListDataEvent e) {
       producer.updateImage(new ViewChangeEvent(this, DisplayChangeType.STRUCTURE_CHANGE));
    }

    public void contentsChanged(ListDataEvent e) {
        producer.updateImage(new ViewChangeEvent(this, DisplayChangeType.STRUCTURE_CHANGE));
    }

    public String toString() {
        return "SimpleImageView -- " + this.getId() + getName();
    }


}
