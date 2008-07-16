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
public class LinearInterpolationToggleCommand extends BrainFlowToggleCommand {

    public LinearInterpolationToggleCommand() {
        super("toggle-interp-linear");
    }

    protected void handleSelection(boolean b) throws ToggleVetoException {
        if (b) {
            ImageView view = getSelectedView();
            if (view != null && view.getScreenInterpolation() != InterpolationType.LINEAR) {
                view.setScreenInterpolation(InterpolationType.LINEAR);
            }
        }

    }

    public void viewSelected(ImageView view) {
        if (view.getScreenInterpolation() == InterpolationType.LINEAR) {
            setSelected(true);
        }

    }
}
