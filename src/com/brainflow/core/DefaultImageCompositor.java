/*
 * ImageCompositor.java
 *
 * Created on June 30, 2006, 10:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.core;

import com.brainflow.display.ImageLayerParameters;
import com.brainflow.display.InterpolationHint;
import com.brainflow.display.InterpolationProperty;
import com.brainflow.image.data.IImageData2D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace2D;

import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.util.ArrayList;
import java.util.List;

/**
 * @author buchs
 */
public class DefaultImageCompositor implements IImageCompositor {

    /**
     * Creates a new instance of ImageCompositor
     */


    private List<RenderedImage> cachedResampled;

    private List<ImageLayer2D> images;

    private BufferedImage composedImage = null;

    private GraphicsConfiguration gf;

    public DefaultImageCompositor() {

    }

    public DefaultImageCompositor(List<ImageLayer2D> images) {
        assert images.size() >= 1;
        this.images = images;

    }

    public synchronized void setImageList(List<ImageLayer2D> images) {

        if (this.images == null) {
            this.images = images;
        } else if (this.images == images || this.images.equals(images)) {
            return;
        } else {
            setDirty();
            this.images = images;
        }

    }


    public void setDirty() {
        cachedResampled = null;
        composedImage = null;
    }


    protected synchronized List<RenderedImage> createResampledImages() {
        if (cachedResampled != null) return cachedResampled;

        List<RenderedImage> resampled = new ArrayList<RenderedImage>();
        ParameterBlock pb = null;
        for (int i = 0; i < images.size(); i++) {
            ImageLayer2D layer = images.get(i);
            IImageData2D data = layer.getImageData();
            ImageSpace2D ispace = (ImageSpace2D) data.getImageSpace();

            ImageLayerParameters dprops = layer.getImageLayerParameters();
            InterpolationProperty interp = dprops.getResampleInterpolation().getProperty();

            double sx = ispace.getImageAxis(Axis.X_AXIS).getRange().getInterval() / ispace.getDimension(Axis.X_AXIS);
            double sy = ispace.getImageAxis(Axis.Y_AXIS).getRange().getInterval() / ispace.getDimension(Axis.Y_AXIS);

            double ox = ispace.getImageAxis(Axis.X_AXIS).getRange().getMinimum();
            double oy = ispace.getImageAxis(Axis.Y_AXIS).getRange().getMinimum();


            RenderingHints hints = new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL,
                    Boolean.TRUE);

            //AffineTransform at = AffineTransform.getTranslateInstance(ox,oy);
            //at.scale(sx,sy);

            //BufferedImageOp bop = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);

            pb = new ParameterBlock();
            pb.addSource(layer.getBufferedImage());
            pb.add((float) sx);
            pb.add((float) sy);
            pb.add((float) ox);
            pb.add((float) oy);

            //BufferedImage bimg = layer.getBufferedImage();
            //GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //GraphicsDevice gd = ge.getDefaultScreenDevice();
            //GraphicsConfiguration gf = gd.getDefaultConfiguration();

            //BufferedImage dest = gf.createCompatibleImage(bimg.getWidth(), bimg.getHeight());


            if (interp.getInterpolation() == InterpolationHint.NEAREST_NEIGHBOR) {
                //resampled.add(bop.filter(layer.getBufferedImage(),null));

                pb.add(Interpolation.getInstance(Interpolation.INTERP_NEAREST));
            } else if (interp.getInterpolation() == InterpolationHint.CUBIC) {
                //resampled.add(bop.filter(layer.getBufferedImage(), null));

                pb.add(Interpolation.getInstance(Interpolation.INTERP_BICUBIC));
            } else if (interp.getInterpolation() == InterpolationHint.LINEAR) {
                //resampled.add(bop.filter(layer.getBufferedImage(), null));

                pb.add(Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
            } else {
                //resampled.add(bop.filter(layer.getBufferedImage(), null));

                pb.add(Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
            }

            resampled.add(JAI.create("scale", pb, hints));


        }

        cachedResampled = resampled;
        return cachedResampled;
    }


    private Rectangle2D getBounds() {
        if (images == null || images.size() == 0) {
            return new Rectangle2D.Double(0, 0, 0, 0);
        }

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (ImageLayer2D layer : images) {
            IImageData2D d2 = (IImageData2D) layer.getImageData();
            IImageSpace space = d2.getImageSpace();
            minX = Math.min(minX, space.getImageAxis(Axis.X_AXIS).getRange().getMinimum());
            minY = Math.min(minY, space.getImageAxis(Axis.Y_AXIS).getRange().getMinimum());
            maxX = Math.max(maxX, space.getImageAxis(Axis.X_AXIS).getRange().getMaximum());
            maxY = Math.max(maxY, space.getImageAxis(Axis.Y_AXIS).getRange().getMaximum());
        }

        Rectangle2D imageBounds = new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
        return imageBounds;
    }

    private GraphicsConfiguration getGraphicsConfiguration() {
        if (gf == null) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            gf = gd.getDefaultConfiguration();
        }

        return gf;
    }

    public synchronized BufferedImage compose() {
        if (images.size() == 0) {
            return null;
        }

        if (composedImage != null) {
            return composedImage;
        }


        createResampledImages();

        /*if (images.size() == 1) {
           composedImage = (BufferedImaimages.get(0).getRenderedImage();
           return composedImage;
       } */


        Rectangle2D frameBounds = getBounds();


        BufferedImage sourceImage = getGraphicsConfiguration().
                createCompatibleImage((int) frameBounds.getWidth(), (int) frameBounds.getHeight());

        Graphics2D g2 = sourceImage.createGraphics();

        for (int i = 0; i < images.size(); i++) {
            ImageLayer2D layer = images.get(i);
            if (layer.getImageLayerParameters().getVisiblility().getProperty().isVisible()) {
                RenderedImage rim = cachedResampled.get(i);
                double transx = (rim.getMinX() - frameBounds.getMinX()) + (-frameBounds.getMinX());
                double transy = (rim.getMinY() - frameBounds.getMinY()) + (-frameBounds.getMinY());

                g2.drawRenderedImage(rim, AffineTransform.getTranslateInstance(transx, transy));
            }
        }

        composedImage = sourceImage;
        return composedImage;

    }


    public synchronized RenderedImage crop2(Rectangle2D region) {
        if (images.size() == 0) {
            return null;
        }

        Rectangle2D bounds = getBounds();
        if (bounds.equals(region)) {
            return compose();
        }

        ParameterBlock pb = new ParameterBlock();

        RenderedImage rimg = compose();
        pb.addSource(rimg);

        float xmin = (float) (region.getX() - bounds.getX());
        float ymin = (float) (region.getY() - bounds.getY());
        float width = (int) Math.min(bounds.getWidth() - xmin, region.getWidth());
        float height = (int) Math.min(bounds.getHeight() - ymin, region.getHeight());


        pb.add(xmin);
        pb.add(ymin);

        pb.add(width);
        pb.add(height);

        RenderedImage croppedImage = JAI.create("crop", pb, null);


        pb = new ParameterBlock();
        pb.addSource(croppedImage);
        pb.add((float) (-croppedImage.getMinX()));
        pb.add((float) (-croppedImage.getMinY()));
        return JAI.create("translate", pb, null);


    }


    public synchronized RenderedImage crop(Rectangle2D region) {
        if (images.size() == 0) {
            return null;
        }

        Rectangle2D bounds = getBounds();
        if (bounds.equals(region)) {
            return compose();
        }

        ParameterBlock pb = new ParameterBlock();


        try {
            RenderedImage rimg = compose();
            pb.addSource(rimg);

            float xmin = (float) Math.floor(region.getX() - bounds.getX());
            float ymin = (float) Math.floor(region.getY() - bounds.getY());
            float width = (int) Math.min(bounds.getWidth() - xmin, region.getWidth());
            float height = (int) Math.min(bounds.getHeight() - ymin, region.getHeight());


            pb.add(xmin);
            pb.add(ymin);

            pb.add(width);
            pb.add(height);

            RenderedImage croppedImage = JAI.create("crop", pb, null);
            pb = new ParameterBlock();
            pb.addSource(croppedImage);
            pb.add((float) (-croppedImage.getMinX()));
            pb.add((float) (-croppedImage.getMinY()));
        } catch (IllegalArgumentException e) {

            e.printStackTrace();
        }


        return JAI.create("translate", pb, null);


    }

    public RenderedImage scale(float sx, float sy) {
        RenderedImage mergedImage = compose();

        ParameterBlock pb = new ParameterBlock();

        pb.addSource(mergedImage);
        pb.add(sx);
        pb.add(sy);

        pb.add(null).add(null);


        pb.add(Interpolation.getInstance(Interpolation.INTERP_BICUBIC));

        RenderedImage screenImage = JAI.create("scale", pb);
        return screenImage;
    }

    public RenderedImage scale2(float sx, float sy) {
        RenderedImage mergedImage = compose();

        AffineTransform at = AffineTransform.getScaleInstance(sx, sy);
        BufferedImageOp bop = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        ParameterBlock pb = new ParameterBlock();

        pb.addSource(mergedImage);
        pb.add(sx);
        pb.add(sy);

        pb.add(null).add(null);


        pb.add(Interpolation.getInstance(Interpolation.INTERP_BICUBIC));

        RenderedImage screenImage = JAI.create("scale", pb);
        return screenImage;
    }


}



