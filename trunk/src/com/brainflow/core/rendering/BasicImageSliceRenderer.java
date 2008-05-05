package com.brainflow.core.rendering;

import com.brainflow.colormap.IColorMap;
import com.brainflow.display.InterpolationType;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.data.*;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.operations.ImageSlicer;
import com.brainflow.image.rendering.PixelUtils;
import com.brainflow.image.rendering.RenderUtils;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace2D;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.core.SliceRenderer;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageLayerProperties;
import com.brainflow.core.ImageLayer3D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 21, 2007
 * Time: 7:34:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicImageSliceRenderer implements SliceRenderer {

    private static Logger log = Logger.getLogger(BasicImageSliceRenderer.class.getName());

    private AnatomicalPoint1D slice;

    private ImageLayer layer;

    private ImageSlicer slicer;

    private IImageData2D data;

    private RGBAImage rgbaImage;

    private RGBAImage thresholdedRGBAImage;

    private BufferedImage rawImage;

    private BufferedImage smoothedImage;

    private BufferedImage resampledImage;

    private Anatomy3D displayAnatomy = Anatomy3D.getCanonicalAxial();

    private ImageSpace3D refSpace;


    public BasicImageSliceRenderer(ImageSpace3D refSpace, ImageLayer layer, AnatomicalPoint1D slice) {
        this.slice = slice;
        this.layer = layer;
        this.refSpace = refSpace;
        
        //hack cast
        slicer = new ImageSlicer(new MappedDataAcessor3D(refSpace, (IImageData3D)layer.getData()));
    }

    public ImageSpace2D getImageSpace() {
        return getData().getImageSpace();
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

        IColorMap cmap = layer.getImageLayerProperties().colorMap.get();
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

    private BufferedImage getSmoothedImage() {
        if (smoothedImage != null) return smoothedImage;
        smoothedImage = this.smooth(getRawImage());

        return smoothedImage;

    }

    private BufferedImage getResampledImage() {
        if (resampledImage != null) return resampledImage;

        resampledImage = this.resample(getSmoothedImage());
        //resampledImage = this.convolveAlpha(resampledImage);
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
            AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) layer.getImageLayerProperties().opacity.get().doubleValue());
            g2.setComposite(composite);
            g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
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
        smoothedImage = null;
        resampledImage = null;
    }

    private BufferedImage smooth(BufferedImage source) {
        ImageLayerProperties dprops = layer.getImageLayerProperties();

        double radius = dprops.smoothingRadius.get();
        if (radius < .01) return source;

        ImageSpace2D ispace = getData().getImageSpace();
        double sx = ispace.getImageAxis(Axis.X_AXIS).getRange().getInterval() / ispace.getDimension(Axis.X_AXIS);
        double sy = ispace.getImageAxis(Axis.Y_AXIS).getRange().getInterval() / ispace.getDimension(Axis.Y_AXIS);

        Kernel kern = PixelUtils.makeKernel((float) radius, (float) sx, (float) sy);

        ConvolveOp cop = new ConvolveOp(kern);
        return cop.filter(source, null);

    }

    private BufferedImage resample(BufferedImage source) {

        ImageLayerProperties dprops = layer.getImageLayerProperties();
        InterpolationType interp = dprops.getInterpolation();
        ImageSpace2D ispace = getData().getImageSpace();

        double sx = ispace.getImageAxis(Axis.X_AXIS).getRange().getInterval() / ispace.getDimension(Axis.X_AXIS);
        double sy = ispace.getImageAxis(Axis.Y_AXIS).getRange().getInterval() / ispace.getDimension(Axis.Y_AXIS);

        // if (!source.isAlphaPremultiplied()) {
        log.finest("premultiplying alpha prior to resize");
        //PremultiplyFilter filter = new PremultiplyFilter();
        //source = filter.filter(source, null);
        //  }

        System.out.println("layer : " + layer);
        System.out.println("scale factor sx : " + sx);
        System.out.println("scale factor sy : " + sy);

        AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
        at.scale(sx, sy);
        AffineTransformOp aop = null;


        if (interp == InterpolationType.NEAREST_NEIGHBOR) {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        } else if (interp == InterpolationType.CUBIC) {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        } else if (interp == InterpolationType.LINEAR) {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        } else {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        }


        BufferedImage ret = aop.filter(source, null);

        //if (ret.isAlphaPremultiplied()) {
        //log.finest("unpremultiplying alpha prior to resize");
        //UnpremultiplyFilter ufilter = new UnpremultiplyFilter();
        //ret = ufilter.filter(ret, null);

        //}

        return ret;


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
        BufferedImage bimg = RenderUtils.createInterleavedBufferedImage(ball, rgba.getWidth(), rgba.getHeight(), false);

        // code snippet is required because of bug in Java ImagingLib.
        // It cannot deal with component sample models... so we convert first.
        //BufferedImage ret = RenderUtils.createCompatibleImage(bimg.getWidth(), bimg.getHeight());
        //Graphics2D g2 = ret.createGraphics();
        //g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //g2.drawRenderedImage(bimg, AffineTransform.getTranslateInstance(0, 0));
        return bimg;
    }


    protected RGBAImage thresholdRGBA(RGBAImage rgba) {
        //todo here's the rub
        ImageSlicer slicer = new ImageSlicer(new MappedDataAcessor3D(refSpace, layer.getMaskList().composeMask(true)));
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

