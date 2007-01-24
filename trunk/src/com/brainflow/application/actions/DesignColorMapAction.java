package com.brainflow.application.actions;

import com.brainflow.application.presentation.ColorBandChartPresenter;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.core.ImageCanvas;
import com.brainflow.core.ImageView;
import com.jidesoft.dialog.JideOptionPane;
import org.bushe.swing.action.BasicAction;

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
        ImageCanvas canvas = (ImageCanvas) getContextValue(ActionContext.SELECTED_CANVAS);
        if (view != null) {

            int layer = view.getImageDisplayModel().getSelectedIndex();
            IColorMap oldMap = view.getImageDisplayModel().getImageLayer(layer).
                    getImageLayerParameters().getColorMap().getParameter();

            if (oldMap instanceof LinearColorMap) {
                LinearColorMap lmap = (LinearColorMap) oldMap;
                LinearColorMap copyMap = lmap.copy();

                ColorBandChartPresenter presenter = new ColorBandChartPresenter(view.getImageDisplayModel().getImageLayer(layer).
                        getImageLayerParameters().getColorMap());
                int ret = JideOptionPane.showOptionDialog(canvas, presenter.getComponent(), "Design Color Map", JideOptionPane.OK_CANCEL_OPTION, JideOptionPane.PLAIN_MESSAGE,
                        null, null, null);

                if (ret != JideOptionPane.OK_OPTION) {
                    view.getImageDisplayModel().getImageLayer(layer).
                            getImageLayerParameters().getColorMap().setParameter(copyMap);

                }

            }
        }


    }
}
