package com.brainflow.core;


import com.brainflow.core.annotations.CrosshairAnnotation;
import com.brainflow.core.annotations.SelectedPlotAnnotation;
import com.brainflow.core.annotations.SliceAnnotation;
import com.brainflow.display.ICrosshair;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.space.Axis;
import com.jgoodies.binding.list.SelectionInList;

import javax.swing.*;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

    //private ImagePane ipane;

    private final static Logger log = Logger.getLogger(SimpleImageView.class.getName());

    private IImagePlot imagePlot = null;

    private Anatomy3D displayAnatomy = Anatomy3D.getCanonicalAxial();

    private SliceController sliceController = new SimpleSliceController();

    public SimpleImageView(ImageView source, Anatomy3D _displayAnatomy) {
        super(source.getModel());
        setDisplayAnatomy(_displayAnatomy);
        initLocal();

    }


    public SimpleImageView(IImageDisplayModel dset) {
        super(dset);
        initLocal();
    }


    public SimpleImageView(IImageDisplayModel dset, Anatomy3D _displayAnatomy) {
        super(dset);
        setDisplayAnatomy(_displayAnatomy);
        initLocal();
    }

    protected void setDisplayAnatomy(Anatomy3D _displayAnatomy) {
        displayAnatomy = _displayAnatomy;
    }

    public Anatomy3D getDisplayAnatomy() {
        return displayAnatomy;
    }

    private void initLocal() {
        Anatomy3D displayAnatomy = getDisplayAnatomy();
        AxisRange xrange = getModel().getImageAxis(displayAnatomy.XAXIS).getRange();
        AxisRange yrange = getModel().getImageAxis(displayAnatomy.YAXIS).getRange();


        imagePlot = new ComponentImagePlot(getModel(), displayAnatomy, xrange, yrange);
        imagePlot.setName(displayAnatomy.XY_PLANE.getOrientation().toString());

        IImageProducer producer = new CompositeImageProducer(imagePlot, getDisplayAnatomy());
        imagePlot.setImageProducer(producer);
        imagePlot.setSlice(getCrosshair().getProperty().getValue(displayAnatomy.ZAXIS));
        imagePlot.setScreenInterpolation(getScreenInterpolation());


        //ipane = new ImagePane(imagePlot);

        setLayout(new BorderLayout());
        add(imagePlot.getComponent(), BorderLayout.CENTER);
        getPlotSelection().setSelection(imagePlot);


        getCrosshair().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                ICrosshair cross = (ICrosshair) evt.getSource();
                AnatomicalPoint1D slice = cross.getLocation().getValue(SimpleImageView.this.getSelectedPlot().getDisplayAnatomy().ZAXIS);
                getSelectedPlot().setSlice(slice);

            }
        });

        initAnnotations();


    }


    private void initAnnotations() {
        CrosshairAnnotation crosshairAnnotation = new CrosshairAnnotation(getCrosshair().getProperty());
        setAnnotation(imagePlot, CrosshairAnnotation.ID, crosshairAnnotation);
        setAnnotation(imagePlot, SelectedPlotAnnotation.ID, new SelectedPlotAnnotation(this));
        setAnnotation(imagePlot, SelectedPlotAnnotation.ID, new SliceAnnotation());

    }


    public SliceController getSliceController() {
        return sliceController;
    }

    public RenderedImage captureImage() {
        BufferedImage img = new BufferedImage(imagePlot.getComponent().getWidth(), imagePlot.getComponent().getHeight(), BufferedImage.TYPE_INT_ARGB);
        imagePlot.getComponent().paint(img.createGraphics());
        return img;

    }


    public IImagePlot getSelectedPlot() {
        return imagePlot;
    }

    public SelectionInList getPlotSelection() {
        return new SelectionInList(new Object[]{imagePlot});
    }

    public List<IImagePlot> getPlots() {
        List<IImagePlot> lst = new ArrayList<IImagePlot>();
        lst.add(imagePlot);
        return lst;
    }


    public IImagePlot whichPlot(Point p) {
         Point point = SwingUtilities.convertPoint(this, p, imagePlot.getComponent());

        boolean inplot = !((point.x < 0) || (point.y < 0) || (point.x > getWidth()) || (point.y > getHeight()));
        if (inplot) {
            return imagePlot;
        } else {
            return null;
        }
    }


    public Dimension getPreferredSize() {
        return imagePlot.getComponent().getPreferredSize();
    }


    public String toString() {
        return "SimpleImageView -- " + this.getId() + getName();
    }


    class SimpleSliceController implements SliceController {

        private double pageStep = .12;


        public AnatomicalPoint1D getSlice() {
            return getCrosshair().getProperty().getValue(getSelectedPlot().getDisplayAnatomy().ZAXIS);
        }

        public void setSlice(AnatomicalPoint1D slice) {
            ICrosshair cross = getCrosshair().getProperty();
            AnatomicalPoint1D zslice = getSlice();
            if (!zslice.equals(slice)) {

                cross.setZValue(slice.getX());
                getSelectedPlot().setSlice(slice);
            }

        }

        public void nextSlice() {
            AnatomicalPoint1D slice = getSlice();
            ICrosshair cross = getCrosshair().getProperty();


            Axis axis = getViewport().getProperty().getBounds().findAxis(getSelectedPlot().getDisplayAnatomy().ZAXIS);
            ImageAxis iaxis = getModel().getImageAxis(axis);

            int sample = iaxis.nearestSample(slice);
            int nsample = sample + 1;
            if (nsample >= 0 && nsample < iaxis.getNumSamples()) {
                cross.setValue(iaxis.valueOf(nsample));
            }
        }

        public void previousSlice() {
            AnatomicalPoint1D slice = getSlice();
            ICrosshair cross = getCrosshair().getProperty();


            Axis axis = getViewport().getProperty().getBounds().findAxis(getSelectedPlot().getDisplayAnatomy().ZAXIS);
            ImageAxis iaxis = getModel().getImageAxis(axis);

            int sample = iaxis.nearestSample(slice);
            int nsample = sample - 1;
            if (nsample >= 0 && nsample < iaxis.getNumSamples()) {
                cross.setValue(iaxis.valueOf(nsample));
            }

        }

        public void pageBack() {
            AnatomicalPoint1D slice = getSlice();
            ICrosshair cross = getCrosshair().getProperty();


            Axis axis = getViewport().getProperty().getBounds().findAxis(getSelectedPlot().getDisplayAnatomy().ZAXIS);
            ImageAxis iaxis = getModel().getImageAxis(axis);

            int sample = iaxis.nearestSample(slice);
            double page = iaxis.getExtent() * pageStep;
            int jump = (int) (page / iaxis.getSpacing());

            // ensure we move at least one sample
            jump = Math.max(jump, 1);
            int nsample = sample - jump;

            if (nsample >= 0 && nsample < iaxis.getNumSamples()) {
                cross.setValue(iaxis.valueOf(nsample));
            }


        }

        public void pageForward() {
            AnatomicalPoint1D slice = getSlice();
            ICrosshair cross = getCrosshair().getProperty();


            Axis axis = getViewport().getProperty().getBounds().findAxis(getSelectedPlot().getDisplayAnatomy().ZAXIS);
            ImageAxis iaxis = getModel().getImageAxis(axis);


            int sample = iaxis.nearestSample(slice);

            double page = iaxis.getExtent() * pageStep;

            int jump = (int) (page / iaxis.getSpacing());

            // ensure we move at least one sample
            jump = Math.max(jump, 1);
            int nsample = sample + jump;


            if (nsample >= 0 && nsample < iaxis.getNumSamples()) {
                cross.setValue(iaxis.valueOf(nsample));
            }

        }
    }


}
