package com.brainflow.modes;

import com.brainflow.core.ImageCanvas;
import com.brainflow.core.ImageView;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class PlotSelectorMode extends ImageCanvasMode {


    public PlotSelectorMode(ImageCanvas _canvas) {
        super.setImageCanvas(_canvas);
    }



    public void mousePressed(MouseEvent event) {

        Point point = event.getPoint();
        ImageView view = canvas.whichView(point);

        if (view == null) {

            canvas.getCanvasSelection().clearSelection();
            canvas.setSelectedView(null);
        } else if (view == canvas.getSelectedView()) {

            return;
        } else if (view != null) {

            canvas.getCanvasSelection().clearPreSelection();
            canvas.getCanvasSelection().setSelectedView(event, view);

        } else {
           

        }

    }


    public void mouseReleased(MouseEvent event) {
    }

    public void mouseClicked(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mouseEntered(MouseEvent event) {
    }

    

}