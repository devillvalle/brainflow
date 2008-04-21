package com.brainflow.core;

import com.brainflow.core.rendering.*;
import com.brainflow.display.InterpolationType;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.utils.OndeckTaskExecutor;
import org.apache.commons.pipeline.Feeder;
import org.apache.commons.pipeline.Stage;
import org.apache.commons.pipeline.StageDriver;
import org.apache.commons.pipeline.driver.SynchronousStageDriverFactory;
import org.apache.commons.pipeline.validation.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 25, 2007
 * Time: 4:36:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompositeImageProducer extends AbstractImageProducer {

    private static final Logger log = Logger.getLogger(CompositeImageProducer.class.getName());

    private IImagePlot plot;

    private ImagePlotPipeline pipeline;

    private ImageLayerListener layerListener;


    private ImageProcessingStage gatherRenderersStage;

    private ImageProcessingStage renderLayersStage;

    //private ImageProcessingStage renderCoordinatesStage;

    private ImageProcessingStage cropImageStage;

    private ImageProcessingStage resizeImageStage;


    private TerminalFeeder terminal = new TerminalFeeder();

    private boolean dirty = true;

    private BufferedImage lastImage;

    private final OndeckTaskExecutor<BufferedImage> renderQueue;


    public CompositeImageProducer(IImagePlot plot, Anatomy3D displayAnatomy) {
        this(plot, displayAnatomy, plot.getModel().getImageAxis(displayAnatomy.ZAXIS).getRange().getCenter());

    }

    public CompositeImageProducer(IImagePlot plot,
                                  Anatomy3D displayAnatomy, AnatomicalPoint1D slice) {
        this(plot, displayAnatomy, slice, Executors.newSingleThreadExecutor());
        //initPipeline();

    }

    public CompositeImageProducer(IImagePlot plot,
                                      Anatomy3D displayAnatomy, AnatomicalPoint1D slice, ExecutorService service) {
            this.plot = plot;
            setDisplayAnatomy(displayAnatomy);

            //initPipeline();

            setSlice(slice);
            layerListener = new PipelineLayerListener();
            getModel().addImageLayerListener(layerListener);
            renderQueue = new OndeckTaskExecutor<BufferedImage>(service);

        }




    public void setScreenInterpolation(InterpolationType type) {
        super.setScreenInterpolation(type);
        //pipeline.clearPath(resizeImageStage);
        dirty = true;
    }


    public void setScreenSize(Rectangle screenSize) {
        super.setScreenSize(screenSize);
        //pipeline.clearPath(resizeImageStage);
        dirty = true;
    }

    public void reset() {
        //pipeline.clearPath(gatherRenderersStage);
        dirty = true;
        //getPlot().getComponent().repaint();
    }

    public void setSlice(AnatomicalPoint1D slice) {
        super.setSlice(slice);
        //pipeline.clearPath(gatherRenderersStage);
        dirty = true;
    }

    public void setPlot(IImagePlot plot) {
        getModel().removeImageLayerListener(layerListener);
        this.plot = plot;
        getModel().addImageLayerListener(layerListener);

        //initPipeline();
        dirty = true;
    }

    @Override
    public void setXAxis(AxisRange xaxis) {
        super.setXAxis(xaxis);
        //pipeline.clearPath(cropImageStage);
        dirty = true;
    }

    @Override
    public void setYAxis(AxisRange yaxis) {
        super.setYAxis(yaxis);
        //pipeline.clearPath(cropImageStage);
        dirty = true;
    }


    public IImageDisplayModel getModel() {
        return plot.getModel();
    }

    public IImagePlot getPlot() {
        return plot;
    }

    private ImagePlotPipeline createPipeline() {
        pipeline = new ImagePlotPipeline(getPlot());
        try {

            // when creating pipeline we could supply cache
            pipeline.addStage(new GatherRenderersStage(), new SynchronousStageDriverFactory());
            pipeline.addStage(new RenderLayersStage(), new SynchronousStageDriverFactory());
            pipeline.addStage(new CropImageStage(), new SynchronousStageDriverFactory());
            pipeline.addStage(new ResizeImageStage(), new SynchronousStageDriverFactory());

            pipeline.getSourceFeeder().feed(getModel());
            pipeline.setTerminalFeeder(new TerminalFeeder());
        }
        catch (ValidationException e) {
            // can'three really handle this exception, so throw uncheckedexception
            throw new RuntimeException(e);
        }

        return pipeline;


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


    public synchronized BufferedImage render() {

        //assert pipeline.getStageDrivers().get(0).getState() != StageDriver.State.RUNNING;
        ImagePlotPipeline pipeline = createPipeline();

        pipeline.getSourceFeeder().feed(getModel());
        pipeline.run();
        dirty = false;
        lastImage = ((TerminalFeeder)pipeline.getTerminalFeeder()).getImage();
        return lastImage;
    }

    public synchronized BufferedImage getImage() {
        // does this spawn a new thread?
        // could be submitted to thread pool?

        if (dirty || lastImage == null) {
            return render();
        } else {
            return lastImage;
        }


    }




    private Callable<BufferedImage> createRenderTask() {
        return new Callable<BufferedImage>() {
            public BufferedImage call() throws Exception {
                BufferedImage buf = render();
                plot.getComponent().repaint();
                return buf;

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



    protected void finalize() throws Throwable {
        System.out.println("garbage collecting " + this);
        getModel().removeImageLayerListener(layerListener);

    }

    class PipelineLayerListener implements ImageLayerListener {

        public void thresholdChanged(ImageLayerEvent event) {
            //todo make this a bit more intelligent. "clearPath" flushes the renderers
            //pipeline.clearPath(gatherRenderersStage);
            dirty = true;
            renderQueue.submitTask(createRenderTask());
            //plot.getComponent().repaint();
        }

        public void smoothingChanged(ImageLayerEvent event) {
            //pipeline.clearPath(gatherRenderersStage);
            dirty = true;
            renderQueue.submitTask(createRenderTask());

        }

        public void colorMapChanged(ImageLayerEvent event) {
            //pipeline.clearPath(gatherRenderersStage);
            dirty = true;

            ThreadPoolExecutor executor = (ThreadPoolExecutor)renderQueue.getExecutorService();
            
            System.out.println("ACTIVE TASKS: " + executor.getActiveCount());
            renderQueue.submitTask(createRenderTask());
            //plot.getComponent().repaint();
        }

        public void opacityChanged(ImageLayerEvent event) {
            //pipeline.clearPath(gatherRenderersStage);
            dirty = true;
            renderQueue.submitTask(createRenderTask());
            //plot.getComponent().repaint();
        }

        public void interpolationMethodChanged(ImageLayerEvent event) {
            //pipeline.clearPath(gatherRenderersStage);
            dirty = true;
            renderQueue.submitTask(createRenderTask());
            //plot.getComponent().repaint();
        }

        public void visibilityChanged(ImageLayerEvent event) {
            //pipeline.clearPath(gatherRenderersStage);
            dirty = true;
            renderQueue.submitTask(createRenderTask());
            //plot.getComponent().repaint();
        }

        public void clipRangeChanged(ImageLayerEvent event) {
            // no need to repaint because clip events are also detected as color map events ...

        }

        public String toString() {
            return "PipelineLayerListener for plot : " + plot.hashCode() + " with model " + plot.getModel();
        }
    }

    ;


}
