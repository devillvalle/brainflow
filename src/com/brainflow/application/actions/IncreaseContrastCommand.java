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
public class IncreaseContrastCommand extends BrainFlowCommand {

    private double percMultiplier = .07;


    public IncreaseContrastCommand() {
        super("increase-contrast");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();
        if (view != null) {
            AbstractLayer layer = view.getSelectedLayer();
            incrementContrast(layer);

        }

    }


    private void incrementContrast(AbstractLayer layer) {
        ImageLayerProperties props = layer.getImageLayerProperties();
        ClipRange clip = props.getClipRange();

        double highClip = clip.getHighClip();
        double lowClip = clip.getLowClip();

        double distance = highClip - lowClip;
        double increment = (percMultiplier * distance) / 2;

        double newHighClip = highClip - increment;
        double newLowClip = lowClip + increment;

        if (newLowClip >= newHighClip) {
            newLowClip = newHighClip - .001;
        }

        clip.setClipRange(newLowClip, newHighClip);


    }
}
