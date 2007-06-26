package com.brainflow.core.annotations;

import com.brainflow.core.IImagePlot;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 24, 2007
 * Time: 9:25:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class SliceAnnotation extends AbstractAnnotation {


    public static final String ID = "slice label";

    private Color fontColor = Color.WHITE;

    private Font font = new Font("helvetica", Font.BOLD, 13);


    public void draw(Graphics2D g2d, Rectangle2D plotArea, IImagePlot plot) {
        if (!isVisible()) return;

        g2d.setColor(fontColor);
        g2d.setFont(font);

        FontMetrics fmetric = g2d.getFontMetrics(font);

        String label = "" + (int)plot.getSlice().getX();
        Rectangle2D strBounds = fmetric.getStringBounds(label, g2d);

        g2d.drawString("" + (int)plot.getSlice().getX(), (int)(plotArea.getX() + (plotArea.getWidth()/2) - strBounds.getWidth()), (int)plotArea.getY() -2);
        

    }

    public IAnnotation safeCopy() {
        return this;
    }

    public String getIdentifier() {
        return ID;
    }
}
