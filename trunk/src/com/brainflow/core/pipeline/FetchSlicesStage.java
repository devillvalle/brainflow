package com.brainflow.core.pipeline;

import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.operations.ImageSlicer;
import com.brainflow.image.data.IImageData2D;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.data.IImageData;
import com.brainflow.core.ImageLayer2D;
import com.brainflow.core.DisplayChangeType;
import com.brainflow.core.ImageLayer;
import com.brainflow.utils.MultiKey;

import org.apache.commons.pipeline.stage.BaseStage;
import org.apache.commons.pipeline.StageContext;
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


    private static final int MAX_ENTRIES = 50;

    private static Logger log = Logger.getLogger(FetchSlicesStage.class.getName());


    public FetchSlicesStage() {


    }


  
    public void process(StageFerry ferry) throws StageException {

        List<PipelineLayer> stack = ferry.getLayers();

        if (stack == null || stack.size() != ferry.getModel().getNumLayers()) {
            doTheWork(ferry);
        }

        emit(ferry);
    }

    private void doTheWork(StageFerry ferry) {
        List<ImageLayer2D> stack = fetchSlices(ferry);
        List<PipelineLayer> layers = ferry.getLayers();

        if (layers == null) {
            layers = new ArrayList<PipelineLayer>();
            ferry.setLayers(layers);
         
        }

        for (int i = 0; i < stack.size(); i++) {
            layers.add(new PipelineLayer(stack.get(i)));
        }

    }


    private List<ImageLayer2D> fetchSlices(StageFerry ferry) {

        List<ImageLayer2D> list = new ArrayList<ImageLayer2D>();
        AnatomicalPoint1D slice = ferry.getSlice();


        for (int i = 0; i < ferry.getModel().getNumLayers(); i++) {

            ImageLayer layer = ferry.getModel().getImageLayer(i);
            IImageData data = layer.getImageData();
            IImageData2D data2d = null;

            /*if (useCache) {
                MultiKey key = new MultiKey(data, slice);
                data2d = cache.get(key);
            }*/

            if (data2d == null) {
                ImageSlicer slicer = new ImageSlicer((IImageData3D) data);
                data2d = slicer.getSlice(ferry.getDisplayAnatomy(), slice);

                //if (useCache) {
                //    cache.put(new MultiKey(data, slice), data2d);
                //}
            }

            ImageLayer2D layer2d = new ImageLayer2D(data2d, layer.getImageLayerParameters());
            list.add(layer2d);


        }

        return list;
    }
}
