package com.brainflow.core;

import com.brainflow.application.ILoadableImage;
import com.brainflow.application.MemoryImage;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.colormap.DiscreteColorMap;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.io.analyze.AnalyzeIO;

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


    private int width;

    private int height;


    public SnapShooter(IImageDisplayModel _dset, AnatomicalVolume _displayAnatomy) {
        dset = _dset;
        setDisplayAnatomy(_displayAnatomy);

    }

    public void setDisplayAnatomy(AnatomicalVolume _displayAnatomy) {
        displayAnatomy = _displayAnatomy;

        emulator = new ComponentImagePlot(dset, displayAnatomy, dset.getImageAxis(displayAnatomy.XAXIS).getRange(),
                dset.getImageAxis(displayAnatomy.YAXIS).getRange());


        width = (int) dset.getImageAxis(displayAnatomy.XAXIS).getRange().getInterval() * 2;
        height = (int) dset.getImageAxis(displayAnatomy.YAXIS).getRange().getInterval() * 2;

    }


    public ImageIcon shootLayer(double slice, int layer, int width, int height) {
       return null;

    }

    public RenderedImage shoot(double slice) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gf = gd.getDefaultConfiguration();

        BufferedImage img = gf.createCompatibleImage(width, height);

        //procurer.setSlice(new AnatomicalPoint1D(displayAnatomy.ZAXIS, slice));
        //emulator.paint(img.createGraphics(), new Rectangle(0, 0, width, height));
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
       layer1.getImageLayerProperties().getColorMap().setProperty(lmap);
       ImageLayer layer2 = new ImageLayer(il2.getData());
       layer2.getImageLayerProperties().getColorMap().setProperty(ragged);

       dset.addLayer(layer1);
       dset.addLayer(layer2);


       SnapShooter shooter = new SnapShooter(dset, AnatomicalVolume.getCanonicalAxial());
       shooter.shootContinuouslyAndSave("/r/d5/despo/buchs/", "TDiagnosis_Axial", -45, 45, 4);


   } catch (Exception e) {
       e.printStackTrace();
   }

} */



