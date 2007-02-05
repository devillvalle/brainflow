package com.brainflow.core;

import com.brainflow.application.ILoadableImage;
import com.brainflow.application.MemoryImage;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.colormap.DiscreteColorMap;
import com.brainflow.display.ImageLayerParameters;
import com.brainflow.display.InterpolationProperty;
import com.brainflow.display.InterpolationHint;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.io.AnalyzeIO;
import com.brainflow.image.data.IImageData3D;

import javax.media.jai.JAI;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Mar 12, 2005
 * Time: 1:01:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class SnapShooter {

    private IImageDisplayModel dset;

    private IImagePlot emulator;

    private AnatomicalVolume displayAnatomy;

    private DefaultImageProcurer procurer;

    private DefaultImageCompositor compositor;

    private int width;

    private int height;


    public SnapShooter(IImageDisplayModel _dset, AnatomicalVolume _displayAnatomy) {
        dset = _dset;
        setDisplayAnatomy(_displayAnatomy);

    }

    public void setDisplayAnatomy(AnatomicalVolume _displayAnatomy) {
        displayAnatomy = _displayAnatomy;
        compositor = new DefaultImageCompositor();
        procurer = new DefaultImageProcurer(dset, AnatomicalVolume.getCanonicalAxial());

        ImagePlotRenderer renderer = new ImagePlotRenderer(compositor, procurer);

        emulator = new BasicImagePlot(displayAnatomy, dset.getImageAxis(displayAnatomy.XAXIS).getRange(),
                dset.getImageAxis(displayAnatomy.YAXIS).getRange(), renderer);


        width = (int) dset.getImageAxis(displayAnatomy.XAXIS).getRange().getInterval() * 2;
        height = (int) dset.getImageAxis(displayAnatomy.YAXIS).getRange().getInterval() * 2;

    }


    public ImageIcon shootLayer(double slice, int layer, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        procurer.setSlice(new AnatomicalPoint1D(displayAnatomy.ZAXIS, slice));

        emulator.paint(img.createGraphics(), new Rectangle(0, 0, width, height));
        return new ImageIcon(img);

    }

    public RenderedImage shoot(double slice) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gf = gd.getDefaultConfiguration();

        BufferedImage img = gf.createCompatibleImage(width, height);

        procurer.setSlice(new AnatomicalPoint1D(displayAnatomy.ZAXIS, slice));
        emulator.paint(img.createGraphics(), new Rectangle(0, 0, width, height));
        return img;
    }

    public void shootContinuouslyAndSave(String path, String prefix, double startSlice, double endSlice, double increment) {
        RenderedImage[] rimg = shootContinuously(startSlice, endSlice, increment);
        for (int i = 0; i < rimg.length; i++) {
            String num = "";
            if (i < 10) num = "00" + i;
            else if (i < 100) num = "0" + i;
            else num = num + i;

            String fname = path + "/" + prefix + "_" + num + ".png";
            JAI.create("filestore", rimg[i], fname, "png");
        }
    }

    public RenderedImage[] shootContinuously(double startSlice, double endSlice, double increment) {
        AxisRange range = dset.getImageAxis(displayAnatomy.ZAXIS).getRange();
        if (!range.contains(startSlice) || (!range.contains(endSlice))) {
            throw new IllegalArgumentException("Illegal slice range " + startSlice + " to " + endSlice);
        }

        List<RenderedImage> list = new ArrayList<RenderedImage>();
        double curSlice = startSlice;
        while (curSlice <= endSlice) {
            list.add(shoot(curSlice));
            curSlice += increment;
        }

        RenderedImage[] rimg = new RenderedImage[list.size()];
        list.toArray(rimg);

        return rimg;

    }

    public RenderedImage[] shootSlices(double[] slices) {
        RenderedImage[] rimg = new RenderedImage[slices.length];
        for (int i = 0; i < slices.length; i++) {
            rimg[i] = shoot(slices[i]);
        }

        return rimg;
    }

    public static Color getHSB(float h, float s, float b, int alpha) {
        Color clr = Color.getHSBColor(h,s,b);
        return new Color(clr.getRed(), clr.getGreen(), clr.getBlue(), alpha);
    }



    public static void main(String[] args) {
        try {

            ImageDisplayModel dset = new ImageDisplayModel("snapper");
            ILoadableImage il1 = new MemoryImage(AnalyzeIO.readAnalyzeImage("c:/DTI/slopes/ch2"));
            ILoadableImage il2 = new MemoryImage(AnalyzeIO.readAnalyzeImage("c:/DTI/slopes/tAgeXDiag"));
            ILoadableImage il3 = new MemoryImage(AnalyzeIO.readAnalyzeImage("c:/DTI/slopes/tAge.Schiz"));
            ILoadableImage il4 = new MemoryImage(AnalyzeIO.readAnalyzeImage("c:/DTI/slopes/tAge.Norm"));
            ILoadableImage il5 = new MemoryImage(AnalyzeIO.readAnalyzeImage("c:/DTI/slopes/bAge.Schiz"));
            ILoadableImage il6 = new MemoryImage(AnalyzeIO.readAnalyzeImage("c:/DTI/slopes/bAge.Norm"));


            LinearColorMap lmap = new LinearColorMap(0, 221, ColorTable.GRAYSCALE);
            DiscreteColorMap ragged = new DiscreteColorMap(lmap);

            /*ragged.addInterval(0, 1.65, new Color(0, 0, 0, 0));
            ragged.addInterval(1.65, 1.95, new Color(255, 26, 26, 100));
            ragged.addInterval(1.95, 2.55, new Color(255, 77, 77, 150));
            ragged.addInterval(2.55, 3.2, new Color(255, 128, 128, 200));
            ragged.addInterval(3.2, 4, new Color(255, 179, 179, 255));
            ragged.addInterval(4, 12, new Color(255, 179, 179, 255));

            ragged.addInterval(-1.65, 0, new Color(0, 0, 0, 0));
            ragged.addInterval(-1.95, -1.65, new Color(26, 26, 255, 100));
            ragged.addInterval(-2.55, -1.95, new Color(77, 77, 255, 150));
            ragged.addInterval(-3.2, -2.55, new Color(128, 128, 255, 200));
            ragged.addInterval(-12, -3.2, new Color(179, 179, 255, 255)); */


            ImageLayer3D layer1 = new ImageLayer3D((IImageData3D) il1.getData());
            layer1.getImageLayerParameters().getColorMap().setParameter(lmap);
            ImageLayer3D layer2 = new ImageLayer3D((IImageData3D) il2.getData());
            layer2.getImageLayerParameters().getColorMap().setParameter(ragged);
            layer2.getImageLayerParameters().setScreenInterpolation(new InterpolationProperty(InterpolationHint.CUBIC));
            layer2.getImageLayerParameters().setResampleInterpolation(new InterpolationProperty(InterpolationHint.CUBIC));
            dset.addLayer(layer1);
            dset.addLayer(layer2);


            SnapShooter shooter = new SnapShooter(dset, AnatomicalVolume.getCanonicalAxial());
            shooter.shootContinuouslyAndSave("c:/DTI/slopes/", "TAgeXDiagnosis_Axial", -45, 45, 2);
            //shooter.setDisplayAnatomy();
            shooter.shootContinuouslyAndSave("c:/DTI/slopes/", "TAgeXDiagnosis_Sagittal", -58, 58, 2);

            dset.removeLayer(1);
            layer2 = new ImageLayer3D((IImageData3D) il3.getData());
            layer2.getImageLayerParameters().getColorMap().setParameter(ragged);
            layer2.getImageLayerParameters().setScreenInterpolation(new InterpolationProperty(InterpolationHint.CUBIC));
            layer2.getImageLayerParameters().setResampleInterpolation(new InterpolationProperty(InterpolationHint.CUBIC));
            dset.addLayer(layer2);
            shooter = new SnapShooter(dset, AnatomicalVolume.getCanonicalAxial());

            shooter.shootContinuouslyAndSave("c:/DTI/slopes/", "TAge.Schiz_Axial", -45, 45, 2);

            dset.removeLayer(1);
            layer2 = new ImageLayer3D((IImageData3D) il4.getData());
            layer2.getImageLayerParameters().getColorMap().setParameter(ragged);
            layer2.getImageLayerParameters().setScreenInterpolation(new InterpolationProperty(InterpolationHint.CUBIC));
            layer2.getImageLayerParameters().setResampleInterpolation(new InterpolationProperty(InterpolationHint.CUBIC));
            dset.addLayer(layer2);
            shooter = new SnapShooter(dset, AnatomicalVolume.getCanonicalAxial());

            shooter.shootContinuouslyAndSave("c:/DTI/slopes/", "TAge.Norm_Axial", -45, 45, 2);

            DiscreteColorMap ragged2 = new DiscreteColorMap(null);

            /*ragged2.addInterval(0, .5, new Color(0, 0, 0, 0));
            ragged2.addInterval(.5, 1, new Color(255, 26, 26, 50));
            ragged2.addInterval(1, 1.5, new Color(255, 50, 50, 75));
            ragged2.addInterval(1.5, 2, new Color(255, 77, 77, 100));
            ragged2.addInterval(2, 2.5, new Color(255, 100, 100, 125));
            ragged2.addInterval(2.5, 3, new Color(255, 128, 128, 150));
            ragged2.addInterval(3, 3.5, new Color(255, 150, 150, 175));
            ragged2.addInterval(3.5, 4, new Color(255, 170, 170, 225));
            ragged2.addInterval(4, 5, new Color(255, 200, 200, 255));


            ragged2.addInterval(-.5, 0, new Color(0, 0, 0, 0));
            ragged2.addInterval(-1, -.5, new Color(26, 26, 255, 50));
            ragged2.addInterval(-1.5, -1, new Color(50, 50, 255, 75));
            ragged2.addInterval(-2, -1.5, new Color(77, 77, 255, 100));
            ragged2.addInterval(-2.5, -2, new Color(100, 100, 255, 125));
            ragged2.addInterval(-3, -2.5, new Color(128, 128, 255, 150));
            ragged2.addInterval(-3.5, -3, new Color(150, 150, 255, 175));
            ragged2.addInterval(-4, -3.5, new Color(179, 179, 255, 225));
            ragged2.addInterval(-5, -4, new Color(200, 200, 255, 255));  */

            /*ragged2.addInterval(0, .5, new Color(0, 0, 0, 0));
            ragged2.addInterval(.5, 1, getHSB(.4f, 1f, 1f, 50));
            ragged2.addInterval(1, 1.5, getHSB(.35f, 1, 1, 75));
            ragged2.addInterval(1.5, 2, getHSB(.3f, 1, 1, 100));
            ragged2.addInterval(2, 2.5, getHSB(.25f, 1, 1, 125));
            ragged2.addInterval(2.5, 3, getHSB(.2f, 1, 1, 175));
            ragged2.addInterval(3, 3.5, getHSB(.15f, 1, 1, 200));
            ragged2.addInterval(3.5, 4, getHSB(.1f, 1, 1, 225));
            ragged2.addInterval(4, 5, getHSB(.05f, 1, 1, 255));


            ragged2.addInterval(-.5, 0, new Color(0, 0, 0, 0));
            ragged2.addInterval(-1, -.5, getHSB(.6f, 1f, 1f, 50));
            ragged2.addInterval(-1.5, -1, getHSB(.65f, 1f, 1f, 75));
            ragged2.addInterval(-2, -1.5, getHSB(.7f, 1f, 1f, 100));
            ragged2.addInterval(-2.5, -2, getHSB(.75f, 1f, 1f, 125));
            ragged2.addInterval(-3, -2.5, getHSB(.8f, 1f, 1f, 175));
            ragged2.addInterval(-3.5, -3, getHSB(.85f, 1f, 1f, 200));
            ragged2.addInterval(-4, -3.5, getHSB(.9f, 1f, 1f, 225));
            ragged2.addInterval(-5, -4, getHSB(.95f, 1f, 1f, 255));   */




            dset.removeLayer(1);
            layer2 = new ImageLayer3D((IImageData3D) il5.getData());
            layer2.getImageLayerParameters().getColorMap().setParameter(ragged2);
            layer2.getImageLayerParameters().setScreenInterpolation(new InterpolationProperty(InterpolationHint.CUBIC));
            layer2.getImageLayerParameters().setResampleInterpolation(new InterpolationProperty(InterpolationHint.CUBIC));
            dset.addLayer(layer2);
            shooter = new SnapShooter(dset, AnatomicalVolume.getCanonicalAxial());

            shooter.shootContinuouslyAndSave("c:/DTI/slopes/", "BAge.Schiz_Axial", -45, 45, 2);

            dset.removeLayer(1);
            layer2 = new ImageLayer3D((IImageData3D) il6.getData());
            layer2.getImageLayerParameters().getColorMap().setParameter(ragged2);
            layer2.getImageLayerParameters().setScreenInterpolation(new InterpolationProperty(InterpolationHint.CUBIC));
            layer2.getImageLayerParameters().setResampleInterpolation(new InterpolationProperty(InterpolationHint.CUBIC));
            dset.addLayer(layer2);
            shooter = new SnapShooter(dset, AnatomicalVolume.getCanonicalAxial());

            shooter.shootContinuouslyAndSave("c:/DTI/slopes/", "BAge.Norm_Axial", -45, 45, 2);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*public static void main(String[] args) {
   try {

       ImageDisplayModel dset = new ImageDisplayModel("snapper");
       ILoadableImage il1 = new MemoryImage(AnalyzeIO.readAnalyzeImage("/r/d5/despo/buchs/MSSM_TEMPLATE"));
       ILoadableImage il2 = new MemoryImage(AnalyzeIO.readAnalyzeImage("/r/d5/despo/buchs/TDiagnosis_G75.midbrain.Covar"));

       LinearColorMap lmap = new LinearColorMap(0, 221, ColorTable.GRAYSCALE);
       RaggedColorMap ragged = new RaggedColorMap();
       ragged.extendHigher(1.65, new Color(0, 0, 0, 0));
       ragged.extendHigher(2.6, new Color(102, 255, 0, 150));
       ragged.extendHigher(3.35, new Color(255, 255, 0, 200));
       ragged.extendHigher(4, new Color(255, 102, 0, 225));
       ragged.extendHigher(20, new Color(255, 0, 51, 255));

       ragged.extendLower(-1.65, new Color(0, 0, 0, 0));
       ragged.extendLower(-2.6, new Color(0, 102, 255, 150));
       ragged.extendLower(-3.35, new Color(51, 0, 255, 200));
       ragged.extendLower(-4, new Color(102, 0, 255, 225));
       ragged.extendLower(-20, new Color(204, 51, 255, 255));

       System.out.println(ragged);

       ImageLayer layer1 = new ImageLayer(il1.getData());
       layer1.getImageLayerParameters().getColorMap().setParameter(lmap);
       ImageLayer layer2 = new ImageLayer(il2.getData());
       layer2.getImageLayerParameters().getColorMap().setParameter(ragged);

       dset.addLayer(layer1);
       dset.addLayer(layer2);


       SnapShooter shooter = new SnapShooter(dset, AnatomicalVolume.getCanonicalAxial());
       shooter.shootContinuouslyAndSave("/r/d5/despo/buchs/", "TDiagnosis_Axial", -45, 45, 4);


   } catch (Exception e) {
       e.printStackTrace();
   }

} */



