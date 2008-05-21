package com.brainflow.application.actions;

import com.brainflow.core.layer.AbstractLayer;
import com.brainflow.core.layer.ImageLayerProperties;
import com.brainflow.core.ImageView;
import com.brainflow.core.ClipRange;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 19, 2007
 * Time: 12:25:25 PM
 */
public class DecreaseContrastCommand extends BrainFlowCommand {

    private double percMultiplier = .07;


    public DecreaseContrastCommand() {
        super("decrease-contrast");
    }

    protected void handleExecute() {

        ImageView view = getSelectedView();
        if (view != null) {
            int idx = view.getSelectedLayerIndex();
            AbstractLayer layer = view.getModel().getLayer(idx);
            decrementContrast(layer);

        }

    }


    private void decrementContrast(AbstractLayer layer) {
        ImageLayerProperties props = layer.getImageLayerProperties();
        ClipRange clipRange = props.getClipRange();
        double highClip = clipRange.getHighClip();
        double lowClip = clipRange.getLowClip();

        double distance = highClip - lowClip;
        double increment = (percMultiplier * distance) / 2;

        double newHighClip = Math.min(highClip + increment, layer.getImageLayerProperties().getColorMap().getMaximumValue());
        double newLowClip = Math.max(lowClip - increment, layer.getImageLayerProperties().getColorMap().getMinimumValue());


        clipRange.setHighClip(newHighClip);
        clipRange.setLowClip(newLowClip);

    }


}
