package com.brainflow.application.actions;

import com.jidesoft.docking.DockableFrame;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 5, 2008
 * Time: 6:21:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActivateDockableFrameCommand extends BrainFlowCommand {

    DockableFrame dframe;

    public ActivateDockableFrameCommand(DockableFrame _dframe) {
        //super("activate-dock");
        dframe = _dframe;
        //getDefaultFace().setIcon(dframe.getIcon());


    }

    protected void handleExecute() {

         if (dframe.isHidden() || dframe.isDocked() || !dframe.isActive()) {
            dframe.getDockingManager().showFrame(dframe.getTitle());
        }
    }
}
