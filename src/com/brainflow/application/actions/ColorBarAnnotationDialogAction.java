package com.brainflow.application.actions;

import com.brainflow.application.presentation.ColorBarAnnotationPresenter;
import com.brainflow.core.ImageView;
import com.brainflow.core.annotations.ColorBarAnnotation;
import com.jgoodies.forms.factories.ButtonBarFactory;
import org.bushe.swing.action.BasicAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 13, 2007
 * Time: 12:59:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColorBarAnnotationDialogAction extends BasicAction {

    private static final Logger log = Logger.getLogger(ColorBarAnnotationDialogAction.class.getName());

    private JDialog dialog;


    public ColorBarAnnotationDialogAction() {
        super();


    }

    protected void execute(ActionEvent actionEvent) throws Exception {

        final ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);


        if (view != null) {
            final ColorBarAnnotation cbar = (ColorBarAnnotation) view.getAnnotation(ColorBarAnnotation.class);
            //final ColorBarAnnotation safeCopy = (CrosshairAnnotation)icross.safeCopy();

            if (cbar != null) {
                log.finest("retrieved color bar annotation for editing");

                ColorBarAnnotationPresenter presenter = new ColorBarAnnotationPresenter(cbar);
                Container c = JOptionPane.getFrameForComponent(view);


                final JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {

                        dialog.setVisible(false);
                        dialog.dispose();
                    }
                });

                final JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        //view.setAnnotation(safeCopy);
                        //icross.setLinePaint(safeCopy.getLinePaint());
                        //icross.setLineWidth(safeCopy.getLineWidth());
                        //icross.setGap(safeCopy.getGap());
                        //icross.setVisible(safeCopy.isVisible());


                        dialog.setVisible(false);
                        dialog.dispose();
                    }
                });


                final JButton applyButton = new JButton("Apply");


                dialog = new JDialog(JOptionPane.getFrameForComponent(view));
                dialog.setLayout(new BorderLayout());


                Point p = c.getLocation();


                JPanel mainPanel = new JPanel();
                mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 8, 15, 8));
                mainPanel.setLayout(new BorderLayout());
                JPanel buttonPanel = ButtonBarFactory.buildRightAlignedBar(okButton, cancelButton, applyButton);

                mainPanel.add(presenter.getComponent(), BorderLayout.CENTER);
                mainPanel.add(buttonPanel, BorderLayout.SOUTH);

                dialog.add(mainPanel, BorderLayout.CENTER);
                dialog.pack();
                dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                dialog.setLocation((int) (p.getX() + c.getWidth() / 2f), (int) (p.getY() + c.getHeight() / 2f));
                dialog.setVisible(true);
            }
        }


    }
}
