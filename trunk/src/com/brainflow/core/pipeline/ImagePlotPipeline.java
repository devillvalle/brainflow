package com.brainflow.core.pipeline;

import org.apache.commons.pipeline.Pipeline;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.IImagePlot;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.data.IImageData2D;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.Axis;

import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 1, 2007
 * Time: 5:25:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImagePlotPipeline extends Pipeline {

    public static final String MODEL_KEY = "model";

    public static final String SLICE_KEY = "slice";

    public static final String DISPLAY_ANATOMY_KEY = "displayAnatomy";

    public static final String USE_SLICE_CACHE = "use_slice_cache";

    private IImagePlot plot;

    private IImageDisplayModel model;


    private AnatomicalPoint1D slice;


    public ImagePlotPipeline(IImageDisplayModel _model, IImagePlot _plot) {
        super();
        model = _model;
        plot = _plot;
    }


    public IImagePlot getPlot() {
        return plot;
    }

    public void setPlot(IImagePlot plot) {
        this.plot = plot;
    }

    public IImageDisplayModel getModel() {
        return model;
    }

    public void setModel(IImageDisplayModel model) {
        this.model = model;
    }

    public AnatomicalVolume getDisplayAnatomy() {
        return plot.getDisplayAnatomy();
    }

    public AnatomicalPoint1D getSlice() {
        return slice;
    }

    public void setSlice(AnatomicalPoint1D slice) {
        this.slice = slice;
    }


    public static Rectangle2D getBounds(List<PipelineLayer> layers) {
        if (layers == null || layers.size() == 0) {
            return new Rectangle2D.Double(0, 0, 0, 0);
        }

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (PipelineLayer layer : layers) {
            IImageData2D d2 = (IImageData2D) layer.getLayer().getImageData();
            IImageSpace space = d2.getImageSpace();
            minX = Math.min(minX, space.getImageAxis(Axis.X_AXIS).getRange().getMinimum());
            minY = Math.min(minY, space.getImageAxis(Axis.Y_AXIS).getRange().getMinimum());
            maxX = Math.max(maxX, space.getImageAxis(Axis.X_AXIS).getRange().getMaximum());
            maxY = Math.max(maxY, space.getImageAxis(Axis.Y_AXIS).getRange().getMaximum());
        }

        Rectangle2D imageBounds = new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
        return imageBounds;
    }
}
