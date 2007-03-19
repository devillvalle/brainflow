package com.brainflow.core.pipeline;

import org.apache.commons.pipeline.StageException;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.brainflow.image.data.RGBAImage;
import com.brainflow.image.rendering.RenderUtils;
import com.brainflow.core.ImageLayer2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 15, 2007
 * Time: 2:05:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateBufferedImagesStage extends ImageProcessingStage {

    private static Logger log = Logger.getLogger(CreateBufferedImagesStage.class.getName());

    public void process(StageFerry ferry) throws StageException {
        List<PipelineLayer> layers = ferry.getLayers();
        for (int i = 0; i < layers.size(); i++) {
            PipelineLayer layer = layers.get(i);
            if (layer.isVisible() && layer.getRawImage() == null) {
                layer.setRawImage(createBufferedImage(layer.getColoredImage()));
            }
        }

        emit(ferry);

    }

    private BufferedImage createBufferedImage(RGBAImage rgba) {
        byte[] br = rgba.getRed().getByteArray();
        byte[] bg = rgba.getGreen().getByteArray();
        byte[] bb = rgba.getBlue().getByteArray();
        byte[] ba = rgba.getAlpha().getByteArray();

        byte[][] ball = new byte[4][];
        ball[0] = br;
        ball[1] = bg;
        ball[2] = bb;
        ball[3] = ba;
        BufferedImage bimg = RenderUtils.createBufferedImage(ball, rgba.getWidth(), rgba.getHeight());

        // code snippet is required because of bug in Java ImagingLib.
        // It cannot deal with component sample models... so we convert first.
        BufferedImage ret = RenderUtils.createCompatibleImage(bimg.getWidth(), bimg.getHeight());
        Graphics2D g2 = ret.createGraphics();
        g2.drawRenderedImage(bimg, AffineTransform.getTranslateInstance(0, 0));
        return ret;
    }
}
