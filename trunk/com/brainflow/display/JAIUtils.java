/*
 * JAIUtils.java
 *
 * Created on February 27, 2003, 12:17 PM
 */

package com.brainflow.display;

import javax.media.jai.JAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.RenderedOp;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;

/**
 * @author Bradley
 */
public class JAIUtils {

    /**
     * Creates a new instance of JAIUtils
     */
    public JAIUtils() {
    }

    public static ParameterBlock getScaleParameterBlock(float sx, float sy, InterpolationProperty interp) {
        ParameterBlock pb = new ParameterBlock();
        pb.add(sx);
        pb.add(sy);
        pb.add(0f);
        pb.add(0f);
        pb.add(interp);

        return pb;
    }

    public static ParameterBlock getScaleParameterBlock(double sx, double sy, InterpolationProperty interp) {
        ParameterBlock pb = new ParameterBlock();
        pb.add((float) sx);
        pb.add((float) sy);
        pb.add(0f);
        pb.add(0f);
        pb.add(interp);

        return pb;
    }

    public static ParameterBlock getTranslateParameterBlock(double tx, double ty) {
        ParameterBlock pb = new ParameterBlock();
        pb.add((float) tx);
        pb.add((float) ty);
        return pb;
    }

    public static ParameterBlock getTranslateParameterBlock(float tx, float ty) {
        ParameterBlock pb = new ParameterBlock();
        pb.add(tx);
        pb.add(ty);
        return pb;
    }

    public static ParameterBlock getCropParameterBlock(Rectangle2D cropRect) {
        ParameterBlock pb = new ParameterBlock();
        pb.add((float) cropRect.getMinX());
        pb.add((float) cropRect.getMinY());
        pb.add((float) cropRect.getWidth());
        pb.add((float) cropRect.getHeight());
        return pb;
    }

    public static ParameterBlock createWindowLevelParameterBlock(int window, int level) {
        double low = level - window / 2.0;
        double high = level + window / 2.0;
        byte[][] lut = JAIUtils.createLookup(low, high);
        LookupTableJAI lookup = new LookupTableJAI(lut);
        ParameterBlock pb = new ParameterBlock();
        pb.add(lookup);
        return pb;
    }

    public static ParameterBlock createHighLowParameterBlock(int low, int high) {
        byte[][] lut = JAIUtils.createLookup(low, high);
        LookupTableJAI lookup = new LookupTableJAI(lut);
        ParameterBlock pb = new ParameterBlock();
        pb.add(lookup);
        return pb;
    }

    public static RenderedOp createWindowLevelOp(RenderedImage src, int window, int level) {
        ParameterBlock pb = JAIUtils.createWindowLevelParameterBlock(window, level);
        pb.addSource(src);
        return JAI.create("lookup", pb);
    }

    public static byte[][] createLookup(double low, double high) {
        double range = high - low;
        if (range <= 0) {
            range = 0;
        }


        byte lut[][] = new byte[3][256];
        int bands = 3;
        int value = 0;
        float idx = 1;
        for (int i = 0; i < 256; i++) {
            if (i < low) value = 0;
            else if (i > high) value = 255;
            else {
                value = (int) (idx / (float) range * 255);
                idx++;
            }
            if (value > 255) value = 255;
            lut[0][i] = (byte) value;
            lut[1][i] = (byte) value;
            lut[2][i] = (byte) value;
        }

        return lut;
    }

}



