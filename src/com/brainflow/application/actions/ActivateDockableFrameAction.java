package com.brainflow.application.actions;

import org.bushe.swing.action.BasicAction;
import org.bushe.swing.action.ActionManager;
import com.jidesoft.docking.DockableFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 28, 2007
 * Time: 11:14:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActivateDockableFrameAction extends BasicAction {

    private DockableFrame dframe;

    public ActivateDockableFrameAction(DockableFrame _dframe) {
        //super();
        dframe = _dframe;
        putValue(Action.NAME, dframe.getTitle());
        putValue(Action.SMALL_ICON, dframe.getIcon());
       
    }


    protected void execute(ActionEvent evt) throws Exception {
        if (dframe.isHidden()) {
            dframe.getDockingManager().showFrame(dframe.getTitle());
        }
        
    }
}
