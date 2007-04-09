package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.core.annotations.CrosshairAnnotation;
import com.brainflow.core.annotations.SelectedPlotAnnotation;
import com.brainflow.display.ICrosshair;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
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

    private MontageSliceController sliceController;

    public MontageImageView(IImageDisplayModel imodel, AnatomicalVolume displayAnatomy) {
        super(imodel);
        this.displayAnatomy = displayAnatomy;

        sliceController = new MontageSliceController(getCrosshair().getProperty().getValue(displayAnatomy.ZAXIS));
        getCrosshair().addPropertyChangeListener(new CrosshairHandler());

        layoutGrid();
        initLocal();
    }

    public MontageImageView(IImageDisplayModel imodel, AnatomicalVolume displayAnatomy, int nrows, int ncols, double sliceGap) {
        super(imodel, nrows, ncols);
        this.displayAnatomy = displayAnatomy;

        sliceController = new MontageSliceController(getCrosshair().getProperty().getValue(displayAnatomy.ZAXIS), sliceGap);
        sliceController.sliceGap = sliceGap;
        getCrosshair().addPropertyChangeListener(new CrosshairHandler());

        layoutGrid();
        initLocal();
    }


    public AnatomicalVolume getDisplayAnatomy() {
        return displayAnatomy;
    }


    private void initLocal() {

        CrosshairAnnotation crosshairAnnotation = new CrosshairAnnotation(getCrosshair().getProperty());

        SelectedPlotAnnotation plotAnnotation = new SelectedPlotAnnotation(this);
        for (IImagePlot plot : getPlots()) {
            setAnnotation(plot, SelectedPlotAnnotation.ID, plotAnnotation);
            setAnnotation(plot, CrosshairAnnotation.ID, crosshairAnnotation);

        }


    }

    public SliceController getSliceController() {
        return sliceController;
    }

    private void updateSlices() {
        List<IImagePlot> plotList = getPlots();

        int i = 0;
        for (IImagePlot plot : plotList) {
            AnatomicalPoint1D slice = sliceController.getSliceForPlot(i);
            plot.setSlice(slice);
            i++;
        }

    }

    //public void setDisplayAnatomy(AnatomicalVolume displayAnatomy) {
    //    this.displayAnatomy = displayAnatomy;
    //}

    @Override
    protected IImagePlot makePlot(int index, int row, int column) {
        AxisRange xrange = getModel().getImageAxis(displayAnatomy.XAXIS).getRange();
        AxisRange yrange = getModel().getImageAxis(displayAnatomy.YAXIS).getRange();


        IImagePlot plot = new ComponentImagePlot(getModel(), displayAnatomy, xrange, yrange);
        plot.setName(displayAnatomy.XY_PLANE.getOrientation().toString() + row + ", " + column);

        CompositeImageProducer producer = new CompositeImageProducer(plot, getDisplayAnatomy());
        producer = new CompositeImageProducer(plot, getDisplayAnatomy());
        plot.setImageProducer(producer);


        AnatomicalPoint1D nextSlice = sliceController.getSliceForPlot(index);

        plot.setSlice(nextSlice);

        return plot;

    }


    public Dimension getPreferredSize() {
        return new Dimension(150 * getNRows(), 150 * getNCols());
    }

    class CrosshairHandler implements PropertyChangeListener {


        public void propertyChange(PropertyChangeEvent evt) {
            ICrosshair cross = (ICrosshair) evt.getSource();
            AnatomicalPoint1D slice = cross.getLocation().getValue(MontageImageView.this.getSelectedPlot().getDisplayAnatomy().ZAXIS);
            double val = sliceController.nearestSlice(slice);
            if (val < .1) {
                int index = sliceController.whichPlot(slice, .11);
                //getPlotSelection().setSelectionIndex(index);
                for (IImagePlot plot : getPlots()) {
                    // can we just call repaint rather than looping?
                    plot.getComponent().repaint();
                }
            } else {
                sliceController.setSlice(slice);
            }

        }
    }

    class MontageSliceController implements SliceController {

        private AnatomicalPoint1D sentinel;

        private double sliceGap = 4;

        private AxisRange sliceRange;


        public MontageSliceController(AnatomicalPoint1D sentinel) {
            this.sentinel = sentinel;
            sliceGap = getModel().getImageSpace().getImageAxis(displayAnatomy.ZAXIS, true).getSpacing();
            sliceRange = new AxisRange(sentinel.getAnatomy(), sentinel.getX(), sentinel.getX() + getNumPlots() * sliceGap);
        }


        public MontageSliceController(AnatomicalPoint1D sentinel, double sliceGap) {
            this.sentinel = sentinel;
            this.sliceGap = sliceGap;
            sliceRange = new AxisRange(sentinel.getAnatomy(), sentinel.getX(), sentinel.getX() + getNumPlots() * sliceGap);
        }


        public double getSliceGap() {
            return sliceGap;
        }


        public AnatomicalPoint1D getSlice() {
            return sentinel;
        }


        public AxisRange getSliceRange() {
            return sliceRange;
        }

        public AnatomicalPoint1D getSliceForPlot(int plotIndex) {
            return new AnatomicalPoint1D(sentinel.getAnatomy(), sentinel.getX() + plotIndex * sliceGap);
        }

        public double nearestSlice(AnatomicalPoint1D slice) {
            double minDist = Double.MAX_VALUE;
            for (int i = 0; i < getNumPlots(); i++) {
                AnatomicalPoint1D pt = getSliceForPlot(i);
                minDist = Math.min(Math.abs(pt.getX() - slice.getX()), minDist);
            }

            return minDist;
        }

        public int whichPlot(AnatomicalPoint1D slice, double tolerance) {
            int index = -1;
            double minDist = Double.MAX_VALUE;
            for (int i = 0; i < getNumPlots(); i++) {
                AnatomicalPoint1D pt = getSliceForPlot(i);
                double dist = Math.abs(pt.getX() - slice.getX());
                if (dist < minDist) {
                    index = i;
                    minDist = dist;
                }
            }

            if (minDist < tolerance) {
                return index;
            } else {
                return -1;
            }


        }


        public void setSlice(AnatomicalPoint1D slice) {

            AxisRange range = getViewport().getProperty().getRange(getDisplayAnatomy().ZAXIS);
            if (slice.getAnatomy() != range.getAnatomicalAxis()) {
                throw new IllegalArgumentException("illegal axis for slice argument : " + slice.getAnatomy() +
                        " -- axis should be : " + range.getAnatomicalAxis());
            }

            sentinel = slice;
            sliceRange = new AxisRange(sentinel.getAnatomy(), sentinel.getX(), sentinel.getX() + getNumPlots() * sliceGap);
            updateSlices();
        }

        public void nextSlice() {
            setSlice(new AnatomicalPoint1D(sentinel.getAnatomy(), sentinel.getX() + sliceGap));
        }

        public void previousSlice() {
            setSlice(new AnatomicalPoint1D(sentinel.getAnatomy(), sentinel.getX() - sliceGap));
        }
    }
}
