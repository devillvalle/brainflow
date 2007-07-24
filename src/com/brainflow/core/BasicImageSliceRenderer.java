package com.brainflow.core;

import com.brainflow.colormap.IColorMap;
import com.brainflow.display.InterpolationHint;
import com.brainflow.display.InterpolationMethod;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.data.IImageData2D;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.data.RGBAImage;
import com.brainflow.image.data.UByteImageData2D;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.operations.ImageSlicer;
import com.brainflow.image.rendering.RenderUtils;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 21, 2007
 * Time: 7:34:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicImageSliceRenderer implements SliceRenderer {

    private AnatomicalPoint1D slice;

    private ImageLayer layer;

    private ImageSlicer slicer;

    private IImageData2D data;

    private RGBAImage rgbaImage;

    private RGBAImage thresholdedRGBAImage;

    private BufferedImage rawImage;

    private BufferedImage resampledImage;

    private Anatomy3D displayAnatomy = Anatomy3D.getCanonicalAxial();


    public BasicImageSliceRenderer(ImageLayer layer, AnatomicalPoint1D slice) {
        this.slice = slice;
        this.layer = layer;

        slicer = new ImageSlicer((IImageData3D) layer.getData());
    }

    public ImageSpace2D getImageSpace() {
        return (ImageSpace2D) getData().getImageSpace();
    }

    public Anatomy3D getDisplayAnatomy() {
        return displayAnatomy;
    }

    public void setDisplayAnatomy(Anatomy3D displayAnatomy) {
        this.displayAnatomy = displayAnatomy;
    }

    private IImageData2D getData() {
        if (data != null) return data;

        data = slicer.getSlice(getDisplayAnatomy(), getSlice());
        return data;
    }

    private RGBAImage getRGBAImage() {
        if (rgbaImage != null) return rgbaImage;

        IColorMap cmap = layer.getImageLayerProperties().getColorMap().getProperty();
        rgbaImage = cmap.getRGBAImage(getData());
        return rgbaImage;
    }

    private RGBAImage getThresholdedRGBAImage() {
        if (thresholdedRGBAImage != null) return thresholdedRGBAImage;

        thresholdedRGBAImage = thresholdRGBA(getRGBAImage());
        return thresholdedRGBAImage;
    }

    private BufferedImage getRawImage() {
        if (rawImage != null) return rawImage;

        rawImage = createBufferedImage(getThresholdedRGBAImage());
        return rawImage;
    }

    private BufferedImage getResampledImage() {
        if (resampledImage != null) return resampledImage;

        resampledImage = this.resample(getRawImage());
        return resampledImage;

    }


    public void setSlice(AnatomicalPoint1D slice) {
        if (!getSlice().equals(slice)) {
            this.slice = slice;
            flush();
        }

    }

    public BufferedImage render() {
        return getResampledImage();
    }

    public void renderUnto(Rectangle2D frame, Graphics2D g2) {
        if (layer.isVisible()) {
            IImageSpace space = getImageSpace();
            double minx = space.getImageAxis(Axis.X_AXIS).getRange().getMinimum();
            double miny = space.getImageAxis(Axis.Y_AXIS).getRange().getMinimum();

            double transx = (minx - frame.getMinX()); //+ (-frameBounds.getMinX());
            double transy = (miny - frame.getMinY()); //+ (-frameBounds.getMinY());


            Composite oldComposite = g2.getComposite();
            AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) layer.getOpacity());
            g2.setComposite(composite);
            g2.drawRenderedImage(render(), AffineTransform.getTranslateInstance(transx, transy));
            g2.setComposite(oldComposite);
        }
    }

    public ImageLayer getLayer() {
        return layer;
    }

    public boolean isVisible() {
        return layer.isVisible();
    }

    public AnatomicalPoint1D getSlice() {
        return slice;
    }

    public void flush() {
        data = null;
        rawImage = null;
        rgbaImage = null;
        thresholdedRGBAImage = null;
        resampledImage = null;
    }

    private BufferedImage resample(BufferedImage source) {

        ImageLayerProperties dprops = layer.getImageLayerProperties();
        InterpolationMethod interp = dprops.getResampleInterpolation().getProperty();
        ImageSpace2D ispace = (ImageSpace2D) getData().getImageSpace();

        double sx = ispace.getImageAxis(Axis.X_AXIS).getRange().getInterval() / ispace.getDimension(Axis.X_AXIS);
        double sy = ispace.getImageAxis(Axis.Y_AXIS).getRange().getInterval() / ispace.getDimension(Axis.Y_AXIS);

        //AffineTransform at = AffineTransform.getTranslateInstance(ox,oy);
        AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
        at.scale(sx, sy);
        AffineTransformOp aop = null;


        if (interp.getInterpolation() == InterpolationHint.NEAREST_NEIGHBOR) {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        } else if (interp.getInterpolation() == InterpolationHint.CUBIC) {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        } else if (interp.getInterpolation() == InterpolationHint.LINEAR) {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        } else {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        }

        return aop.filter(source, null);


    }

    private BufferedImage createBufferedImage(RGBAImage rgba) {
        //todo RGBAImage shuold have method that directly isntantiates INT_ARGB image
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


    private RGBAImage thresholdRGBA(RGBAImage rgba) {

        ImageSlicer slicer = new ImageSlicer(layer.getMaskList().composeMask(true));
        IImageData2D maskData = slicer.getSlice(getDisplayAnatomy(), getSlice());

        UByteImageData2D alpha = rgba.getAlpha();
        UByteImageData2D out = new UByteImageData2D(alpha.getImageSpace());

        ImageIterator sourceIter = alpha.iterator();
        ImageIterator maskIter = maskData.iterator();

        while (sourceIter.hasNext()) {
            int index = sourceIter.index();
            double a = sourceIter.next();
            double b = maskIter.next();

            double val = a * b;

            out.set(index, (byte) val);
        }

        RGBAImage ret = new RGBAImage(rgba.getSource(), rgba.getRed(), rgba.getGreen(), rgba.getBlue(), out);
        return ret;

    }
}

/*private RGBAImage thresholdRGBA(RGBAImage rgba) {

ThresholdRange trange = layer.getImageLayerProperties().getThresholdRange().getProperty();

if (Double.compare(trange.getMin(), trange.getMax()) != 0) {
   UByteImageData2D alpha = rgba.getAlpha();
   UByteImageData2D out = new UByteImageData2D(alpha.getImageSpace());

   MaskedData2D mask = new MaskedData2D(rgba.getSource(), (MaskPredicate) trange);

   ImageIterator sourceIter = alpha.iterator();
   ImageIterator maskIter = mask.iterator();

   while (sourceIter.hasNext()) {
       int index = sourceIter.index();
       double a = sourceIter.next();
       double b = maskIter.next();

       double val = a * b;

       out.set(index, (byte)val);
   }

   RGBAImage ret = new RGBAImage(rgba.getSource(), rgba.getRed(), rgba.getGreen(), rgba.getBlue(), out);
   return ret;

} else {
   System.out.println("min thresh = max thresh");
   return rgba;
}
}     */

