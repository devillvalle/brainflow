package com.brainflow.application.actions;

import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockingManager;
import com.pietschy.command.toggle.ToggleVetoException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 5, 2008
 * Time: 6:21:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActivateDockableFrameCommand extends BrainFlowToggleCommand {

    DockableFrame dframe;

    public ActivateDockableFrameCommand(DockableFrame _dframe) {
        //super("activate-dock");
        dframe = _dframe;
        if (!dframe.isHidden()) {
            setSelected(true);
        } else {
            setSelected(false);
        }


    }

    protected void handleSelection(boolean b) throws ToggleVetoException {
        if (b) {
            if (dframe.isHidden() || dframe.isDocked()) {
                DockingManager dman = dframe.getDockingManager();
                if (dman != null)
                    dman.showFrame(dframe.getTitle());
            }
        } else {
            DockingManager dman = dframe.getDockingManager();
            dman.hideFrame(dframe.getName());

        }
    }

    
}
