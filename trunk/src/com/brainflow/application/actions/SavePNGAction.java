/*
 * OpenAnalyzeAction.java
 *
 * Created on February 25, 2003, 3:34 PM
 */

package com.brainflow.application.actions;

import com.brainflow.application.toplevel.BrainCanvasManager;
import com.brainflow.core.ImageView;
import com.brainflow.core.BrainCanvas;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Bradley
 */
public class SavePNGAction extends AbstractAction {

    Component parent;

    /**
     * Creates a new instance of OpenAnalyzeAction
     */
    public SavePNGAction(Component _parent) {
        // temporary hack
        parent = _parent;
        putValue(Action.NAME, "Save PNG");
        putValue(Action.SHORT_DESCRIPTION, "Save selected plot as PNG image");
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("resources/icons/save_edit.gif")));

    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {

        BrainCanvas canvas = BrainCanvasManager.getInstance().getSelectedCanvas();
        ImageView view = canvas.getSelectedView();

        if (view == null) {
            JOptionPane.showMessageDialog(parent, "Error: Need to select an image view before saving.");
            return;
        }


        JFileChooser chooser = new JFileChooser();
        int option = chooser.showSaveDialog(JOptionPane.getFrameForComponent(parent));
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            RenderedImage img = view.captureImage();
            try {
                ImageIO.write(img, "png", file);
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
        }
    }


}



