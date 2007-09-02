package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.TripleSliderForm;
import com.brainflow.display.Crosshair;
import com.brainflow.display.ICrosshair;
import com.brainflow.gui.AbstractPresenter;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.space.Axis;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 2:02:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorldCoordinatePresenter extends AbstractPresenter {

    private TripleSliderForm form;

    private ICrosshair crosshair;

    private BeanAdapter crosshairAdapter;

    private BeanAdapter viewportAdapter;

    private BoundedRangeAdapter XSliderAdapter;

    private BoundedRangeAdapter YSliderAdapter;

    private BoundedRangeAdapter ZSliderAdapter;

    public WorldCoordinatePresenter(ICrosshair _crosshair) {
        form = new TripleSliderForm();
        crosshair = _crosshair;

        if (crosshair != null)
            initBinding();

    }

    public void setCrosshair(ICrosshair _crosshair) {
        crosshair = _crosshair;
        if (crosshairAdapter != null) {
            crosshairAdapter.setBean(crosshair);
            viewportAdapter.setBean(crosshair.getViewport());
        } else {
            initBinding();
        }
    }

    private void initBinding() {
        crosshairAdapter = new BeanAdapter(crosshair, true);
        viewportAdapter = new BeanAdapter(crosshair.getViewport(), true);


        ImageAxis xaxis = crosshair.getViewport().getBounds().getImageAxis(Axis.X_AXIS);
        ImageAxis yaxis = crosshair.getViewport().getBounds().getImageAxis(Axis.Y_AXIS);
        ImageAxis zaxis = crosshair.getViewport().getBounds().getImageAxis(Axis.Z_AXIS);


        XSliderAdapter = BindingUtils.createPercentageBasedRangeModel(crosshairAdapter.getValueModel(Crosshair.X_VALUE_PROPERTY),
                xaxis.getRange().getMinimum(), xaxis.getRange().getMaximum(), 100);


        YSliderAdapter = BindingUtils.createPercentageBasedRangeModel(crosshairAdapter.getValueModel(Crosshair.Y_VALUE_PROPERTY),
                yaxis.getRange().getMinimum(), yaxis.getRange().getMaximum(), 100);


        ZSliderAdapter = BindingUtils.createPercentageBasedRangeModel(crosshairAdapter.getValueModel(Crosshair.Z_VALUE_PROPERTY),
                zaxis.getRange().getMinimum(), zaxis.getRange().getMaximum(), 100);


        form.getSlider1().setModel(XSliderAdapter);
        form.getSlider2().setModel(YSliderAdapter);
        form.getSlider3().setModel(ZSliderAdapter);

        form.getSliderLabel1().setText("X: " + "(" + crosshair.getViewport().getXAxis() + ")");
        form.getSliderLabel2().setText("Y: " + "(" + crosshair.getViewport().getYAxis() + ")");
        form.getSliderLabel3().setText("Z: " + "(" + crosshair.getViewport().getZAxis() + ")");


        Bindings.bind(form.getValueLabel1(), crosshairAdapter.getValueModel(Crosshair.X_VALUE_PROPERTY));
        Bindings.bind(form.getValueLabel2(), crosshairAdapter.getValueModel(Crosshair.Y_VALUE_PROPERTY));
        Bindings.bind(form.getValueLabel3(), crosshairAdapter.getValueModel(Crosshair.Z_VALUE_PROPERTY));


    }

    public JComponent getComponent() {
        return form;
    }
}
