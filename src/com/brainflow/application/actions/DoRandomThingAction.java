package com.brainflow.application.actions;

import com.brainflow.colormap.ColorTable;
import com.brainflow.core.CoordinateLayer;
import com.brainflow.core.ImageLayerProperties;
import com.brainflow.core.ImageView;
import com.brainflow.image.data.CoordinateSet3D;
import com.brainflow.utils.Range;
import org.bushe.swing.action.BasicAction;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 21, 2007
 * Time: 7:15:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class DoRandomThingAction extends BasicAction {

    double[][] points = new double[][]{{0, 0, 0}, {-12, 32, 34}, {33, 0, -12}, {25, 25, 25}};
    double[] values = new double[]{1, 2, 3, 4};
    double[] radii = new double[]{2, 6, 12, 20};

    protected void contextChanged() {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);

        if (view != null) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
    }

    protected void execute(ActionEvent actionEvent) throws Exception {
        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);

        if (view != null) {
            CoordinateSet3D cset = new CoordinateSet3D(view.getModel().getImageSpace(), points, values, radii);
            Range range = new Range(cset.getMinValue(), cset.getMaxValue());

            CoordinateLayer layer = new CoordinateLayer(new ImageLayerProperties(ColorTable.SPECTRUM, range), cset);
            // view.getModel().addLayer(layer);
        }

    }


}
