package com.brainflow.core.pipeline;

import org.apache.commons.pipeline.StageException;

import javax.imageio.ImageIO;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2007
 * Time: 4:08:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class WriteCompositeImageStage extends ImageProcessingStage {
    private static Logger log = Logger.getLogger(WriteCompositeImageStage.class.getName());

    String path = System.getProperty("user.dir");


    public void process(StageFerry ferry) throws StageException {
        BufferedImage bimg = ferry.getCompositeImage();
        try {
            if (bimg != null) {
                ImageIO.write(bimg, "png", new File(path + "/" + "Composite.png"));

            }
        } catch (IOException e) {
            throw new StageException(this, e);
        }

        emit(ferry);
    }
}
