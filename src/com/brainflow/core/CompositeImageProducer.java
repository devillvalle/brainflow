package com.brainflow.core;

import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.core.pipeline.*;

import java.awt.image.BufferedImage;
import java.awt.*;

import org.apache.commons.pipeline.driver.SynchronousStageDriverFactory;
import org.apache.commons.pipeline.validation.ValidationException;
import org.apache.commons.pipeline.Feeder;

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
        this.plot = plot;


        setDisplayAnatomy(displayAnatomy);

        initPipeline();

        setSlice(getModel().getImageAxis(displayAnatomy.ZAXIS).getRange().getCenter());
        layerListener = new PipelineLayerListener();
        getModel().addImageLayerListener(layerListener);


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
        this.plot = plot;

     
        getModel().removeImageLayerListener(layerListener);
        getModel().addImageLayerListener(layerListener);

        initPipeline();
    }

    @Override
    public void setXAxis(AxisRange xaxis) {
        System.out.println("new axis  " + xaxis);
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
            // can't really handle this exception, so throw uncheckedexception
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

    public BufferedImage getImage() {
        pipeline.getSourceFeeder().feed(getModel());
        pipeline.run();
        return terminal.getImage();
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


    class PipelineLayerListener implements ImageLayerListener {

        public void thresholdChanged(ImageLayerEvent event) {
            pipeline.clearPath(gatherRenderersStage);
            plot.getComponent().repaint();
        }

        public void colorMapChanged(ImageLayerEvent event) {
            pipeline.clearPath(gatherRenderersStage);
            plot.getComponent().repaint();
        }

        public void opacityChanged(ImageLayerEvent event) {
            pipeline.clearPath(gatherRenderersStage);
            plot.getComponent().repaint();
        }

        public void interpolationMethodChanged(ImageLayerEvent event) {
            pipeline.clearPath(gatherRenderersStage);
            plot.getComponent().repaint();
        }

        public void visibilityChanged(ImageLayerEvent event) {
            pipeline.clearPath(gatherRenderersStage);
            plot.getComponent().repaint();
        }
    }

    ;


}
