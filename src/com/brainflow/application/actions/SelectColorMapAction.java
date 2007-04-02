package com.brainflow.application.actions;

import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.core.ImageView;
import org.bushe.swing.action.BasicAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.IndexColorModel;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 6, 2006
 * Time: 1:01:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class SelectColorMapAction extends BasicAction {

    private IndexColorModel icm;

    public SelectColorMapAction(String string, Icon icon, IndexColorModel icm) {
        super(string, icon);
        this.icm = icm;
    }

    protected void execute(ActionEvent actionEvent) throws Exception {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);
        if (view != null) {

            int layer = view.getModel().getSelectedIndex();
            IColorMap map = view.getModel().getImageLayer(layer).
                    getImageLayerProperties().getColorMap().getProperty();

            LinearColorMap lmap = new LinearColorMap(map.getMinimumValue(), map.getMaximumValue(), icm);
            if (map instanceof LinearColorMap) {
                LinearColorMap lmap2 = (LinearColorMap) map;

                lmap.setHighClip(lmap2.getHighClip());
                lmap.setLowClip(lmap2.getLowClip());
               
            }

            view.getModel().getImageLayer(layer).
                    getImageLayerProperties().getColorMap().setProperty(lmap);


        }


    }


}
