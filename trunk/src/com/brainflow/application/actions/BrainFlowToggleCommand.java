package com.brainflow.application.actions;

import com.brainflow.application.toplevel.Brainflow;
import com.brainflow.core.BrainCanvas;
import com.brainflow.core.ImageView;
import com.pietschy.command.toggle.ToggleCommand;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 13, 2007
 * Time: 10:21:50 PM
 */
public abstract class BrainFlowToggleCommand extends ToggleCommand {

    protected BrainFlowToggleCommand() {
        super();
    }


    protected BrainFlowToggleCommand(String s) {
        super(s);
    }

    public ImageView getSelectedView() {
        return Brainflow.getInstance().getSelectedView();

    }

    public BrainCanvas getSelectedCanvas() {
        return Brainflow.getInstance().getSelectedCanvas();

    }


}
