package com.brainflow.application.actions;

import com.brainflow.core.ImageLayer;
import org.bushe.swing.action.ActionManager;
import org.bushe.swing.action.BasicAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Dec 25, 2006
 * Time: 11:56:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class LayerVisibilityAction extends BasicAction {

    private ImageLayer layer;

    public LayerVisibilityAction(ImageLayer _layer) {
        layer = _layer;
        putValue(Action.NAME, "Visible");
        putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_CHECKBOX);

        setSelected(layer.getImageLayerParameters().getVisiblility().getParameter().isVisible());
    }

    protected void execute(ActionEvent actionEvent) throws Exception {

        /// bound to action

    }


}
