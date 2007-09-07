package com.brainflow.application.actions;

import com.brainflow.application.presentation.ColorBandChartPresenter;
import com.brainflow.colormap.IColorMap;
import com.brainflow.core.ImageCanvas2;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageView;
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


    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceModerateLookAndFeel());


            JFrame frame = new JFrame();
            frame.setVisible(true);
            frame.setSize(800, 800);


            int ret = JOptionPane.showOptionDialog(frame, new JPanel(), "Option", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, null, null);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public DesignColorMapAction(String string) {
        super(string);

    }

    protected void execute(ActionEvent actionEvent) throws Exception {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);
        ImageCanvas2 canvas = (ImageCanvas2) getContextValue(ActionContext.SELECTED_CANVAS);
        if (view != null) {

            //int layer = view.getModel().getSelectedLayerIndex();
            ImageLayer layer = view.getModel().getSelectedLayer();
            IColorMap oldMap = layer.getImageLayerProperties().getColorMap().getProperty();

            //if (oldMap instanceof LinearColorMap) {
            // todo fix me
            //IColorMap copyMap = oldMap.copy();

            ColorBandChartPresenter presenter = new ColorBandChartPresenter(layer.
                    getImageLayerProperties().getColorMap());
            int ret = JOptionPane.showOptionDialog(JOptionPane.getFrameForComponent(presenter.getComponent()), presenter.getComponent(), "Design Color Map", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, null, null);

            if (ret != JOptionPane.OK_OPTION) {
                layer.getImageLayerProperties().getColorMap().setProperty(oldMap);

            }

            // }
        }


    }
}
