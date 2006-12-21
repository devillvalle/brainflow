package com.brainflow.display;

import com.brainflow.colormap.operations.ColorMapUtils;
import com.brainflow.utils.ToStringGenerator;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.MosaicDescriptor;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Dec 16, 2004
 * Time: 7:08:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class MosaicImagePipeline extends AbstractImagePipeline {

    private Logger log = Logger.getAnonymousLogger();

    //private List<RenderedOpChain> chain = new ArrayList<RenderedOpChain>();


    public MosaicImagePipeline(DisplayableImageStack _dstack) {
        dstack = _dstack;
        computeBounds();
        frameBounds = imageBounds;
    }


    protected RenderedImage mergeImages() {
        RenderedImage mergedImage = getMergedImage();
        if (mergedImage != null) return mergedImage;

        RenderedImage[] resImages = dstack.createResampledImages();
        assert resImages != null;

        if (resImages.length == 1) return resImages[0];

        ImageLayout layout = new ImageLayout((int) frameBounds.getMinX(), (int) frameBounds.getMinY(), (int) frameBounds.getWidth(), (int) frameBounds.getHeight());
        double[] bgColor = new double[]{0, 0, 0};

        RenderedOp[] sourceAlpha = new RenderedOp[resImages.length];
        ParameterBlock mosaicParam = new ParameterBlock();
        for (int i = 0; i < resImages.length; i++) {
            if (resImages[i].getSampleModel().getNumBands() == 3) {
                System.out.println("mosaic: 3 banded image encountered");
                mosaicParam.addSource(resImages[i]);
            } else if (resImages[i].getSampleModel().getNumBands() == 4) {
                System.out.println("mosaic: 4 banded image encountered: layer = " + i);
                ParameterBlock pb = new ParameterBlock();
                pb.addSource(resImages[i]);
                pb.add(new int[]{0, 1, 2});
                RenderedOp sourceImage = JAI.create("bandselect", pb);
                mosaicParam.addSource(sourceImage);

                pb = new ParameterBlock();
                pb.addSource(resImages[i]);
                pb.add(new int[]{3});
                sourceAlpha[i] = JAI.create("bandselect", pb);
            } else if (resImages[i].getSampleModel().getNumBands() == 1) {

                resImages[i] = ColorMapUtils.paletteToRGB(resImages[i]);

                ParameterBlock pb = new ParameterBlock();
                pb.addSource(resImages[i]);
                pb.add(new int[]{0, 1, 2});
                RenderedOp sourceImage = JAI.create("bandselect", pb);
                mosaicParam.addSource(sourceImage);

                pb = new ParameterBlock();
                pb.addSource(resImages[i]);
                pb.add(new int[]{3});
                sourceAlpha[i] = JAI.create("bandselect", pb);


            }
        }

        double[][] threshold = {{0.0}};
        mosaicParam.add(MosaicDescriptor.MOSAIC_TYPE_BLEND);
        mosaicParam.add(sourceAlpha).add(null);
        mosaicParam.add(threshold).add(bgColor);

        RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
        mergedImage = JAI.create("mosaic", mosaicParam, hints);

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
        //pb.add((float)-mergedImage.getMinX());
        //pb.add((float)-mergedImage.getMinY());
        pb.add(null).add(null);

        //pb.add(InterpolationProperty.getInstance(InterpolationProperty.INTERP_BILINEAR));

        RenderedImage screenImage = JAI.create("scale", pb);
        return screenImage;


    }


    public String dump() {
        ToStringGenerator gen = new ToStringGenerator();
        return gen.generateToString(this);
    }

    public static void main(String[] args) {


    }

}


