package com.brainflow.application.actions;

import com.brainflow.core.ImageLayer;
import com.brainflow.display.ImageLayerParameters;
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

    private ImageLayerParameters layer;

    public LayerVisibilityAction(ImageLayerParameters _layer) {
        layer = _layer;
        putValue(Action.NAME, "Visible");
        putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_CHECKBOX);

        setSelected(layer.getVisiblility().getProperty().isVisible());
    }

    protected void execute(ActionEvent actionEvent) throws Exception {

        /// bound to action

    }


}
