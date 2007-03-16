package com.brainflow.core.pipeline;

import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.DisplayChangeType;
import com.brainflow.image.io.analyze.AnalyzeIO;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.application.BrainflowException;
import com.brainflow.application.MemoryImage;
import com.brainflow.display.ImageLayerProperties;
import com.brainflow.utils.Range;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.LinearColorMap;
import org.apache.commons.pipeline.driver.SynchronousStageDriverFactory;
import org.apache.commons.pipeline.validation.ValidationException;
import org.apache.commons.pipeline.StageException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 15, 2007
 * Time: 4:58:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestPipeline {

    ImagePipeline pipeline;

    public TestPipeline() throws BrainflowException, StageException, ValidationException {
        IImageDisplayModel model = new ImageDisplayModel("test");
        IImageData data = AnalyzeIO.readNiftiImage("F:/data/anyback/sbhighres_mean.nii");
        IImageData data2 = AnalyzeIO.readNiftiImage("F:/data/anyback/FLag_sphericity_linear.nii");

        Range range = new Range(data.getMinValue(), data.getMaxValue());


        ImageLayer layer1 = new ImageLayer(new MemoryImage(data),
                new ImageLayerProperties(new LinearColorMap(range.getMin(), range.getMax(), ColorTable.GRAYSCALE)));

        ImageLayer layer2 = new ImageLayer(new MemoryImage(data2),
                new ImageLayerProperties(new LinearColorMap(data2.getMinValue(), data2.getMaxValue(), ColorTable.SPECTRUM)));


        model.addLayer(layer1);
        model.addLayer(layer2);


        pipeline = new ImagePipeline(model, AnatomicalVolume.getCanonicalAxial());
        pipeline.addStage(new FetchSlicesStage(), new SynchronousStageDriverFactory());
        pipeline.addStage(new ColorizeImagesStage(), new SynchronousStageDriverFactory());
        pipeline.addStage(new CreateBufferedImagesStage(), new SynchronousStageDriverFactory());
        pipeline.addStage(new ResampleImagesStage(), new SynchronousStageDriverFactory());
        pipeline.addStage(new WriteBufferedImagesStage(), new SynchronousStageDriverFactory());


        StageFerry ferry = new StageFerry(model,
                new AnatomicalPoint1D(AnatomicalAxis.INFERIOR_SUPERIOR, 0),
                AnatomicalVolume.getCanonicalAxial(),
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
