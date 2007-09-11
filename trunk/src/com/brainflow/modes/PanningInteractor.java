package com.brainflow.modes;

import com.brainflow.core.ImageView;
import com.brainflow.image.anatomy.AnatomicalPoint2D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.display.Viewport3D;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 8, 2007
 * Time: 7:12:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class PanningInteractor extends ImageViewInteractor {

    private boolean dragging = false;

    private Point lastPoint;

    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && e.isControlDown()) {
            dragging = true;
            pan(e);
        }

    }


    public void mousePressed(MouseEvent e) {
        lastPoint = e.getPoint();
    }

    private void pan(MouseEvent e) {
        if (lastPoint != null) {

            ImageView view = getView();
            AnatomicalPoint2D lastpos = view.getSelectedPlot().translateScreenToAnat(SwingUtilities.convertPoint(e.getComponent(), lastPoint, view.getSelectedPlot().getComponent()));
            AnatomicalPoint2D curpos = view.getSelectedPlot().translateScreenToAnat(SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), view.getSelectedPlot().getComponent()));

            double dx = curpos.getX() - lastpos.getX();
            double dy = curpos.getY() - lastpos.getY();

            Anatomy3D anatomy = view.getSelectedPlot().getDisplayAnatomy();
            Viewport3D viewport = view.getViewport();
            viewport.setAxisMin(anatomy.XAXIS, viewport.getXAxisMin() + dx);
            viewport.setAxisMin(anatomy.YAXIS, viewport.getYAxisMin() + dy);

            lastPoint = e.getPoint();

        }

    }


    public void mouseReleased(MouseEvent e) {
        lastPoint = null;
        dragging = false;


    }
}
