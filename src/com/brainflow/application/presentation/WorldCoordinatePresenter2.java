package com.brainflow.application.presentation;

import com.brainflow.application.presentation.binding.DoubleToStringConverter;
import com.brainflow.application.presentation.binding.PercentageRangeConverter;
import com.brainflow.application.presentation.forms.TripleSliderForm;
import com.brainflow.application.presentation.forms.CoordinateSpinner;
import com.brainflow.core.ImageView;

import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;

import net.java.dev.properties.binding.swing.adapters.SwingBind;
import net.java.dev.properties.container.BeanContainer;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 2:02:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorldCoordinatePresenter2 extends ImageViewPresenter {

    private CoordinateSpinner form;


    public WorldCoordinatePresenter2() {
        buildGUI();

        if (getSelectedView() != null)
            bind();

    }


    private void buildGUI() {
        form = new CoordinateSpinner();

    }

    private void bind() {
        ImageView view = getSelectedView();
        IImageSpace ispace = view.getModel().getImageSpace();

        //ImageAxis xaxis = ispace.getImageAxis(Axis.X_AXIS);
        //ImageAxis yaxis = ispace.getImageAxis(Axis.Y_AXIS);
        //ImageAxis zaxis = ispace.getImageAxis(Axis.Z_AXIS);

        // bind cursorPos values to JSliders using double --> integer converter wrapper
        SwingBind.get().bind(view.cursorX, form.getXspinner());
        SwingBind.get().bind(view.cursorY, form.getYspinner());
        SwingBind.get().bind(view.cursorZ, form.getZspinner());

        String header1 = view.cursorPos.get().getAnatomy().XAXIS.getMinDirection().toString();
        String header2 = view.cursorPos.get().getAnatomy().YAXIS.getMinDirection().toString();
        String header3 = view.cursorPos.get().getAnatomy().ZAXIS.getMinDirection().toString();

        form.getXspinnerHeader().setText(header1);
        form.getYspinnerHeader().setText(header2);
        form.getZspinnerHeader().setText(header3);

        //SwingBind.get().bind(new DoubleToStringConverter(view.cursorX), form.getValueLabel1());
        //SwingBind.get().bind(new DoubleToStringConverter(view.cursorY), form.getValueLabel2());
        //SwingBind.get().bind(new DoubleToStringConverter(view.cursorZ), form.getValueLabel3());

        //form.getSliderLabel1().setText("X: " + "(" + xaxis.getAnatomicalAxis() + ")");
        //form.getSliderLabel2().setText("Y: " + "(" + yaxis.getAnatomicalAxis() + ")");
        //form.getSliderLabel3().setText("Z: " + "(" + zaxis.getAnatomicalAxis() + ")");
    }


    public JComponent getComponent() {
        return form;
    }

    public void viewSelected(ImageView view) {
        bind();
    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}