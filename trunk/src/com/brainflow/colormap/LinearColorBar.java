package com.brainflow.colormap;

import com.brainflow.image.LinearSet1D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Apr 28, 2005
 * Time: 8:05:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class LinearColorBar extends AbstractColorBar {


    public LinearColorBar(IColorMap cmap, int orientation) {
        super(cmap, orientation);

    }

    private int computeStartIndex(IColorMap model) {
        int startIndex = 0;

        if (!(model.getLowClip() > model.getMinimumValue())) {
            startIndex = 1;
        }

        return startIndex;

    }

    private int computeEndIndex(IColorMap model) {
        int endIndex = model.getMapSize() - 1;
        if (!(model.getHighClip() < model.getMaximumValue())) {
            endIndex = endIndex - 1;
        }

        return endIndex;


    }

    private float[] getFractions() {
        //todo allow for the fact that the first and last intervals MIGHT have a fractional size of 0.

        IColorMap model = getColorMap();

        int startIndex = computeStartIndex(model);
        int endIndex = computeEndIndex(model);

        float[] frac = new float[endIndex - startIndex + 1];


        double cRange = model.getMaximumValue() - model.getMinimumValue();

        //System.out.println("start index : " + startIndex);
        //System.out.println("end index : " + endIndex);

        float lastFrac = -1;
        for (int i = startIndex; i <= endIndex; i++) {

            ColorInterval ci = model.getInterval(i);
            double max = ci.getMaximum();
            double diff = max - model.getMinimumValue();
            float f = (float) (diff / cRange);


            frac[i - startIndex] = f;

            assert f > lastFrac : "bin index : " + i + " bin max " + max + " bin diff " + diff + " frac " + f;

            lastFrac=f;
        }

        //System.out.println("high clip : " + model.getHighClip());
        ///System.out.println("last getValue = " + frac[model.getMapSize()-1]);
        //System.out.println("second to last getValue = " + frac[model.getMapSize()-2]);

        return frac;

    }


    private Color[] getColors() {
        IColorMap model = getColorMap();
        int startIndex = computeStartIndex(model);
        int endIndex = computeEndIndex(model);

        Color[] clrs = new Color[endIndex - startIndex + 1];

        for (int i = startIndex; i <= endIndex; i++) {

            ColorInterval ci = model.getInterval(i);
            clrs[i - startIndex] = ci.getColor();

        }


        return clrs;


    }

    private Paint createGradientPaint(int length) {
        Paint paint = null;

        if (getColorMap().getMapSize() == 1) {
            paint = getColorMap().getInterval(0).getColor();
        } else if (getOrientation() == SwingConstants.HORIZONTAL) {
            // todo check for correct increasing fractions

            paint = new LinearGradientPaint(0f, 0f, (float) length, (float) 0, getFractions(), getColors());


        } else {
            paint = new LinearGradientPaint(0f, 0f, (float) 0, (float) length, getFractions(), getColors());
        }


        return paint;

    }

    //@Override
    protected BufferedImage renderOffscreen() {

        Paint p;
        int ncolors = getColorMap().getMapSize();
        BufferedImage bimage;
        Rectangle fillRect;

        if (getOrientation() == SwingConstants.HORIZONTAL) {
            p = createGradientPaint(ncolors);
            fillRect = new Rectangle(0, 0, ncolors, 10);
            bimage = new BufferedImage(ncolors, 10, BufferedImage.TYPE_4BYTE_ABGR);
        } else {
            p = createGradientPaint(ncolors);
            fillRect = new Rectangle(0, 0, 10, ncolors);
            bimage = new BufferedImage(10, ncolors, BufferedImage.TYPE_4BYTE_ABGR);

        }


        Graphics2D g2 = bimage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(p);

        g2.fill(fillRect);

        cachedImage = bimage;
        return cachedImage;
    }


    protected BufferedImage renderOffscreen2() {
        BufferedImage bimage;
        LinearSet1D set = new LinearSet1D(colorMap.getMinimumValue(), colorMap.getMaximumValue(), colorMap.getMapSize());
        double[] samples = set.getSamples();
        int ncolors = samples.length;
        if (getOrientation() == SwingConstants.HORIZONTAL) {
            bimage = new BufferedImage(ncolors, 10, BufferedImage.TYPE_4BYTE_ABGR);

            Graphics2D g2d = (Graphics2D) bimage.createGraphics();

            for (int i = 0; i < ncolors; i++) {
                Color clr = colorMap.getColor(samples[i]);
                float trans = (float) clr.getAlpha();

                AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, trans / 255f);

                g2d.setComposite(comp);
                g2d.setPaint(clr);
                g2d.drawRect(i, 0, 1, 10);
            }
        } else {
            bimage = new BufferedImage(10, ncolors, BufferedImage.TYPE_4BYTE_ABGR);
            int row = 0;
            Graphics2D g2d = (Graphics2D) bimage.createGraphics();
            for (int i = (ncolors - 1); i >= 0; i--, row++) {

                Color clr = colorMap.getColor(samples[i]);
                float trans = (float) clr.getAlpha();
                AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, trans / 255f);
                g2d.setComposite(comp);
                g2d.setPaint(clr);
                g2d.drawRect(0, row, 10, 1);
            }
        }

        cachedImage = bimage;
        return cachedImage;

    }


    public Dimension getPreferredSize() {
        if (getOrientation() == SwingConstants.VERTICAL) {
            return new Dimension(75, 256);
        }

        return new Dimension(256, 50);
    }


    public static void main(String[] args) {
        LinearColorMapDeprecated cmap = new LinearColorMapDeprecated(-100, 100, ColorTable.SPECTRUM);
        JFrame frame = new JFrame();
        LinearColorBar cbar = new LinearColorBar(cmap, SwingConstants.HORIZONTAL);

        frame.add(cbar, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);


    }


}
