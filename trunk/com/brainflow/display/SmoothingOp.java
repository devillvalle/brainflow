package com.brainflow.display;

import cern.jet.random.Normal;
import com.brainflow.application.ILoadableImage;
import com.brainflow.application.MemoryImage;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.SnapShooter;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.io.AnalyzeIO;

import javax.media.jai.JAI;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RenderedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 17, 2006
 * Time: 5:18:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class SmoothingOp implements ImageOp {

    public static final String OP_NAME = "smoothing_op";

    float[] kernel = new float[]{.11f, .11f, .11f, .11f, .11f, .11f, .11f, .11f, .11f};
    float radius = 1;
    Normal normal;


    public SmoothingOp(float radius) {
        this.radius = radius;
    }


    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public RenderedImage filter(RenderedImage input) {
        BufferedImage bimg = null;

        if (!(input instanceof BufferedImage)) {
            bimg = new BufferedImage(input.getWidth(),
                    input.getHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);

            bimg.createGraphics().drawRenderedImage(input,
                    AffineTransform.getTranslateInstance(-input.getMinX(), -input.getMinY()));
        } else {
            System.out.println("input is bufferedimage");
            bimg = (BufferedImage) input;
        }

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gf = gd.getDefaultConfiguration();

        BufferedImage destImage = gf.createCompatibleImage((int) input.getWidth(), (int) input.getHeight());


        ConvolveOp cop = new ConvolveOp(new Kernel(3, 3, kernel));
        return cop.filter(bimg, destImage);
    }


    public float[] makeKernel(double radius) {
        radius += 1;
        int size = (int) radius * 2 + 1;
        float[] kernel = new float[size];

        for (int i = 0; i < size; i++)
            kernel[i] = (float) Math.exp(-0.5 * (Math.sqrt((i - radius) / (radius * 2))) / Math.sqrt(0.2));
        float[] kernel2 = new float[size - 2];
        for (int i = 0; i < size - 2; i++)
            kernel2[i] = kernel[i + 1];
        if (kernel2.length == 1)
            kernel2[0] = 1f;
        System.out.println("radius = " + radius);
        for (int i = 0; i < kernel2.length; i++) {
            System.out.println("k " + i + ": " + kernel2[i]);
        }
        return kernel2;
    }


    public static void main(String[] args) {
        try {
            ILoadableImage img = new MemoryImage(AnalyzeIO.readNiftiImage("F:/data/anyback/tgoodbad.nii"));

            LinearColorMap lmap = new LinearColorMap(-4, 4, ColorTable.GRAYSCALE);

            ImageDisplayModel dset1 = new ImageDisplayModel("unsmoothed");
            ImageDisplayModel dset2 = new ImageDisplayModel("smoothed");

            dset1.addLayer(new ImageLayer(img.getData(), new ImageLayerParameters(lmap)));
            dset2.addLayer(new ImageLayer(img.getData(), new ImageLayerParameters(lmap)));

            SnapShooter shooter = new SnapShooter(dset1, AnatomicalVolume.getCanonicalAxial());
            RenderedImage rimg = shooter.shoot(0);
            JAI.create("filestore", rimg, "c:/test_unsmoothed.png", "png");

            SmoothingOp sop = new SmoothingOp(5);
            rimg = sop.filter(rimg);
            JAI.create("filestore", rimg, "c:/test_smoothed.png", "png");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
