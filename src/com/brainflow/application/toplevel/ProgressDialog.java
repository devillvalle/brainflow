package com.brainflow.application.toplevel;

import com.brainflow.application.services.ProgressEvent;
import com.jidesoft.dialog.JideOptionPane;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import javax.swing.*;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Feb 20, 2007
 * Time: 11:26:49 AM
 */
class ProgressDialog implements EventSubscriber {

    private JLabel messageLabel;
    private Class serviceClass;
    private JProgressBar progressBar;
    private JPanel mainPanel;
    private JideOptionPane optionPane;
    private String title = "";

    private JDialog dialog;

    public ProgressDialog(Class _serviceClass, String _startMessage, String _title) {

        serviceClass = _serviceClass;
        title = _title;
        EventBus.subscribe(serviceClass, this);
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);

        mainPanel = new JPanel();

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        messageLabel = new JLabel(_startMessage);
        mainPanel.add(messageLabel);
        mainPanel.add(Box.createVerticalStrut(3));
        mainPanel.add(progressBar);
        mainPanel.add(Box.createVerticalStrut(3));


        optionPane = new JideOptionPane(mainPanel, JOptionPane.INFORMATION_MESSAGE, 0,
                null, new Object[]{new JButton("Cancel")});
        optionPane.setTitle(title);
        //optionPane.setDetails("junk");


    }

    public JDialog createDialog() {
        if (dialog == null)
            dialog = optionPane.createDialog(title);

        return dialog;
    }

    public void onEvent(Object evt) {

        ProgressEvent event = (ProgressEvent) evt;
        progressBar.setValue(event.getProgress());
        messageLabel.setText(event.getMessage());

        if (event.getProgress() == progressBar.getMaximum()) {
            if (dialog != null)
                dialog.setVisible(false);
        }


    }


}
