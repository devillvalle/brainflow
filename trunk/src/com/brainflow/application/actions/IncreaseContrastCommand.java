package com.brainflow.application.actions;

import com.brainflow.colormap.AbstractColorMap;
import com.brainflow.colormap.IColorMap;
import com.brainflow.core.AbstractLayer;
import com.brainflow.core.ImageLayerProperties;
import com.brainflow.core.ImageView;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 19, 2007
 * Time: 12:25:25 PM
 */
public class IncreaseContrastCommand extends BrainFlowCommand {

    private double percMultiplier = .07;


    public IncreaseContrastCommand() {
        super("increase-contrast");
    }

    protected void handleExecute() {

        ImageView view = getSelectedView();
        if (view != null) {
            int idx = view.getSelectedIndex();
            AbstractLayer layer = view.getModel().getLayer(idx);
            incrementContrast(layer);

        }

    }


    private void incrementContrast(AbstractLayer layer) {
        ImageLayerProperties props = layer.getImageLayerProperties();
        IColorMap colorMap = props.getColorMap().getProperty();
        if (colorMap instanceof AbstractColorMap) {
            AbstractColorMap absmap = (AbstractColorMap) colorMap;
            double highClip = absmap.getHighClip();
            double lowClip = absmap.getLowClip();

            double distance = highClip - lowClip;
            double increment = (percMultiplier * distance) / 2;

            double newHighClip = highClip - increment;
            double newLowClip = lowClip + increment;

            if (newLowClip >= newHighClip) {
                newLowClip = newHighClip - .001;
            }

            absmap.setHighClip(newHighClip);
            absmap.setLowClip(newLowClip);

        }


    }
}
