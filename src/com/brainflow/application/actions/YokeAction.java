package com.brainflow.application.actions;

import org.bushe.swing.action.BasicAction;
import org.bushe.swing.action.ActionManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

import com.brainflow.core.ImageView;
import com.brainflow.core.ImageCanvas;
import com.brainflow.application.toplevel.ImageCanvasManager;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 23, 2007
 * Time: 11:48:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class YokeAction extends BasicAction {


    public YokeAction() {
      
    }


    protected void execute(ActionEvent evt) throws Exception {
        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);

        if (view != null) {
            ImageCanvas canvas = ImageCanvasManager.getInstance().getSelectedCanvas();
            List<ImageView> views = canvas.getViews();

            for (ImageView v : views) {
                if (v != view) {
                    ImageCanvasManager.getInstance().yoke(view, v);
                }
            }

        }

    }


}
