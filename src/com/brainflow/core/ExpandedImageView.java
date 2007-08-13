package com.brainflow.core;

import com.brainflow.core.annotations.CrosshairAnnotation;
import com.brainflow.core.annotations.SelectedPlotAnnotation;
import com.brainflow.display.ICrosshair;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.space.Axis;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 21, 2007
 * Time: 5:43:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExpandedImageView extends AbstractGriddedImageView {


    private Anatomy3D displayAnatomy;

    private SliceController sliceController = new SimpleSliceController();

    public ExpandedImageView(IImageDisplayModel imodel) {
        super(imodel);
        displayAnatomy = Anatomy3D.getCanonicalAxial();
        layoutGrid();
        initLocal();

    }

    public ExpandedImageView(IImageDisplayModel imodel, Anatomy3D displayAnatomy) {
        super(imodel);
        this.displayAnatomy = displayAnatomy;
        layoutGrid();
        initLocal();

    }


    public Anatomy3D getDisplayAnatomy() {
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


    @Override
    protected IImagePlot makePlot(int index, int row, int column) {
        AxisRange xrange = getModel().getImageAxis(displayAnatomy.XAXIS).getRange();
        AxisRange yrange = getModel().getImageAxis(displayAnatomy.YAXIS).getRange();

        IImageDisplayModel subModel = new ImageDisplayModel("layer " + index);
        subModel.addLayer(getModel().getLayer(index));

        IImagePlot plot = new ComponentImagePlot(subModel, displayAnatomy, xrange, yrange);
        plot.setName(displayAnatomy.XY_PLANE.getOrientation().toString() + row + ", " + column);

        CompositeImageProducer producer = new CompositeImageProducer(plot, getDisplayAnatomy());
        producer = new CompositeImageProducer(plot, getDisplayAnatomy());
        plot.setImageProducer(producer);


        AnatomicalPoint1D nextSlice = sliceController.getSlice();

        plot.setSlice(nextSlice);

        return plot;

    }


    public SliceController getSliceController() {
        return sliceController;
    }

    public int getNRows() {
        return 1;
    }

    public int getNCols() {
        return getModel().getNumLayers();
    }

    public Dimension getPreferredSize() {
        return new Dimension(256 * getNCols(), 256 * getNRows());
    }


    class SimpleSliceController implements SliceController {

        AnatomicalPoint1D slice;

        public SimpleSliceController() {
        }


        public AnatomicalPoint1D getSlice() {

            return getCrosshair().getProperty().getValue(getDisplayAnatomy().ZAXIS);
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
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void pageForward() {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }


}
