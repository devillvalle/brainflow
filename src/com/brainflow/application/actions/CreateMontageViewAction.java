package com.brainflow.application.actions;

import org.bushe.swing.action.BasicAction;
import com.brainflow.core.ImageView;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageViewFactory;
import com.brainflow.core.ImageCanvas2;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.axis.ImageAxis;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 31, 2006
 * Time: 11:42:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateMontageViewAction extends BasicAction {


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
            IImageDisplayModel displayModel = view.getModel();
            //ImageView sview = ImageViewFactory.createOrthogonalView(displayModel);

            InputPanel ip = new InputPanel(view);
            ImageCanvas2 canvas = (ImageCanvas2) getContextValue(ActionContext.SELECTED_CANVAS);

            JOptionPane.showMessageDialog(canvas, ip);
            ImageView sview = ImageViewFactory.createMontageView(displayModel, ip.getRows(), ip.getColumns(), ip.getSliceGap());

            if (canvas != null) {
                canvas.addImageView(sview);
            }
        }

    }

    class InputPanel extends JPanel {

        FormLayout layout;

        JSpinner rowSpinner;
        JSpinner colSpinner;
        JSpinner gapSpinner;

        public InputPanel(ImageView view) {

            layout = new FormLayout("6dlu, l:p, 4dlu, 1dlu, l:45dlu, 6dlu", "8dlu, p, 6dlu, p, 6dlu, p, 8dlu");
            layout.addGroupedColumn(2);
            layout.addGroupedColumn(4);
            CellConstraints cc = new CellConstraints();
            setLayout(layout);

            rowSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 6, 1));
            colSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 6, 1));


            Anatomy3D anatomy = view.getSelectedPlot().getDisplayAnatomy();
            ImageAxis iaxis = view.getModel().getImageSpace().getImageAxis(anatomy.ZAXIS, true);
            gapSpinner = new JSpinner(new SpinnerNumberModel(iaxis.getSpacing(), iaxis.getSpacing(), 20, 1));

            add(rowSpinner, cc.xyw(4, 2, 2));
            add(colSpinner, cc.xyw(4, 4, 2));
            add(gapSpinner, cc.xyw(4, 6, 2));

            add(new JLabel("Rows:"), cc.xy(2,2));
            add(new JLabel("Columns:"), cc.xy(2,4));
            add(new JLabel("Slice Gap:"), cc.xy(2,6));


        }

        public int getRows() {
            return ((Number)rowSpinner.getValue()).intValue();
        }

        public int getColumns() {
            return ((Number)colSpinner.getValue()).intValue();
        }

        public double getSliceGap() {
            return ((Number)gapSpinner.getValue()).doubleValue();
        }
    }

    
}
