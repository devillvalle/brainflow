package com.brainflow.application.actions;

import org.bushe.swing.action.BasicAction;

import java.awt.event.ActionEvent;
import java.util.List;

import com.brainflow.core.ImageView;
import com.brainflow.core.BrainCanvas;
import com.brainflow.application.toplevel.BrainCanvasManager;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 23, 2007
 * Time: 11:48:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnyokeAction extends BasicAction {


    public UnyokeAction() {
        
    }


    protected void execute(ActionEvent evt) throws Exception {
        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);

        if (view != null) {
            BrainCanvas canvas = BrainCanvasManager.getInstance().getSelectedCanvas();
            List<ImageView> views = canvas.getViews();

            for (ImageView v : views) {
                if (v != view) {
                    BrainCanvasManager.getInstance().unyoke(view, v);
                }
            }
        }

    }


}
