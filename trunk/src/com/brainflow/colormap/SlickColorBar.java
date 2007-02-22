package com.brainflow.colormap;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 22, 2007
 * Time: 12:26:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class SlickColorBar extends AbstractColorBar {

    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 300;


    protected SlickColorBar(IColorMap _colorMap, int orientation) {
        super(_colorMap, orientation);
    }

    @Override
        protected BufferedImage renderOffscreen() {
            BufferedImage bimage = null;


            if (getOrientation() == SwingConstants.HORIZONTAL) {

                bimage = new BufferedImage(DEFAULT_WIDTH, 10, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D g2d = bimage.createGraphics();
                int ncolors = colorMap.getMapSize();
                double distance = colorMap.getMaximumValue() - colorMap.getMinimumValue();
                System.out.println("distance = " + distance);
                double start = colorMap.getMinimumValue();

                for (int i = 0; i < ncolors; i++) {
                    ColorInterval ci = colorMap.getInterval(i);
                    double size = ((ci.getMaximum() - ci.getMinimum()) / distance);
                    double xpos = (ci.getMinimum() - start) / distance;


                    Rectangle2D rect = new Rectangle2D.Double(xpos * DEFAULT_WIDTH, 0, size * DEFAULT_WIDTH, 10);
                    float trans = (float) ci.getAlpha();

                    AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, trans / 255f);

                    g2d.setComposite(comp);
                    g2d.setPaint(ci.getColor());
                    g2d.fill(rect);
                }
            } else {
                bimage = new BufferedImage(10, DEFAULT_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
                int ncolors = colorMap.getMapSize();
                double distance = colorMap.getMaximumValue() - colorMap.getMinimumValue();
                double start = colorMap.getMinimumValue();

                Graphics2D g2d = bimage.createGraphics();
                for (int i = 0; i < ncolors; i++) {
                    ColorInterval ci = colorMap.getInterval(i);
                    double size = ((ci.getMaximum() - ci.getMinimum()) / distance);
                    double ypos = (ci.getMinimum() - start) / distance;


                    Rectangle2D rect = new Rectangle2D.Double(0, ypos * DEFAULT_HEIGHT, 10, size * DEFAULT_HEIGHT);
                    float trans = (float) ci.getAlpha();

                    AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, trans / 255f);

                    g2d.setComposite(comp);
                    g2d.setPaint(ci.getColor());
                    g2d.fill(rect);

                }
            }

            cachedImage = bimage;
            return cachedImage;
        }


}
