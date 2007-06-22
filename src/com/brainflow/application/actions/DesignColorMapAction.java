package com.brainflow.application.actions;

import com.brainflow.application.presentation.ColorBandChartPresenter;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.core.ImageView;
import com.brainflow.core.ImageCanvas2;
import com.jidesoft.dialog.JideOptionPane;
import org.bushe.swing.action.BasicAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Dec 24, 2006
 * Time: 3:12:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class DesignColorMapAction extends BasicAction {

    public DesignColorMapAction(String string) {
        super(string);

    }

    protected void execute(ActionEvent actionEvent) throws Exception {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);
        ImageCanvas2 canvas = (ImageCanvas2) getContextValue(ActionContext.SELECTED_CANVAS);
        if (view != null) {

            int layer = view.getModel().getSelectedIndex();
            IColorMap oldMap = view.getModel().getLayer(layer).
                    getImageLayerProperties().getColorMap().getProperty();

            if (oldMap instanceof LinearColorMap) {
                LinearColorMap lmap = (LinearColorMap) oldMap;
                LinearColorMap copyMap = lmap.copy();

                ColorBandChartPresenter presenter = new ColorBandChartPresenter(view.getModel().getLayer(layer).
                        getImageLayerProperties().getColorMap());
                int ret = JOptionPane.showOptionDialog(canvas, presenter.getComponent(), "Design Color Map", JideOptionPane.OK_CANCEL_OPTION, JideOptionPane.PLAIN_MESSAGE,
                        null, null, null);

                if (ret != JOptionPane.OK_OPTION) {
                    view.getModel().getLayer(layer).
                            getImageLayerProperties().getColorMap().setProperty(copyMap);

                }

            }
        }


    }
}
