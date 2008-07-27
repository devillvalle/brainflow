package com.brainflow.application.presentation;

import com.brainflow.application.presentation.binding.CoordinateToIndexConverter;
import com.brainflow.application.presentation.binding.IntegerToStringConverter;
import com.brainflow.application.presentation.controls.TripleSliderForm;
import com.brainflow.core.ImageView;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import net.java.dev.properties.binding.swing.adapters.SwingBind;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 9:21:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexCoordinatePresenter extends ImageViewPresenter {

    private TripleSliderForm form;


    public IndexCoordinatePresenter() {
        form = new TripleSliderForm();
        buildGUI();
        if (getSelectedView() != null)
            bind();


    }


    private void buildGUI() {
        form = new TripleSliderForm();

    }

    public void bind() {
        ImageView view = getSelectedView();
        IImageSpace ispace = view.getModel().getImageSpace();

        ImageAxis xaxis = ispace.getImageAxis(Axis.X_AXIS);
        ImageAxis yaxis = ispace.getImageAxis(Axis.Y_AXIS);
        ImageAxis zaxis = ispace.getImageAxis(Axis.Z_AXIS);



        CoordinateToIndexConverter iconv = new CoordinateToIndexConverter(view.cursorX, xaxis);
        CoordinateToIndexConverter jconv = new CoordinateToIndexConverter(view.cursorY, yaxis);
        CoordinateToIndexConverter kconv = new CoordinateToIndexConverter(view.cursorZ, zaxis);
        // bind cursorPos values to JSliders using double --> integer converter wrapper
        SwingBind.get().bind(iconv, form.getSlider1());
        SwingBind.get().bind(jconv, form.getSlider2());
        SwingBind.get().bind(kconv, form.getSlider3());

        SwingBind.get().bind(new IntegerToStringConverter(iconv), form.getValueLabel1());
        SwingBind.get().bind(new IntegerToStringConverter(jconv), form.getValueLabel2());
        SwingBind.get().bind(new IntegerToStringConverter(kconv), form.getValueLabel3());

        form.getSliderLabel1().setText("I: " + "(" + xaxis.getAnatomicalAxis() + ")");
        form.getSliderLabel2().setText("J: " + "(" + yaxis.getAnatomicalAxis() + ")");
        form.getSliderLabel3().setText("K: " + "(" + zaxis.getAnatomicalAxis() + ")");
    }


    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void viewSelected(ImageView view) {
        bind();
    }



    public JComponent getComponent() {
        return form;
    }
}


