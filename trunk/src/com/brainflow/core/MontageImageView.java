package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.core.annotations.CrosshairAnnotation;
import com.brainflow.core.annotations.SelectedPlotAnnotation;
import com.brainflow.display.ICrosshair;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 2, 2007
 * Time: 2:19:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class MontageImageView extends AbstractGriddedImageView {

    private AnatomicalVolume displayAnatomy;


    public MontageImageView(IImageDisplayModel imodel, AnatomicalVolume displayAnatomy) {
        super(imodel);
        this.displayAnatomy = displayAnatomy;
        layoutGrid();
        initView();
    }


    public AnatomicalVolume getDisplayAnatomy() {
        return displayAnatomy;
    }


    private void initView() {

        CrosshairAnnotation crosshairAnnotation = new CrosshairAnnotation(getCrosshair().getProperty());


        getPlotSelection().setSelectionIndex(0);

        setAnnotation(getSelectedPlot(), CrosshairAnnotation.ID, crosshairAnnotation);
        //setAnnotation(imagePlot, SelectedPlotAnnotation.ID, new SelectedPlotAnnotation(imagePlot));






        getCrosshair().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                ICrosshair cross = (ICrosshair) evt.getSource();
                AnatomicalPoint1D slice = cross.getLocation().getValue(MontageImageView.this.getSelectedPlot().getDisplayAnatomy().ZAXIS);
                getSelectedPlot().setSlice(slice);

            }
        });


    }

    //public void setDisplayAnatomy(AnatomicalVolume displayAnatomy) {
    //    this.displayAnatomy = displayAnatomy;
    //}

    public IImagePlot makePlot(int row, int column) {
        AxisRange xrange = getModel().getImageAxis(displayAnatomy.XAXIS).getRange();
        AxisRange yrange = getModel().getImageAxis(displayAnatomy.YAXIS).getRange();


        IImagePlot plot = new ComponentImagePlot(getModel(), displayAnatomy, xrange, yrange);
        plot.setName(displayAnatomy.XY_PLANE.getOrientation().toString() + row + ", " + column);

        CompositeImageProducer producer = new CompositeImageProducer(plot, getDisplayAnatomy());
        producer = new CompositeImageProducer(plot, getDisplayAnatomy());
        plot.setImageProducer(producer);
        plot.setSlice(getCrosshair().getProperty().getValue(displayAnatomy.ZAXIS));

        return plot;

    }


    public Dimension getPreferredSize() {
        return new Dimension(150*getNRows(), 150*getNCols());
    }
}
