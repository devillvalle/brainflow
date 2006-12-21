package com.brainflow.colormap;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 13, 2006
 * Time: 11:51:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class RaggedColorBar extends AbstractColorBar {

    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 300;


    public RaggedColorBar(RaggedColorMap _colorMap, int orientation) {
        super(_colorMap, orientation);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Dimension d = this.getSize();
        int width = (int) d.getWidth();
        int height = (int) d.getHeight();

        g2.clearRect(0, 0, width, height);
        AffineTransform at = null;


        if (cachedImage == null) {
            renderOffscreen();
        }
        if (getOrientation() == SwingConstants.VERTICAL) {
            at = AffineTransform.getScaleInstance((double) width / 10f, (double) height / DEFAULT_HEIGHT);
        }

        if (getOrientation() == SwingConstants.HORIZONTAL) {
            at = AffineTransform.getScaleInstance((double) width / DEFAULT_WIDTH, (double) height / (double) 10);
        }


        drawBackground(g2);
        g2.drawRenderedImage(cachedImage, at);
    }


    @Override
    protected BufferedImage renderOffscreen() {
        BufferedImage bimage = null;

        RaggedColorMap raggedMap = (RaggedColorMap) colorMap;

        if (getOrientation() == SwingConstants.HORIZONTAL) {

            bimage = new BufferedImage(DEFAULT_WIDTH, 10, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2d = (Graphics2D) bimage.createGraphics();
            int ncolors = raggedMap.getMapSize();
            double distance = raggedMap.getRange().getInterval();
            double start = raggedMap.getMinimumValue();

            for (int i = 0; i < ncolors; i++) {
                ColorInterval ci = colorMap.getInterval(i);
                double size = ((ci.getMaximum() - ci.getMinimum()) / distance);
                double xpos = (ci.getMinimum() - start) / distance;

                System.out.println("i = " + i);
                System.out.println("color = " + ci);
                System.out.println("distance " + distance);
                System.out.println("size " + size);
                System.out.println("xpos " + xpos);
                Rectangle2D rect = new Rectangle2D.Double(xpos * DEFAULT_WIDTH, 0, size * DEFAULT_WIDTH, 10);
                float trans = (float) ci.getAlpha();
                System.out.println("rect " + rect);
                AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, trans / 255f);

                g2d.setComposite(comp);
                g2d.setPaint(ci.getColor());
                g2d.fill(rect);
            }
        } else {
            /*bimage = new BufferedImage(10, ncolors, BufferedImage.TYPE_4BYTE_ABGR);
            int row = 0;
            Graphics2D g2d = (Graphics2D) bimage.createGraphics();
            for (int i = (ncolors - 1); i >= 0; i--, row++) {

                Color clr = colorMap.getColor(samples[i]);
                float trans = (float) clr.getAlpha();
                AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, trans / 255f);
                g2d.setComposite(comp);
                g2d.setPaint(clr);
                g2d.drawRect(0, row, 10, 1);
            }*/
        }

        cachedImage = bimage;
        return cachedImage;
    }

    public Dimension getPreferredSize() {
        if (getOrientation() == SwingConstants.VERTICAL) {
            return new Dimension(75, 256);
        }

        return new Dimension(DEFAULT_WIDTH, 50);
    }

    public static void main(String[] args) {
        RaggedColorMap cmap = new RaggedColorMap();
        cmap.addInterval(-100, 0, Color.BLUE);
        cmap.extendHigher(56, Color.RED);
        cmap.extendHigher(324, Color.PINK);
        cmap.extendHigher(1200, Color.YELLOW);

        JPanel jp = cmap.createColorBar();

        JFrame jf = new JFrame();
        jf.add(jp);
        jf.pack();
        jf.setVisible(true);


    }
}
