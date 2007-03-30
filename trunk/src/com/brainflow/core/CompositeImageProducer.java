package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.core.pipeline.*;

import java.awt.image.BufferedImage;

import org.apache.commons.pipeline.driver.SynchronousStageDriverFactory;
import org.apache.commons.pipeline.validation.ValidationException;

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

    private StageFerry ferry;



    public CompositeImageProducer(IImagePlot plot, IImageDisplayModel model, AnatomicalVolume displayAnatomy) {
        this.plot = plot;
        setDisplayAnatomy(displayAnatomy);
        setModel(model);
        setSlice(model.getImageAxis(displayAnatomy.ZAXIS).getRange().getCenter());
        initPipeline();

    }

    public CompositeImageProducer(IImagePlot plot, IImageDisplayModel model,
                                  AnatomicalVolume displayAnatomy, AnatomicalPoint1D slice) {
        this.plot = plot;
        setDisplayAnatomy(displayAnatomy);
        setModel(model);
        setSlice(slice);
        initPipeline();

    }


    
    public void setPlot(IImagePlot plot) {
        this.plot = plot;
        setModel(plot.getModel());
        initPipeline();
    }

    public IImagePlot getPlot() {
        return plot;
    }

    private void initPipeline() {
        pipeline = new ImagePlotPipeline(getModel(), getPlot());
        try {
            pipeline.addStage(new FetchSlicesStage(), new SynchronousStageDriverFactory());
            pipeline.addStage(new ColorizeImagesStage(), new SynchronousStageDriverFactory());
            pipeline.addStage(new ThresholdImagesStage(), new SynchronousStageDriverFactory());
            pipeline.addStage(new CreateBufferedImagesStage(), new SynchronousStageDriverFactory());
            pipeline.addStage(new ResampleImagesStage(), new SynchronousStageDriverFactory());
            pipeline.addStage(new ComposeImagesStage(), new SynchronousStageDriverFactory());
            pipeline.addStage(new CropImageStage(), new SynchronousStageDriverFactory());
            pipeline.addStage(new ResizeImageStage(), new SynchronousStageDriverFactory());
            ferry = new StageFerry(getModel(), getSlice(), getDisplayAnatomy(), DisplayChangeType.RESET_CHANGE);

            pipeline.getSourceFeeder().feed(getModel());
        }
        catch (ValidationException e) {
            // can't really handle this exception, so throw uncheckedexception
            throw new RuntimeException(e);
        }


    }


    public void updateImage(DisplayChangeEvent event) {

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


    }

    public BufferedImage getImage() {
        pipeline.getSourceFeeder().feed(ferry);
        pipeline.run();
        return ferry.getResizedImage();
    }

}
