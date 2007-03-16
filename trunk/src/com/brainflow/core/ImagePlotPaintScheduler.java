package com.brainflow.core;

import com.brainflow.display.Viewport3D;
import com.brainflow.display.ICrosshair;
import com.brainflow.image.anatomy.AnatomicalPoint1D;

import javax.swing.*;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 29, 2006
 * Time: 11:56:53 AM
 * To change this template use File | Settings | File Templates.
 */


public class ImagePlotPaintScheduler {

    private static final Logger log = Logger.getLogger(ImagePlotPaintScheduler.class.getName());

    private AbstractImageProcurer procurer;
    private IImageCompositor compositor;
    private ImagePane ipane;
    private IImagePlot plot;

    private ScheduledThreadPoolExecutor executor;

    public ImagePlotPaintScheduler(ImagePane _ipane, AbstractImageProcurer _procurer, IImageCompositor _compositor) {
        procurer = _procurer;
        compositor = _compositor;
        ipane = _ipane;
        plot = ipane.getImagePlot();

        executor = new ScheduledThreadPoolExecutor(2, new RejectedExecutionHandler() {

            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

            }
        });
    }


    public void viewportChanged(Viewport3D viewport) {
        plot.updateAxis(viewport.getRange(viewport.getXAxis()));
        plot.updateAxis(viewport.getRange(viewport.getYAxis()));
        plot.updateAxis(viewport.getRange(viewport.getZAxis()));
        ipane.repaint();

    }

    public void annotationChanged() {
         ipane.repaint();

    }

    public void crossHairChanged(ICrosshair cross) {
        executor.submit(new ProcureTask(cross.getValue(plot.getDisplayAnatomy().ZAXIS)));

    }

    public void layerChanged(DisplayChangeType changeType) {

        log.info("Display Action : " + changeType);


        if (changeType == DisplayChangeType.STRUCTURE_CHANGE) {
            procurer.reset();
            compositor.setDirty();
            executor.submit(new ProcureTask(procurer.getSlice()));

        } else if (changeType == DisplayChangeType.IMAGE_FILTER_CHANGE) {
            procurer.reset();
            compositor.setDirty();
            executor.submit(new ProcureTask(procurer.getSlice()));
        } else if (changeType == DisplayChangeType.COLOR_MAP_CHANGE) {
            procurer.reset();
            compositor.setDirty();
            executor.submit(new ProcureTask(procurer.getSlice()));

        } else if (changeType == DisplayChangeType.RESAMPLE_CHANGE) {
            executor.submit(new ComposeTask());
        } else if (changeType == DisplayChangeType.COMPOSITION_CHANGE) {
            executor.submit(new ComposeTask());
        } else {
            procurer.reset();
            compositor.setDirty();
            executor.submit(new ProcureTask(procurer.getSlice()));

        }

    }


    class ComposeTask extends SwingWorker {

        public ComposeTask() {
        }

        protected void done() {
            ipane.repaint();
        }

        protected Object doInBackground() {
            compositor.setDirty();
            compositor.compose();
            return "Finished";
        }


    }

    class ProcureTask extends SwingWorker {

        private AnatomicalPoint1D slice;

        public ProcureTask(AnatomicalPoint1D _slice) {
            slice = _slice;
        }


        protected void done() {
            ipane.repaint();
        }

        public Object doInBackground() {
            procurer.setSlice(slice);
            compositor.setImageList(procurer.procure());
            compositor.compose();

            return "Finished";
        }
    }


}
