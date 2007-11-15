package com.brainflow.application.actions;

import com.brainflow.core.AbstractLayer;
import com.brainflow.core.ImageLayerProperties;
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
            incrementContrast(layer);

        }

    }


    private void incrementContrast(AbstractLayer layer) {
        ImageLayerProperties props = layer.getImageLayerProperties();
        ClipRange clipRange = props.getClipRange();
        double highClip = clipRange.getHighClip();
        double lowClip = clipRange.getLowClip();

        double distance = highClip - lowClip;
        double increment = (percMultiplier * distance) / 2;

        double newHighClip = highClip + increment;
        double newLowClip = lowClip - increment;


        clipRange.setHighClip(newHighClip);
        clipRange.setLowClip(newLowClip);

    }


}
