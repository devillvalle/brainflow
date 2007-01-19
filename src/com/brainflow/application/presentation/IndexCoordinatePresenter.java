package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.TripleSliderForm;
import com.brainflow.display.Crosshair;
import com.brainflow.gui.AbstractPresenter;
import com.brainflow.image.space.Axis;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 9:21:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexCoordinatePresenter extends AbstractPresenter {

    private TripleSliderForm form;
    private Crosshair crosshair;

    private BeanAdapter crosshairAdapter;
    private BeanAdapter viewportAdapter;

    private BoundedRangeAdapter ISliderAdapter;
    private BoundedRangeAdapter JSliderAdapter;
    private BoundedRangeAdapter KSliderAdapter;

    public IndexCoordinatePresenter(Crosshair _crosshair) {
        form = new TripleSliderForm();
        crosshair = _crosshair;

        if (crosshair != null)
            initBinding();

    }

    public void setCrosshair(Crosshair _crosshair) {
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


        ValueModel imodel = new CoordinateToIndexConverter(crosshairAdapter.getValueModel(Crosshair.X_VALUE_PROPERTY),
                crosshair.getViewport().getBounds().getImageAxis(Axis.X_AXIS));

        ISliderAdapter = new BoundedRangeAdapter(imodel, 0, 0,
                crosshair.getViewport().getBounds().getImageAxis(Axis.X_AXIS).getNumSamples() - 1);

        form.getSlider1().setModel(ISliderAdapter);
        form.getSliderLabel1().setText("I: ");


        ValueModel jmodel = new CoordinateToIndexConverter(crosshairAdapter.getValueModel(Crosshair.Y_VALUE_PROPERTY),
                crosshair.getViewport().getBounds().getImageAxis(Axis.Y_AXIS));

        JSliderAdapter = new BoundedRangeAdapter(jmodel, 0, 0,
                crosshair.getViewport().getBounds().getImageAxis(Axis.Y_AXIS).getNumSamples() - 1);


        form.getSlider2().setModel(JSliderAdapter);
        form.getSliderLabel2().setText("J: ");

        ValueModel kmodel = new CoordinateToIndexConverter(crosshairAdapter.getValueModel(Crosshair.Z_VALUE_PROPERTY),
                crosshair.getViewport().getBounds().getImageAxis(Axis.Z_AXIS));

        KSliderAdapter = new BoundedRangeAdapter(kmodel, 0, 0,
                crosshair.getViewport().getBounds().getImageAxis(Axis.Z_AXIS).getNumSamples() - 1);


        form.getSlider3().setModel(KSliderAdapter);
        form.getSliderLabel3().setText("K: ");

        Bindings.bind(form.getValueLabel1(), imodel);
        Bindings.bind(form.getValueLabel2(), jmodel);
        Bindings.bind(form.getValueLabel3(), kmodel);


    }

    public JComponent getComponent() {
        return form;
    }
}


