package com.brainflow.core.pipeline;

import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.operations.ImageSlicer;
import com.brainflow.image.data.IImageData2D;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.data.IImageData;
import com.brainflow.core.*;

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
public class GatherRenderersStage extends ImageProcessingStage {


    private List<SliceRenderer> rendererList = null;

    private static Logger log = Logger.getLogger(FetchSlicesStage.class.getName());


    public GatherRenderersStage() {
    }


    public void flush() {
        rendererList = null;
    }

    public Object filter(Object o) throws StageException {
        if (rendererList != null) return rendererList;

        rendererList = new ArrayList<SliceRenderer>();
        
        IImageDisplayModel model = (IImageDisplayModel)o;
        for (int i=0; i<model.getNumLayers(); i++) {
            AbstractLayer layer = model.getLayer(i);
            SliceRenderer renderer = layer.getSliceRenderer(getSlice());
            renderer.setDisplayAnatomy(getDisplayAnatomy());
            rendererList.add(renderer);
        }

        return rendererList;

    }








}