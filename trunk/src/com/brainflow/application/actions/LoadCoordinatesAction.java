package com.brainflow.application.actions;

import org.bushe.swing.action.BasicAction;

import java.awt.event.ActionEvent;
import java.awt.*;

import com.brainflow.application.presentation.wizards.LoadCoordinatesWizard;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 14, 2007
 * Time: 6:34:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoadCoordinatesAction extends BasicAction {

    protected void execute(ActionEvent evt) throws Exception {
        LoadCoordinatesWizard wizard = new LoadCoordinatesWizard();
        Container c = (Container)getContextValue(ActionContext.SELECTED_CANVAS);
        wizard.showWizard((JFrame)JOptionPane.getFrameForComponent(c));
    }
}
