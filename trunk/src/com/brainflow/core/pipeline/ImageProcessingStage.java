package com.brainflow.core.pipeline;

import org.apache.commons.pipeline.stage.BaseStage;
import org.apache.commons.pipeline.StageException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 1, 2007
 * Time: 5:53:56 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ImageProcessingStage extends BaseStage {


    public void process(Object obj) throws StageException {
        process((StageFerry)obj);
    }

    public abstract void process(StageFerry ferry) throws StageException;

}
