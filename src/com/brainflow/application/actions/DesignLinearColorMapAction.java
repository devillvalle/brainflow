package com.brainflow.application.actions;

import com.brainflow.application.presentation.ColorBandChartPresenter;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMapDeprecated;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageView;
import com.jidesoft.dialog.JideOptionPane;
import org.bushe.swing.action.BasicAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 4, 2006
 * Time: 2:02:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class DesignLinearColorMapAction extends BasicAction {


    protected void execute(ActionEvent actionEvent) throws Exception {
        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);

        if (view != null) {
            IImageDisplayModel displayModel = view.getModel();
            int idx = view.getSelectedLayerIndex();
            IColorMap colorMap = displayModel.getLayerParameters(idx).getColorMap().getProperty();

            Container parent = view.getParent();

            if (colorMap instanceof LinearColorMapDeprecated) {
                JDialog dialog = new JDialog(JOptionPane.getFrameForComponent(parent));
                ColorBandChartPresenter presenter = new ColorBandChartPresenter(displayModel.getLayerParameters(idx).getColorMap());
                dialog.add(presenter.getComponent());
                dialog.pack();


                dialog.setVisible(true);
            } else {
                JideOptionPane.showMessageDialog(parent, "Error, not a linear color map");
            }

        }

    }

}
