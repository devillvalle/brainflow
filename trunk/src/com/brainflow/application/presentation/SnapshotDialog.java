package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.SnapshotForm;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 24, 2007
 * Time: 3:58:09 PM
 */
public class SnapshotDialog extends JDialog {


    private SnapshotForm form;

    private BufferedImage snapshot;


    public SnapshotDialog(BufferedImage snapshot) {
        this.snapshot = snapshot;
        buildGUI();
    }

    private void buildGUI() {
        setLayout(new BorderLayout());
        form = new SnapshotForm(snapshot);
        add(form, BorderLayout.CENTER);

        //ButtonPanel bp = new ButtonPanel();
        //bp.setAlignment(1);
        //bp.addButton(new JButton("Dismiss"));
        //add(bp, BorderLayout.SOUTH);

    }


    public static void main(String[] args) {
        try {

            UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
            BufferedImage bimg = ImageIO.read(new File("c:/lddmm/lddmm-coronal-slice-neg-5-shot1.png"));
            SnapshotDialog form = new SnapshotDialog(bimg);

            JFrame frame = new JFrame();
            form.setVisible(true);
            form.pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class SaveAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {

        }
    }


}
