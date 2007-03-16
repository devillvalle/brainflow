package com.brainflow.core.pipeline;

import org.apache.commons.pipeline.Pipeline;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.anatomy.AnatomicalPoint1D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 1, 2007
 * Time: 5:25:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImagePipeline extends Pipeline {

    public static final String MODEL_KEY = "model";

    public static final String SLICE_KEY = "slice";

    public static final String DISPLAY_ANATOMY_KEY = "displayAnatomy";

    public static final String USE_SLICE_CACHE = "use_slice_cache";

    private boolean useSliceCache = true;

    private IImageDisplayModel model;

    private AnatomicalVolume displayAnatomy;

    private AnatomicalPoint1D slice;


    public ImagePipeline(IImageDisplayModel _model, AnatomicalVolume _displayAnatomy) {
        super();
        model = _model;

        displayAnatomy = _displayAnatomy;

        setEnv(MODEL_KEY, model);
        setEnv(DISPLAY_ANATOMY_KEY, displayAnatomy);
        setEnv(USE_SLICE_CACHE, true);
    }


    
}
