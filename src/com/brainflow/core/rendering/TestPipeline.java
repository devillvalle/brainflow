package com.brainflow.core.rendering;

import com.brainflow.application.BrainflowException;
import com.brainflow.application.MemoryImage;
import com.brainflow.colormap.ColorTable;
import com.brainflow.core.*;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.analyze.AnalyzeIO;
import com.brainflow.image.space.Axis;
import com.brainflow.utils.Range;
import org.apache.commons.pipeline.StageException;
import org.apache.commons.pipeline.driver.SynchronousStageDriverFactory;
import org.apache.commons.pipeline.validation.ValidationException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 15, 2007
 * Time: 4:58:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestPipeline {

    ImagePlotPipeline pipeline;

    public TestPipeline() throws BrainflowException, StageException, ValidationException {
        IImageDisplayModel model = new ImageDisplayModel("test");
        IImageData data = AnalyzeIO.readNiftiImage("F:/data/anyback/sbhighres_mean.nii");
        IImageData data2 = AnalyzeIO.readNiftiImage("F:/data/anyback/FLag_sphericity_linear.nii");

        Range range = new Range(data.getMinValue(), data.getMaxValue());


        ImageLayer layer1 = new ImageLayer3D(new MemoryImage(data),
                new ImageLayerProperties(ColorTable.GRAYSCALE, range));

        ImageLayer layer2 = new ImageLayer3D(new MemoryImage(data2),
                new ImageLayerProperties(ColorTable.SPECTRUM, range));

        layer2.getImageLayerProperties().getOpacity().getProperty().setOpacity(.9f);
        model.addLayer(layer1);
        model.addLayer(layer2);


        IImagePlot plot = new ComponentImagePlot(model, Anatomy3D.getCanonicalAxial(),
                new AxisRange(model.getImageAxis(Axis.X_AXIS).getAnatomicalAxis(), -50, 50),
                new AxisRange(model.getImageAxis(Axis.Y_AXIS).getAnatomicalAxis(), -50, 50));


        pipeline = new ImagePlotPipeline(plot);
        pipeline.addStage(new FetchSlicesStage(), new SynchronousStageDriverFactory());
        pipeline.addStage(new ColorizeImagesStage(), new SynchronousStageDriverFactory());
        pipeline.addStage(new CreateBufferedImagesStage(), new SynchronousStageDriverFactory());
        pipeline.addStage(new ResampleImagesStage(), new SynchronousStageDriverFactory());

        pipeline.addStage(new ComposeImagesStage(), new SynchronousStageDriverFactory());


        pipeline.addStage(new CropImageStage(), new SynchronousStageDriverFactory());

        pipeline.addStage(new ResizeImageStage(), new SynchronousStageDriverFactory());


        StageFerry ferry = new StageFerry(model,
                new AnatomicalPoint1D(AnatomicalAxis.INFERIOR_SUPERIOR, 0),
                Anatomy3D.getCanonicalAxial(),
                DisplayChangeType.SLICE_CHANGE);

        pipeline.getSourceFeeder().feed(ferry);
        pipeline.run();


    }

    public static void main(String[] args) {
        try {
            new TestPipeline();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
