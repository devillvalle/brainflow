package com.brainflow.core.rendering;

import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.operations.ImageSlicer;
import com.brainflow.image.data.IImageData2D;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.data.IImageData;
import com.brainflow.core.ImageLayer2D;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.AbstractLayer;

import org.apache.commons.pipeline.StageException;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 1, 2007
 * Time: 5:31:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class FetchSlicesStage extends ImageProcessingStage {


    private List<ImageLayer2D> layers = null;

    private static Logger log = Logger.getLogger(FetchSlicesStage.class.getName());


    public FetchSlicesStage() {
    }


    public void flush() {
        layers = null;
    }

    public Object filter(Object o) throws StageException {
        IImageDisplayModel model = (IImageDisplayModel)o;
        if (layers == null || layers.size() != model.getNumLayers()) {
            layers = doTheWork(model);
        }

        getPipeline().setEnv(ImagePlotPipeline.IMAGE_LAYER_DATA_KEY, layers);

        return layers;

    }
  

    private List<ImageLayer2D> doTheWork(IImageDisplayModel model) {
        List<ImageLayer2D> stack = fetchSlices(model);
        return stack;
    }


    private List<ImageLayer2D> fetchSlices(IImageDisplayModel model) {

        List<ImageLayer2D> list = new ArrayList<ImageLayer2D>();
        AnatomicalPoint1D slice = getSlice();


        for (int i = 0; i < model.getNumLayers(); i++) {

            AbstractLayer layer = model.getLayer(i);

            if (layer instanceof ImageLayer) {
                ImageLayer ilayer = (ImageLayer)layer;
                IImageData data = ilayer.getData();

                ImageSlicer slicer = new ImageSlicer((IImageData3D) data);
                IImageData2D  data2d = slicer.getSlice(getDisplayAnatomy(), slice);

                ImageLayer2D layer2d = new ImageLayer2D(data2d, layer.getImageLayerProperties());
                list.add(layer2d);
            }


        }

        return list;
    }


    


}
