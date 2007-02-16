package com.brainflow.core.annotations;

import com.brainflow.core.IImagePlot;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Feb 16, 2007
 * Time: 3:02:59 PM
 */
public class SelectedAnnotation extends AbstractAnnotation {

    private IImagePlot selectedPlot;
    private Stroke stroke = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 10f, new float[]{2f, 2f}, 0f);
    private Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f);

    public SelectedAnnotation(IImagePlot selectedPlot) {
        this.selectedPlot = selectedPlot;
    }

    public void draw(Graphics2D g2d, Rectangle2D plotArea, IImagePlot plot) {
        if (plot == selectedPlot) {

            Paint oldPaint = g2d.getPaint();
            Stroke oldStroke = g2d.getStroke();
            Composite oldComposite = g2d.getComposite();

            g2d.setPaint(Color.WHITE);
            g2d.setStroke(stroke);
            g2d.setComposite(composite);
            g2d.draw(plotArea);

            g2d.setPaint(oldPaint);
            g2d.setStroke(oldStroke);
            g2d.setComposite(oldComposite);

        }

    }


    public IImagePlot getSelectedPlot() {
        return selectedPlot;
    }

    public void setSelectedPlot(IImagePlot selectedPlot) {
        this.selectedPlot = selectedPlot;
    }

    public IAnnotation safeCopy() {
        return this;
    }
}
