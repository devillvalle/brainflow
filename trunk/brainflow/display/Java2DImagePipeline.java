package com.brainflow.display;

import javax.media.jai.JAI;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 22, 2005
 * Time: 8:27:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Java2DImagePipeline extends AbstractImagePipeline {


    public Java2DImagePipeline(DisplayableImageStack _dstack) {
        dstack = _dstack;
        computeBounds();
        frameBounds = imageBounds;
    }

    protected RenderedImage mergeImages() {
        if (mergedImage != null) return mergedImage;

        RenderedImage[] resImages = dstack.createResampledImages();

        if (resImages.length == 1) return resImages[0];

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gf = gd.getDefaultConfiguration();

        BufferedImage sourceImage = gf.createCompatibleImage((int) frameBounds.getWidth(), (int) frameBounds.getHeight());

        //AffineTransformOp  atop = new AffineTransformOp(AffineTransform.getTranslateInstance(frameBounds.getMinX(),
        //frameBounds.getMinY()), InterpolationProperty.INTERP_BILINEAR);

        //sourceImage = atop.filter(sourceImage, null);
        Graphics2D g2 = sourceImage.createGraphics();
        //System.out.println("clip: " + g2.getClipBounds());


        for (int i = 0; i < resImages.length; i++) {
            double transx = (resImages[i].getMinX() - frameBounds.getMinX()) + (-frameBounds.getMinX());
            double transy = resImages[i].getMinY() - frameBounds.getMinY() + (-frameBounds.getMinY());
            g2.drawRenderedImage(resImages[i], AffineTransform.getTranslateInstance(transx, transy));
        }


        mergedImage = sourceImage;
        return mergedImage;

    }

    public RenderedImage getScreenImage(Dimension screenDim) {
        RenderedImage mergedImage = mergeImages();
        double sx = screenDim.getWidth() / frameBounds.getWidth();
        double sy = screenDim.getHeight() / frameBounds.getHeight();

        ParameterBlock pb = new ParameterBlock();

        pb.addSource(mergedImage);
        pb.add((float) sx);
        pb.add((float) sy);

        pb.add(null).add(null);

//        Interpolation interp = dstack.get(0).getDisplayProperties().getScreenInterpolation();

        //       pb.add(interp);

        RenderedImage screenImage = JAI.create("scale", pb);
        return screenImage;
    }
}
