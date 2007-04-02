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

    private AnatomicalVolume displayAnatomy = AnatomicalVolume.getCanonicalAxial();


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

    protected void setDisplayAnatomy(AnatomicalVolume _displayAnatomy) {
        displayAnatomy = _displayAnatomy;
    }

    public AnatomicalVolume getDisplayAnatomy() {
        return displayAnatomy;
    }

    private void initView() {

        CrosshairAnnotation crosshairAnnotation = new CrosshairAnnotation(getCrosshair().getProperty());

        AnatomicalVolume displayAnatomy = getDisplayAnatomy();
        AxisRange xrange = getModel().getImageAxis(displayAnatomy.XAXIS).getRange();
        AxisRange yrange = getModel().getImageAxis(displayAnatomy.YAXIS).getRange();


        imagePlot = new ComponentImagePlot(getModel(), displayAnatomy, xrange, yrange);
        imagePlot.setName(displayAnatomy.XY_PLANE.getOrientation().toString());
        producer = new CompositeImageProducer(imagePlot, getDisplayAnatomy());
        imagePlot.setImageProducer(producer);
        imagePlot.setSlice(getCrosshair().getProperty().getValue(displayAnatomy.ZAXIS));
        

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
                getSelectedPlot().setSlice(slice);

            }
        });



    }




    public RenderedImage captureImage() {
        return ipane.captureImage();
    }


    public IImagePlot getSelectedPlot() {
        return imagePlot;
    }

    public SelectionInList getPlotSelection() {
        return new SelectionInList(new Object[] { imagePlot } );
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


  

    public String toString() {
        return "SimpleImageView -- " + this.getId() + getName();
    }


}
