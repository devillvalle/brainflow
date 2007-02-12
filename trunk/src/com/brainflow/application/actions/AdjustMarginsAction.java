package com.brainflow.application.actions;

import org.bushe.swing.action.BasicAction;
import org.bushe.swing.action.ActionManager;


import javax.swing.*;

import com.brainflow.core.ImageView;
import com.brainflow.core.IImagePlot;
import com.brainflow.core.annotations.ColorBarAnnotation;
import com.brainflow.core.annotations.CrosshairAnnotation;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jidesoft.combobox.InsetsComboBox;
import com.jidesoft.combobox.PopupPanel;
import com.jidesoft.combobox.InsetsChooserPanel;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.logging.Logger;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 11, 2007
 * Time: 7:22:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdjustMarginsAction extends BasicAction {

    private static final Logger log = Logger.getLogger(AdjustMarginsAction.class.getName());
    private JDialog dialog;


    public AdjustMarginsAction() {
        putValue(Action.NAME, "Adjust Margins");
    }

    protected void contextChanged() {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);
        if (view != null) {
            setEnabled(true);


        } else {
            setEnabled(false);
        }
    }


    protected void execute(ActionEvent evt) throws Exception {
        super.execute(evt);    //To change body of overridden methods use File | Settings | File Templates.

        final ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);

        if (view != null) {
            final IImagePlot plot = view.getPlots().get(0);
            Insets oldInsets = (Insets) plot.getPlotInsets().clone();

            InsetsComboBox ipanel = new InsetsComboBox(plot.getPlotInsets());

            final InsetsChooserPanel panel = (InsetsChooserPanel)ipanel.createPopupComponent();
            panel.setSelectedInsets(oldInsets);
            Container c = JOptionPane.getFrameForComponent(view);


            AbstractAction okAction = new AbstractAction("OK") {

                public void actionPerformed(ActionEvent e) {
                    plot.setPlotInsets(panel.getSelectedInsets());
                    view.repaint();
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            };

            AbstractAction cancelAction = new AbstractAction("Cancel") {

                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            };

            panel.setOkAction(okAction);
            panel.setCancelAction(cancelAction);



            panel.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    System.out.println("insets event: " + evt.getNewValue());
                }
            });
           
            panel.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    //System.out.println("item changed: " + e.getItem());
                }
            });

            dialog = new JDialog(JOptionPane.getFrameForComponent(view));
            dialog.setLayout(new BorderLayout());

            Point p = c.getLocation();
            JPanel mainPanel = new JPanel();
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 8, 15, 8));
            mainPanel.setLayout(new BorderLayout());

            mainPanel.add(panel, BorderLayout.CENTER);

            dialog.add(mainPanel, BorderLayout.CENTER);
            dialog.pack();
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setLocation((int) (p.getX() + c.getWidth() / 2f), (int) (p.getY() + c.getHeight() / 2f));
            dialog.setVisible(true);   
            
        } else {
            setEnabled(false);
        }
    }


}
