package com.brainflow.application.presentation;

import com.brainflow.application.presentation.binding.WorldToAxisConverter;
import com.brainflow.application.presentation.controls.CoordinateSpinner;
import com.brainflow.core.ImageView;

import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;

import net.java.dev.properties.binding.swing.adapters.SwingBind;

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

        form.getXspinner().setModel(new SpinnerNumberModel(0, -1000, 1000, 1.0));
        form.getYspinner().setModel(new SpinnerNumberModel(0, -1000, 1000, 1.0));
        form.getZspinner().setModel(new SpinnerNumberModel(0, -1000, 1000, 1.0));

        
        //ImageAxis xaxis = ispace.getImageAxis(Axis.X_AXIS);
        //ImageAxis yaxis = ispace.getImageAxis(Axis.Y_AXIS);
        //ImageAxis zaxis = ispace.getImageAxis(Axis.Z_AXIS);

        // bind cursorPos values to JSliders using double --> integer converter wrapper
        SwingBind.get().bind(new WorldToAxisConverter(view.worldCursorPos, Axis.X_AXIS), form.getXspinner());
        SwingBind.get().bind(new WorldToAxisConverter(view.worldCursorPos, Axis.Y_AXIS), form.getYspinner());
        SwingBind.get().bind(new WorldToAxisConverter(view.worldCursorPos, Axis.Z_AXIS), form.getZspinner());


        String header1 = view.worldCursorPos.get().getAnatomy().XAXIS.getMinDirection().toString();
        String header2 = view.worldCursorPos.get().getAnatomy().YAXIS.getMinDirection().toString();
        String header3 = view.worldCursorPos.get().getAnatomy().ZAXIS.getMinDirection().toString();

        form.getXspinnerHeader().setText(header1);
        form.getYspinnerHeader().setText(header2);
        form.getZspinnerHeader().setText(header3);

       
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