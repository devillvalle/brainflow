package com.brainflow.application.actions;

import com.brainflow.application.presentation.CrosshairPresenter;
import com.brainflow.core.ImageView;
import com.brainflow.core.annotations.CrosshairAnnotation;
import com.brainflow.core.annotations.IAnnotation;
import org.bushe.swing.action.BasicAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 13, 2007
 * Time: 12:59:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrosshairDialogAction extends BasicAction {

    private static final Logger log = Logger.getLogger(CrosshairDialogAction.class.getName());

    protected void execute(ActionEvent actionEvent) throws Exception {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);


        if (view != null) {
            IAnnotation icross = view.getAnnotation(CrosshairAnnotation.class);
            if (icross != null) {
                log.finest("retrieved crosshair annotation for editing");
                CrosshairPresenter presenter = new CrosshairPresenter((CrosshairAnnotation) icross);
                Container c = JOptionPane.getFrameForComponent(view);
                JDialog dialog = new JDialog(JOptionPane.getFrameForComponent(view));
                Point p = c.getLocation();

                dialog.add(presenter.getComponent());
                dialog.pack();
                dialog.setLocation((int) (p.getX() + c.getWidth() / 2f), (int) (p.getY() + c.getHeight() / 2f));
                dialog.setVisible(true);
            }
        }

    }
}
