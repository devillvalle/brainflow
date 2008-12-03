package com.brainflow.core.annotations;

import com.brainflow.core.IImagePlot;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.space.IImageSpace3D;
import com.brainflow.image.space.Axis;

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

        AnatomicalPoint3D pt = plot.getSlice();

        //todo hack cast
        IImageSpace3D space = (IImageSpace3D)plot.getModel().getImageSpace();
        pt = AnatomicalPoint3D.convertToWorld(pt, space);
        AnatomicalAxis zaxis = Anatomy3D.REFERENCE_ANATOMY.matchAxis(plot.getDisplayAnatomy().ZAXIS);

        String label = "" + (int)Math.round(pt.getValue(zaxis).getValue());
        Rectangle2D strBounds = fmetric.getStringBounds(label, g2d);

        g2d.drawString(label, (int)(plotArea.getX() + (plotArea.getWidth()/2) - strBounds.getWidth()), (int)plotArea.getY() -2);
        

    }

    public IAnnotation safeCopy() {
        return this;
    }

    public String getIdentifier() {
        return ID;
    }
}
