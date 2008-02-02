package com.brainflow.application.actions;

import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMapDeprecated;
import com.brainflow.colormap.LinearColorMap2;
import com.brainflow.core.ImageView;
import com.brainflow.core.ImageLayer;
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

            ImageLayer layer = view.getSelectedLayer();

            IColorMap map = layer.getImageLayerProperties().getColorMap();

            LinearColorMap2 lmap = new LinearColorMap2(map.getMinimumValue(), map.getMaximumValue(), map.getLowClip(), map.getHighClip(), icm);
            layer.getImageLayerProperties().colorMap.set(lmap);


        }


    }


}
