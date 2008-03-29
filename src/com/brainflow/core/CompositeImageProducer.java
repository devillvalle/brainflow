package com.brainflow.core;

import com.brainflow.core.rendering.*;
import com.brainflow.display.InterpolationType;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.axis.AxisRange;
import org.apache.commons.pipeline.Feeder;
import org.apache.commons.pipeline.driver.SynchronousStageDriverFactory;
import org.apache.commons.pipeline.validation.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 25, 2007
 * Time: 4:36:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompositeImageProducer extends AbstractImageProducer {

    private IImagePlot plot;

    private ImagePlotPipeline pipeline;

    private ImageLayerListener layerListener;


    private ImageProcessingStage gatherRenderersStage;

    private ImageProcessingStage renderLayersStage;

    //private ImageProcessingStage renderCoordinatesStage;

    private ImageProcessingStage cropImageStage;

    private ImageProcessingStage resizeImageStage;


    private TerminalFeeder terminal = new TerminalFeeder();


    public CompositeImageProducer(IImagePlot plot, Anatomy3D displayAnatomy) {
        this(plot, displayAnatomy, plot.getModel().getImageAxis(displayAnatomy.ZAXIS).getRange().getCenter());

    }

    public CompositeImageProducer(IImagePlot plot,
                                  Anatomy3D displayAnatomy, AnatomicalPoint1D slice) {
        this.plot = plot;
        setDisplayAnatomy(displayAnatomy);

        initPipeline();

        setSlice(slice);
        layerListener = new PipelineLayerListener();
        getModel().addImageLayerListener(layerListener);
        initPipeline();


    }

    public void setScreenInterpolation(InterpolationType type) {
        super.setScreenInterpolation(type);
        pipeline.clearPath(resizeImageStage);
    }


    public void setScreenSize(Rectangle screenSize) {
        super.setScreenSize(screenSize);

        pipeline.clearPath(resizeImageStage);
    }

    public void reset() {
        pipeline.clearPath(gatherRenderersStage);
        //getPlot().getComponent().repaint();
    }

    public void setSlice(AnatomicalPoint1D slice) {
        super.setSlice(slice);

        pipeline.clearPath(gatherRenderersStage);
    }

    public void setPlot(IImagePlot plot) {
        getModel().removeImageLayerListener(layerListener);
        this.plot = plot;
        getModel().addImageLayerListener(layerListener);

        initPipeline();
    }

    @Override
    public void setXAxis(AxisRange xaxis) {
        super.setXAxis(xaxis);
        pipeline.clearPath(cropImageStage);
    }

    @Override
    public void setYAxis(AxisRange yaxis) {
        super.setYAxis(yaxis);
        pipeline.clearPath(cropImageStage);
    }


    public IImageDisplayModel getModel() {
        return plot.getModel();
    }

    public IImagePlot getPlot() {
        return plot;
    }

    private void initPipeline() {
        pipeline = new ImagePlotPipeline(getPlot());
        try {
            gatherRenderersStage = new GatherRenderersStage();
            pipeline.addStage(gatherRenderersStage, new SynchronousStageDriverFactory());


            renderLayersStage = new RenderLayersStage();
            pipeline.addStage(renderLayersStage, new SynchronousStageDriverFactory());


            cropImageStage = new CropImageStage();
            pipeline.addStage(cropImageStage, new SynchronousStageDriverFactory());

            resizeImageStage = new ResizeImageStage();
            pipeline.addStage(resizeImageStage, new SynchronousStageDriverFactory());

            pipeline.getSourceFeeder().feed(getModel());
            pipeline.setTerminalFeeder(terminal);
        }
        catch (ValidationException e) {
            // can'three really handle this exception, so throw uncheckedexception
            throw new RuntimeException(e);
        }


    }

    /*public void updateImage(DisplayChangeEvent event) {

     switch (event.getChangeType()) {
         case SLICE_CHANGE:
             ferry.clearAll();
             ferry.setSlice(getSlice());
             break;
         case COLOR_MAP_CHANGE:
             assert event.getLayerLindex() >= 0;
             ferry.clearColoredImage(event.getLayerLindex());
             break;
         case COMPOSITION_CHANGE:
             ferry.clearComposition();
             break;
         case IMAGE_FILTER_CHANGE:
             // do nothing for now
             break;
         case RESAMPLE_CHANGE:
             assert event.getLayerLindex() >= 0;
             ferry.clearResampledImage(event.getLayerLindex());
             break;
         case RESET_CHANGE:
             ferry.clearAll();
             break;
         case SCREEN_SIZE_CHANGE:
             ferry.clearResizedImage();
             break;
         case STRUCTURE_CHANGE:
             ferry.clearAll();
             break;

         case VIEWPORT_CHANGE:
             ferry.clearCroppedImage();
             break;
         case THRESHOLD_CHANGED:
             ferry.clearThresholdedImage(event.getLayerLindex());
             break;
         case ANNOTATION_CHANGE:
             // do nothing
             break;
     }


 }   */

    public synchronized BufferedImage render() {
        pipeline.getSourceFeeder().feed(getModel());
        pipeline.run();
        return terminal.getImage();

    }

    public BufferedImage getImage() {
        // does this spawn a new thread?
        // could be submitted to thread pool?

        if (terminal.getImage() == null) {
            render();
        }

        return terminal.getImage();
    }

    class TaskQueue<T> {

        private ExecutorService service;

        private FutureTask<T> currentTask;

        private FutureTask<T> nextTask;


        public TaskQueue() {
            service = Executors.newFixedThreadPool(1);

        }

        private void submitNext() {
            if (nextTask != null) {
                System.out.println("submitting next");
                service.submit(nextTask);
            }
        }

        public void submit(Callable<T> task) {

            FutureTask<T> ft = new FutureTask<T>(task) {
                protected void done() {
                    System.out.println("done!");
                    plot.getComponent().repaint();
                    if (nextTask == this) {
                        System.out.println("this is next task, setting to null");
                        nextTask = null;
                    } else {
                        submitNext();
                    }


                }
            };

            //execute immediately or add to queue
            if (currentTask != null && !currentTask.isDone()) {
                System.out.println("busy ... queing task");
                nextTask = ft;
            } else {
                System.out.println("not busy ... submitting task");
                currentTask = ft;
                service.submit(ft);

            }

            


            
        }
    }

    

    private Callable<BufferedImage> createRenderTask() {
        return new Callable<BufferedImage>() {
            public BufferedImage call() throws Exception {              
                return render();

            }

        };



    }


    class TerminalFeeder implements Feeder {

        BufferedImage finalImage;


        public void feed(Object obj) {
            finalImage = (BufferedImage) obj;
        }

        public BufferedImage getImage() {
            return finalImage;
        }
    }

    private final TaskQueue<BufferedImage> renderQueue = new TaskQueue<BufferedImage>();

    protected void finalize() throws Throwable {
        System.out.println("garbage collecting " + this);
        getModel().removeImageLayerListener(layerListener);

    }

    class PipelineLayerListener implements ImageLayerListener {

        public void thresholdChanged(ImageLayerEvent event) {
            //todo make this a bit more intelligent. "clearPath" flushes the renderers
            pipeline.clearPath(gatherRenderersStage);
            renderQueue.submit(createRenderTask());
            //plot.getComponent().repaint();
        }

        public void smoothingChanged(ImageLayerEvent event) {
            pipeline.clearPath(gatherRenderersStage);
            renderQueue.submit(createRenderTask());

        }

        public void colorMapChanged(ImageLayerEvent event) {
            pipeline.clearPath(gatherRenderersStage);
            renderQueue.submit(createRenderTask());
            //plot.getComponent().repaint();
        }

        public void opacityChanged(ImageLayerEvent event) {
            pipeline.clearPath(gatherRenderersStage);
            renderQueue.submit(createRenderTask());
            //plot.getComponent().repaint();
        }

        public void interpolationMethodChanged(ImageLayerEvent event) {
            pipeline.clearPath(gatherRenderersStage);
            renderQueue.submit(createRenderTask());
            //plot.getComponent().repaint();
        }

        public void visibilityChanged(ImageLayerEvent event) {
            pipeline.clearPath(gatherRenderersStage);
            renderQueue.submit(createRenderTask());
            //plot.getComponent().repaint();
        }

        public void clipRangeChanged(ImageLayerEvent event) {
            pipeline.clearPath(gatherRenderersStage);
            renderQueue.submit(createRenderTask());
            //plot.getComponent().repaint();

        }

        public String toString() {
            return "PipelineLayerListener for plot : " + plot.hashCode() + " with model " + plot.getModel();
        }
    }

    ;


}
