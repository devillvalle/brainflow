package com.brainflow.application;

import com.brainflow.image.data.IImageData;
import com.brainflow.utils.ProgressListener;
import com.brainflow.application.services.LoadableImageProgressEvent;
import com.jidesoft.dialog.JideOptionPane;

import javax.swing.*;
import java.util.List;
import java.util.Arrays;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import org.bushe.swing.event.EventBus;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 20, 2007
 * Time: 10:44:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageProgressDialog extends SwingWorker<IImageData, Integer> implements ProgressListener {


    private int min;

    private int max;

    private int value;

    private String message = "";

    private ILoadableImage loadable;

    private JProgressBar progressBar;

    private JDialog dialog;

    public ImageProgressDialog(ILoadableImage _loadable) {
        loadable = _loadable;
        this.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("LOADING IMAGE : " + evt.getNewValue());
            }
        });

        buildGUI();
    }



    private void buildGUI() {
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Percent Complete: " + progressBar.getValue() + "%"));
        panel.add(Box.createVerticalStrut(3));
        panel.add(progressBar);
        panel.add(Box.createVerticalStrut(3));
        panel.add(new JLabel("Scranning C:\\Program Files\\...\\win.ini ..."));
        JideOptionPane optionPane = new JideOptionPane(panel, JOptionPane.INFORMATION_MESSAGE, 0, null, new Object[]{
                new JButton("Cancel")});

        optionPane.setTitle("Loading Image " + loadable.getStem());

        String details = ("Details are as Follows");
        optionPane.setDetails(details);

        dialog = optionPane.createDialog(JOptionPane.getFrameForComponent(progressBar), "Please wait");
        dialog.setResizable(true);
        dialog.setModal(false);
        dialog.pack();
       

    }

    public JDialog getDialog() {
        return dialog;
    }


    protected void process(List<Integer> chunks) {
        for (Integer i : chunks) {
            double perc = ((double)i)/getMax();
            int prog = (int)(perc*100f);
            System.out.println("processing " + prog);
            progressBar.setValue(prog);

        }
    }

    protected void done() {
        progressBar.setValue(100);
        //dialog.setVisible(false);
    }

    protected IImageData doInBackground() throws Exception {
        return loadable.load(this);
    }


    public void setValue(int val) {
        value = val;
        publish(val);

    }


    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public void setMinimum(int val) {
        min = val;
    }

    public void setMaximum(int val) {
        max = val;
    }


    public void setString(String _message) {
        message = _message;
        System.out.println(" " + message);

    }

    public void setIndeterminate(boolean b) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void finished() {
        //
    }
}