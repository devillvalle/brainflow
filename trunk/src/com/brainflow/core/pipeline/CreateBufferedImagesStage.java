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
        List<RGBAImage> layers = ferry.getRGBAImageLayerStack();
        List<BufferedImage> imageList = ferry.getBufferedImageStack();

        if (imageList == null || imageList.size() != ferry.getModel().getNumLayers()) {
            imageList = new ArrayList<BufferedImage>();
            int n = layers.size();
            for (int i=0; i<n; i++) {
                if (ferry.getModel().getImageLayer(i).isVisible()) {
                    imageList.add(createBufferedImage(layers.get(i)));
                } else {
                    imageList.add(null);
                }
            }

            ferry.setBufferedImageStack(imageList);
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
        BufferedImage bimg =  RenderUtils.createBufferedImage(ball, rgba.getWidth(), rgba.getHeight());

        // code snippet is required because of bug in Java ImagingLib.
        // It cannot deal with component sample models... so we convert first.
        BufferedImage ret = RenderUtils.createCompatibleImage(bimg.getWidth(), bimg.getHeight());
        Graphics2D g2 = ret.createGraphics();
        g2.drawRenderedImage(bimg, AffineTransform.getTranslateInstance(0,0));
        return ret;
    }
}
