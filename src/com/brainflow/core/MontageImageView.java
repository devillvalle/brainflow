package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.axis.AxisRange;

import java.awt.*;

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
    }


    public AnatomicalVolume getDisplayAnatomy() {
        return displayAnatomy;
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
        return new Dimension(500,500);
    }
}
