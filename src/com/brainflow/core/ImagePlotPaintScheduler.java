package com.brainflow.core;

import com.brainflow.display.Viewport3D;
import com.brainflow.display.ICrosshair;
import com.brainflow.image.anatomy.AnatomicalPoint1D;

import javax.swing.*;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 29, 2006
 * Time: 11:56:53 AM
 * To change this template use File | Settings | File Templates.
 */


public class ImagePlotPaintScheduler {

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
                System.out.println("task was rejected! " + r);
            }
        });
    }


    public void displayChanged(DisplayChangeEvent event) {

        System.out.println("DISPLAY ACTION: " + event.getDisplayAction());
        if (event.getDisplayAction() == DisplayAction.SLICE_CHANGED) {

            ICrosshair cross = (ICrosshair) event.getDisplayParameter().getProperty();
            executor.submit(new ProcureTask(cross.getValue(plot.getDisplayAnatomy().ZAXIS)));

        } else if (event.getDisplayAction() == DisplayAction.DATA_CHANGED) {
            procurer.reset();
            compositor.setDirty();
            executor.submit(new ProcureTask(procurer.getSlice()));

        } else if (event.getDisplayAction() == DisplayAction.FILTER_LAYER_CHANGED) {
            procurer.reset();
            compositor.setDirty();
            executor.submit(new ProcureTask(procurer.getSlice()));
        } else if (event.getDisplayAction() == DisplayAction.COLOR_MAP_CHANGED) {
            procurer.reset();
            compositor.setDirty();
            executor.submit(new ProcureTask(procurer.getSlice()));

        } else if (event.getDisplayAction() == DisplayAction.INTERPOLATION_CHANGED) {
            executor.submit(new ComposeTask());
        } else if (event.getDisplayAction() == DisplayAction.RECOMPOSE) {
            executor.submit(new ComposeTask());
        } else if (event.getDisplayAction() == DisplayAction.VIEWPORT_CHANGED) {
            Viewport3D viewport = (Viewport3D) event.getDisplayParameter().getProperty();
            plot.updateAxis(viewport.getRange(viewport.getXAxis()));
            plot.updateAxis(viewport.getRange(viewport.getYAxis()));
            plot.updateAxis(viewport.getRange(viewport.getZAxis()));
            ipane.repaint();
        } else if (event.getDisplayAction() == DisplayAction.ANNOTATION_CHANGED) {
            ipane.repaint();
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
