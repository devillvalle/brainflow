package com.brainflow.application;

import com.brainflow.image.data.IImageData;
import com.brainflow.utils.ProgressListener;
import com.jidesoft.dialog.JideOptionPane;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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

    private IImageDataSource loadable;

    private JProgressBar progressBar;


    private JDialog dialog;


    private Component parent;


    public ImageProgressDialog(Component _parent) {
        parent = _parent;
        buildGUI();
    }

    public ImageProgressDialog(IImageDataSource _loadable, Component _parent) {
        loadable = _loadable;
        parent = _parent;
        buildGUI();
    }


    public IImageDataSource getLoadable() {
        return loadable;
    }

    public void setLoadable(IImageDataSource loadable) {
        this.loadable = loadable;
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
        panel.add(new JLabel("Scanning C:\\Program Files\\...\\win.ini ..."));
        JideOptionPane optionPane = new JideOptionPane(panel, JOptionPane.INFORMATION_MESSAGE, 0, null, new Object[]{
                new JButton("Cancel")});

        optionPane.setTitle("Loading Image ");

        String details = ("Details are as Follows");
        optionPane.setDetails(details);

        dialog = optionPane.createDialog(parent, "Loading Image");
        dialog.setModalityType(Dialog.ModalityType.MODELESS);
        dialog.pack();


    }

    public JDialog getDialog() {
        return dialog;

    }


    protected void process(List<Integer> chunks) {
        for (Integer i : chunks) {
            double perc = ((double) i) / getMax();
            int prog = (int) (perc * 100f);
            System.out.println("processing " + prog);
            progressBar.setValue(prog);

        }
    }

    protected void done() {

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
