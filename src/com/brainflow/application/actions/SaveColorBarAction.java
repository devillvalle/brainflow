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
            int idx = view.getModel().getSelectedIndex();
            IColorMap cmap = view.getModel().getLayerParameters(idx).getColorMap().getProperty();
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
