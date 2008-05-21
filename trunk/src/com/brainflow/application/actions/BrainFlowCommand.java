package com.brainflow.application.actions;

import com.brainflow.application.toplevel.Brainflow;
import com.brainflow.core.BrainCanvas;
import com.brainflow.core.ImageView;
import com.brainflow.core.IBrainCanvas;
import com.pietschy.command.ActionCommand;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 19, 2007
 * Time: 12:28:23 PM
 */
public abstract class BrainFlowCommand extends ActionCommand {

    protected BrainFlowCommand() {
    }

    protected BrainFlowCommand(String s, boolean b) {
        super(s, b);
    }

    protected BrainFlowCommand(String s) {
        super(s);
    }

    public ImageView getSelectedView() {
        return Brainflow.getInstance().getSelectedView();

    }

    public IBrainCanvas getSelectedCanvas() {
        return Brainflow.getInstance().getSelectedCanvas();

    }


}
