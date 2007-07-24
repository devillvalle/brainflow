package com.brainflow.application.actions;

import com.brainflow.application.toplevel.Brainflow;
import com.brainflow.core.ImageCanvas2;
import com.brainflow.core.ImageView;
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

    public ImageCanvas2 getSelectedCanvas() {
        return Brainflow.getInstance().getSelectedCanvas();

    }


}
