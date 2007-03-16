package com.brainflow.core.pipeline;

import org.apache.commons.pipeline.stage.BaseStage;
import org.apache.commons.pipeline.StageException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Logger;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 15, 2007
 * Time: 4:59:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class WriteBufferedImagesStage extends ImageProcessingStage {

    private static Logger log = Logger.getLogger(CreateBufferedImagesStage.class.getName());

    String path = System.getProperty("user.dir");

    public void process(StageFerry ferry) throws StageException {
        log.entering(getClass().getName(), "process");
        log.info("path = " + path);
        List<BufferedImage> imgs = ferry.getResampledImageStack();

        try {
            for (int i = 0; i < imgs.size(); i++) {
                BufferedImage img = imgs.get(i);
                if (img != null) {
                    ImageIO.write(imgs.get(i), "png", new File(path + "/" + "Raw-Layer-" + i + ".png"));
                }
            }
        } catch (IOException e) {
            throw new StageException(this, e);
        }

        emit(ferry);


    }
}
