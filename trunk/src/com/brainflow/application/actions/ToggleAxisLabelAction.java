package com.brainflow.application.actions;

import org.bushe.swing.action.BasicAction;
import org.bushe.swing.action.ActionManager;

import javax.swing.*;

import com.brainflow.core.ImageView;
import com.brainflow.core.annotations.CrosshairAnnotation;
import com.brainflow.core.annotations.AxisLabelAnnotation;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 28, 2007
 * Time: 11:52:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ToggleAxisLabelAction extends BasicAction {

     public ToggleAxisLabelAction() {
        putValue(Action.NAME, "Toggle Axis Label");
        putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
    }


    protected void contextChanged() {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);
        if (view != null) {
            setEnabled(true);
            AxisLabelAnnotation annot = (AxisLabelAnnotation)view.getAnnotation(AxisLabelAnnotation.class);
            boolean selected = annot.isVisible();
            setSelected(selected);
        } else {
            setEnabled(false);
        }
    }

    protected void execute(ActionEvent evt) throws Exception {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);

        if (view != null) {
            AxisLabelAnnotation annot = (AxisLabelAnnotation)view.getAnnotation(AxisLabelAnnotation.class);
            if (annot != null) {

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
