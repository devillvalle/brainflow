package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.TripleSliderForm;
import com.brainflow.display.Crosshair;
import com.brainflow.display.Viewport3D;
import com.brainflow.display.ICrosshair;
import com.brainflow.gui.AbstractPresenter;
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


        XSliderAdapter = new BoundedRangeAdapter(new PercentageConverter(crosshairAdapter.getValueModel(Crosshair.X_VALUE_PROPERTY),
                viewportAdapter.getValueModel(Viewport3D.X_AXIS_MIN_PROPERTY),
                viewportAdapter.getValueModel(Viewport3D.X_AXIS_MAX_PROPERTY), 100), 0, 0, 100);


        form.getSlider1().setModel(XSliderAdapter);
        form.getSliderLabel1().setText("X: " + "(" + crosshair.getViewport().getXAxis() + ")");
        form.getSliderLabel2().setText("Y: " + "(" + crosshair.getViewport().getYAxis() + ")");
        form.getSliderLabel3().setText("Z: " + "(" + crosshair.getViewport().getZAxis() + ")");

        YSliderAdapter = new BoundedRangeAdapter(new PercentageConverter(crosshairAdapter.getValueModel(Crosshair.Y_VALUE_PROPERTY),
                viewportAdapter.getValueModel(Viewport3D.Y_AXIS_MIN_PROPERTY),
                viewportAdapter.getValueModel(Viewport3D.Y_AXIS_MAX_PROPERTY), 100), 0, 0, 100);

        form.getSlider2().setModel(YSliderAdapter);


        ZSliderAdapter = new BoundedRangeAdapter(new PercentageConverter(crosshairAdapter.getValueModel(Crosshair.Z_VALUE_PROPERTY),
                viewportAdapter.getValueModel(Viewport3D.Z_AXIS_MIN_PROPERTY),
                viewportAdapter.getValueModel(Viewport3D.Z_AXIS_MAX_PROPERTY), 100), 0, 0, 100);

        form.getSlider3().setModel(ZSliderAdapter);

        Bindings.bind(form.getValueLabel1(), crosshairAdapter.getValueModel(Crosshair.X_VALUE_PROPERTY));


        Bindings.bind(form.getValueLabel2(), crosshairAdapter.getValueModel(Crosshair.Y_VALUE_PROPERTY));


        Bindings.bind(form.getValueLabel3(), crosshairAdapter.getValueModel(Crosshair.Z_VALUE_PROPERTY));

        // Bindings.bind(control.getLowField(), adapter.getValueModel(LinearColorMap.LOWER_ALPHA_PROPERTY));

    }

    public JComponent getComponent() {
        return form;
    }
}
