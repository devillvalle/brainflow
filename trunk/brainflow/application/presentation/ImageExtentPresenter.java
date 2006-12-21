package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.TripleSliderForm2;
import com.brainflow.display.Viewport3D;
import com.brainflow.gui.AbstractPresenter;
import com.brainflow.image.space.Axis;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueHolder;

import javax.swing.*;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 18, 2006
 * Time: 1:45:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageExtentPresenter extends AbstractPresenter {

    private TripleSliderForm2 form;

    private Viewport3D viewport;
    private BeanAdapter viewportAdapter;

    private BoundedRangeAdapter xExtentAdapter;
    private BoundedRangeAdapter yExtentAdapter;
    private BoundedRangeAdapter zExtentAdapter;


    public ImageExtentPresenter(Viewport3D _viewport) {
        form = new TripleSliderForm2();
        viewport = _viewport;

        if (viewport != null)
            initBinding();

    }


    public void setViewport(Viewport3D _viewport) {
        viewport = _viewport;
        if (viewportAdapter != null) {
            viewportAdapter.setBean(viewport);
        } else {
            initBinding();
        }
    }

    public JComponent getComponent() {
        return form;
    }

    private void initBinding() {

        viewportAdapter = new BeanAdapter(viewport, true);


        xExtentAdapter = new BoundedRangeAdapter(new PercentageConverter(viewportAdapter.getValueModel(Viewport3D.X_AXIS_WIDTH_PROPERTY),
                new ValueHolder(10),
                new ValueHolder(viewport.getBounds().getImageAxis(Axis.X_AXIS).getRange().getInterval()), 100), 0, 0, 100);


        form.getSlider1().setModel(xExtentAdapter);

        yExtentAdapter = new BoundedRangeAdapter(new PercentageConverter(viewportAdapter.getValueModel(Viewport3D.Y_AXIS_WIDTH_PROPERTY),
                new ValueHolder(10),
                new ValueHolder(viewport.getBounds().getImageAxis(Axis.Y_AXIS).getRange().getInterval()), 100), 0, 0, 100);


        form.getSlider2().setModel(yExtentAdapter);

        zExtentAdapter = new BoundedRangeAdapter(new PercentageConverter(viewportAdapter.getValueModel(Viewport3D.Z_AXIS_WIDTH_PROPERTY),
                new ValueHolder(10),
                new ValueHolder(viewport.getBounds().getImageAxis(Axis.Z_AXIS).getRange().getInterval()), 100), 0, 0, 100);

        form.getSlider3().setModel(zExtentAdapter);

        Bindings.bind(form.getValueLabel1(), ConverterFactory.createStringConverter(
                viewportAdapter.getValueModel(Viewport3D.X_AXIS_WIDTH_PROPERTY),
                NumberFormat.getInstance()));

        Bindings.bind(form.getValueLabel2(), ConverterFactory.createStringConverter(
                viewportAdapter.getValueModel(Viewport3D.Y_AXIS_WIDTH_PROPERTY),
                NumberFormat.getInstance()));

        Bindings.bind(form.getValueLabel3(), ConverterFactory.createStringConverter(
                viewportAdapter.getValueModel(Viewport3D.Z_AXIS_WIDTH_PROPERTY),
                NumberFormat.getInstance()));

        // Bindings.bind(control.getLowField(), adapter.getValueModel(LinearColorMap.LOWER_ALPHA_PROPERTY));


    }


}
