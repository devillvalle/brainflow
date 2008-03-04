package com.brainflow.application.actions;

import com.brainflow.colormap.AbstractColorBar;
import com.brainflow.colormap.IColorMap;
import com.brainflow.core.ImageView;
import org.bushe.swing.action.BasicAction;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 14, 2006
 * Time: 4:08:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class SaveColorBarCommand extends BrainFlowCommand {

    protected void handleExecute()  {
        ImageView view = getSelectedView();
        //To change body of implemented methods use File | Settings | File Templates.

        if (view != null) {
            int idx = view.getModel().getSelectedIndex();
            IColorMap cmap = view.getModel().getLayerParameters(idx).getColorMap();
            AbstractColorBar cbar = cmap.createColorBar();

            cbar.setOrientation(SwingUtilities.HORIZONTAL);
            cbar.setSize(500, 50);

            BufferedImage bimg = new BufferedImage(500, 50, BufferedImage.TYPE_3BYTE_BGR);
            cbar.paintComponent(bimg.createGraphics());

            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(view);

            if (res == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();

                try {
                    ImageIO.write(bimg, "png", f);
                } catch (IOException e) {
                    throw new RuntimeException("Error during attempt to save color bar image: " + f, e);
                }

            }
        }


    }

}