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


    public LinearColorBar(LinearColorMap cmap, int orientation) {
        super(cmap, orientation);
        initListener(cmap);
        initBackground();


    }


    protected BufferedImage renderOffscreen() {
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
        LinearColorMap cmap = new LinearColorMap(-100, 100, ColorTable.SPECTRUM);
        JFrame frame = new JFrame();
        LinearColorBar cbar = new LinearColorBar(cmap, SwingConstants.HORIZONTAL);

        frame.add(cbar, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);


    }


}