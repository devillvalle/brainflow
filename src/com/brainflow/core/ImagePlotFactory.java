package com.brainflow.core;

import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.axis.AxisRange;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 30, 2008
 * Time: 12:41:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImagePlotFactory {

    private static ExecutorService threadService;

    public static IImagePlot createComponentPlot(IImageDisplayModel model, Anatomy3D displayAnatomy) {

        if (threadService == null) {
            threadService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        }

        
        AxisRange xrange = model.getImageAxis(displayAnatomy.XAXIS).getRange();
        AxisRange yrange = model.getImageAxis(displayAnatomy.YAXIS).getRange();

        IImagePlot plot = new ComponentImagePlot(model, displayAnatomy, xrange, yrange);
        plot.setName(displayAnatomy.XY_PLANE.getOrientation().toString());

        AnatomicalPoint1D slice = model.getImageAxis(displayAnatomy.ZAXIS).getRange().getCenter();
        IImageProducer producer = new CompositeImageProducer(plot, displayAnatomy, slice, threadService);
        plot.setImageProducer(producer);


        //plot.setSlice(getView().getCursorPos().getValue(displayAnatomy.ZAXIS));
        //plot.setScreenInterpolation(getView().getScreenInterpolation());
        return plot;


    }
}
