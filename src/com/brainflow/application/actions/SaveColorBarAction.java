package com.brainflow.application.actions;

import org.bushe.swing.action.BasicAction;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import com.brainflow.core.ImageView;
import com.brainflow.core.ImageCanvas;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.AbstractColorBar;

import javax.swing.*;
import javax.imageio.ImageIO;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 14, 2006
 * Time: 4:08:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class SaveColorBarAction extends BasicAction {


    protected void execute(ActionEvent actionEvent) throws Exception {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);

        if (view != null) {
            int idx = view.getImageDisplayModel().getSelectedIndex();
            IColorMap cmap = view.getImageDisplayModel().getLayerParameters(idx).getColorMap().getParameter();
            AbstractColorBar cbar = cmap.createColorBar();

            cbar.setOrientation(SwingUtilities.HORIZONTAL);
            cbar.setSize(500, 50);

            BufferedImage bimg = new BufferedImage(500, 50, BufferedImage.TYPE_3BYTE_BGR);
            cbar.paintComponent(bimg.createGraphics());

            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(view);

            if (res == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                ImageIO.write(bimg, "png", f);
            }
        }


    }

}
