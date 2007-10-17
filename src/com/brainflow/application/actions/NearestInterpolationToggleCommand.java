package com.brainflow.application.actions;

import com.brainflow.application.toplevel.Brainflow;
import com.brainflow.core.ImageView;
import com.brainflow.display.InterpolationType;
import com.pietschy.command.toggle.ToggleCommand;
import com.pietschy.command.toggle.ToggleVetoException;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 12, 2007
 * Time: 10:31:56 PM
 */
public class NearestInterpolationToggleCommand extends ToggleCommand {

    public NearestInterpolationToggleCommand() {
        super("toggle-interp-nearest");
    }

    protected void handleSelection(boolean b) throws ToggleVetoException {
        if (b) {
            ImageView view = Brainflow.getInstance().getSelectedView();
            if (view != null) {
                view.setScreenInterpolation(InterpolationType.NEAREST_NEIGHBOR);
            }
        }
    }
}