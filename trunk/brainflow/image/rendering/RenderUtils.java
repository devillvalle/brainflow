package com.brainflow.image.rendering;

import com.brainflow.utils.ArrayUtils;

import javax.media.jai.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.awt.image.renderable.ParameterBlock;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class RenderUtils {

    public RenderUtils() {
    }

    public static RenderedImage createRGBAImage(byte[] rgba, int width, int height) {
        SampleModel sm = RasterFactory.createPixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, width, height, 4);

        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        //TiledImage tiledImage = new TiledImage(0, 0, width, height, 0, 0, sm, cm);
        DataBuffer buffer = new DataBufferByte(rgba, width * height);

        Raster raster = RasterFactory.createWritableRaster(sm, buffer, new Point(0, 0));

        // set the TiledImage data to that of the Raster
        //tiledImage.setData(raster);
        bimg.setData(raster);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gf = gd.getDefaultConfiguration();

        BufferedImage destImage = gf.createCompatibleImage((int) bimg.getWidth(), (int) bimg.getHeight());
        Graphics2D g2 = destImage.createGraphics();
        g2.drawRenderedImage(bimg, AffineTransform.getTranslateInstance(0, 0));

        //RenderedImageAdapter img = new RenderedImageAdapter((RenderedImage) tiledImage);
        return destImage;


    }

    public static RenderedImage createRGBAImage(byte[][] rgba, int width, int height) {


        SampleModel sm = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_BYTE,
                width,
                height,
                4);


        ColorModel cm = PlanarImage.createColorModel(sm);

        // create a TiledImage using the float SampleModel
        TiledImage tiledImage = new TiledImage(0, 0, width, height, 0, 0, sm, cm);
        DataBuffer buffer = new DataBufferByte(rgba, width * height);

        Raster raster = RasterFactory.createWritableRaster(sm, buffer, new Point(0, 0));

        // set the TiledImage data to that of the Raster
        tiledImage.setData(raster);

        RenderedImageAdapter img = new RenderedImageAdapter((RenderedImage) tiledImage);

        return img;
    }

    public static RenderedImage createSingleBandedImage(byte[] data, int width, int height) {

        WritableRaster raster = WritableRaster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                width, height, 1, new Point(0, 0));
        raster.setDataElements(0, 0, width, height, data);
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        bimg.setData(raster);


        return bimg;
    }


    public static RenderedImage insertColorMap(RenderedImage img, IndexColorModel icm) {
        if (!(img.getColorModel() instanceof IndexColorModel)) {
            throw new IllegalArgumentException("Image must have Color Model of class IndexColorModel!");
        }


        Raster raster = img.getData();
        TiledImage timg = new TiledImage(raster.getMinX(), raster.getMinY(), raster.getWidth(), raster.getHeight(), 0, 0,
                raster.getSampleModel(), icm);


        timg.setData(raster);

        return timg;

    }

    public static IndexColorModel createIndexColorModel(double[][] table) {
        int numbands = table.length;

        byte[] red = ArrayUtils.castToBytes(table[0]);
        byte[] green = ArrayUtils.castToBytes(table[1]);
        byte[] blue = ArrayUtils.castToBytes(table[2]);

        byte[] alpha = null;

        if (table.length < 4) {
            alpha = new byte[256];
            java.util.Arrays.fill(alpha, (byte) 255);
        } else {
            alpha = ArrayUtils.castToBytes(table[3]);
        }


        return new IndexColorModel(8, 256, red, green, blue, alpha);
    }


    public static RenderedImage createTiledImage(byte[] data, int width, int height, IndexColorModel icm) {
        WritableRaster raster = WritableRaster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                width, height, 1, new Point(0, 0));
        raster.setDataElements(0, 0, width, height, data);

        TiledImage timg = new TiledImage(raster.getMinX(), raster.getMinY(), raster.getWidth(), raster.getHeight(), 0, 0,
                raster.getSampleModel(), icm);

        timg.setData(raster);

        ImageLayout tileLayout = new ImageLayout(timg);
        tileLayout.setTileHeight(timg.getHeight());
        tileLayout.setTileWidth(timg.getWidth());
        RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, tileLayout);
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(timg);
        return JAI.create("format", pb, hints);


    }


    public static RenderedImage paletteToRGB(RenderedImage src, boolean hasAlpha) {
        if (hasAlpha)
            return RenderUtils.paletteToRGB(src);

        RenderedImage dst = null;
        if (src.getColorModel() instanceof IndexColorModel) {
            IndexColorModel icm = (IndexColorModel) src.getColorModel();
            byte[][] data = new byte[3][icm.getMapSize()];
            //System.out.println("num components = " + icm.getNumComponents());
            icm.getReds(data[0]);
            icm.getGreens(data[1]);
            icm.getBlues(data[2]);


            LookupTableJAI lut = new LookupTableJAI(data);

            dst = JAI.create("lookup", src, lut);
        } else {
            dst = src;
        }

        return dst;
    }


    public static RenderedImage paletteToRGB(RenderedImage src) {
        RenderedImage dst = null;

        if (src.getColorModel() instanceof IndexColorModel) {
            IndexColorModel icm = (IndexColorModel) src.getColorModel();
            byte[][] data = new byte[icm.getNumComponents()][icm.getMapSize()];
            //System.out.println("num components = " + icm.getNumComponents());
            icm.getReds(data[0]);
            icm.getGreens(data[1]);
            icm.getBlues(data[2]);

            if (icm.hasAlpha())
                icm.getAlphas(data[3]);


            LookupTableJAI lut = new LookupTableJAI(data);

            dst = JAI.create("lookup", src, lut);
        } else {
            dst = src;
        }

        return dst;
    }


    public static RenderedImage createImage(byte[] data, int width, int height, IndexColorModel icm) {

        RenderedImage dst = null;


        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, icm);

        WritableRaster wr = WritableRaster.createBandedRaster(DataBuffer.TYPE_BYTE,
                width, height, 1, new Point(0, 0));
        wr.setDataElements(0, 0, width, height, data);

        bi.setData(wr);
        dst = bi;

        return dst;

    }

    public static RenderedImage createImage(byte[] data, int width, int height, IndexColorModel icm, boolean hasAlpha) {
        if (hasAlpha) {
            return RenderUtils.createImage(data, width, height, icm);
        }

        //icm = org.lcbr.gui.GeneralLuts.removeAlphaBand(icm);
        RenderedImage dst = null;

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, icm);
        WritableRaster wr = WritableRaster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                width, height, 1, new Point(0, 0));
        wr.setDataElements(0, 0, width, height, data);
        bi.setData(wr);
        dst = bi;
        return dst;
    }

    public static BufferedImage createCompatibleImage(int width, int height) {
        GraphicsEnvironment local = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screen = local.getDefaultScreenDevice();
        GraphicsConfiguration config = screen.getDefaultConfiguration();
        return config.createCompatibleImage(width, height);
    }

    public static byte[][] extractTable(IndexColorModel icm) {
        byte[][] table = new byte[4][icm.getMapSize()];

        icm.getReds(table[0]);
        icm.getGreens(table[1]);
        icm.getBlues(table[2]);
        icm.getAlphas(table[3]);

        return table;
    }

}