package com.brainflow.application.actions;

import javax.swing.*;

import com.brainflow.core.ImageView;
import com.brainflow.core.annotations.CrosshairAnnotation;
import com.brainflow.core.annotations.ColorBarAnnotation;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 28, 2007
 * Time: 11:52:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ToggleColorbarAction extends BasicAction {

     public ToggleColorbarAction() {
        putValue(Action.NAME, "Toggle Colorbar");
        //putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
    }


    protected void contextChanged() {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);
        if (view != null) {
            setEnabled(true);
            ColorBarAnnotation annot = (ColorBarAnnotation)view.getAnnotation(view.getSelectedPlot(), ColorBarAnnotation.ID);
            if (annot != null) {
                boolean selected = annot.isVisible();
                setSelected(selected);
            } else {
                setSelected(false);
            }
        } else {
            setEnabled(false);
        }
    }

    protected void execute(ActionEvent evt) throws Exception {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);
        // wow is this ugly
        //todo clean up method
        if (view != null) {
            ColorBarAnnotation annot = (ColorBarAnnotation)view.getAnnotation(view.getSelectedPlot(), ColorBarAnnotation.ID);
            if (annot != null) {
                if (isSelected()) {
                    annot.setVisible(true);
                }
                else {
                    annot.setVisible(false);
                }
            } else {
                annot = new ColorBarAnnotation(view.getModel());
                view.setAnnotation(view.getSelectedPlot(), ColorBarAnnotation.ID, annot);
                if (isSelected()) {
                    annot.setVisible(true);
                }
                else {
                    annot.setVisible(false);
                }
            }
        }


    }
}
