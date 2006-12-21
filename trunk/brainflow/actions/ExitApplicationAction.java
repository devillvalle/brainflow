/*
 * OpenAnalyzeAction.java
 *
 * Created on February 25, 2003, 3:34 PM
 */

package com.brainflow.actions;

import org.bushe.swing.action.BasicAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Bradley
 */
public class ExitApplicationAction extends BasicAction {


    /**
     * Creates a new instance of OpenAnalyzeAction
     */
    public ExitApplicationAction() {

    }


    protected void execute(ActionEvent actionEvent) throws Exception {
        JFrame jframe = (JFrame) getContextValue(ActionContext.APPLICATION_FRAME);
        if (jframe != null) {
            jframe.setVisible(false);
            jframe.dispose();
        }

        System.exit(0);
    }
}



