package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.TripleSliderForm;
import com.brainflow.display.Viewport3D;
import com.brainflow.gui.AbstractPresenter;
import com.brainflow.image.space.Axis;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueHolder;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 18, 2006
 * Time: 1:45:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageOriginPresenter extends AbstractPresenter {

    private TripleSliderForm form;

    private Viewport3D viewport;
    private BeanAdapter viewportAdapter;

    private BoundedRangeAdapter xOriginAdapter;
    private BoundedRangeAdapter yOriginAdapter;
    private BoundedRangeAdapter zOriginAdapter;


    public ImageOriginPresenter(Viewport3D _viewport) {
        form = new TripleSliderForm();
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


        xOriginAdapter = new BoundedRangeAdapter(new PercentageConverter(viewportAdapter.getValueModel(Viewport3D.X_AXIS_MIN_PROPERTY),
                new ValueHolder(viewport.getBounds().getImageAxis(Axis.X_AXIS).getRange().getMinimum()),
                new ValueHolder(viewport.getBounds().getImageAxis(Axis.X_AXIS).getRange().getMaximum()), 100), 0, 0, 100);


        form.getSlider1().setModel(xOriginAdapter);

        yOriginAdapter = new BoundedRangeAdapter(new PercentageConverter(viewportAdapter.getValueModel(Viewport3D.Y_AXIS_MIN_PROPERTY),
                new ValueHolder(viewport.getBounds().getImageAxis(Axis.Y_AXIS).getRange().getMinimum()),
                new ValueHolder(viewport.getBounds().getImageAxis(Axis.Y_AXIS).getRange().getMaximum()), 100), 0, 0, 100);


        form.getSlider2().setModel(yOriginAdapter);
        zOriginAdapter = new BoundedRangeAdapter(new PercentageConverter(viewportAdapter.getValueModel(Viewport3D.Z_AXIS_MIN_PROPERTY),
                new ValueHolder(viewport.getBounds().getImageAxis(Axis.Z_AXIS).getRange().getMinimum()),
                new ValueHolder(viewport.getBounds().getImageAxis(Axis.Z_AXIS).getRange().getMaximum()), 100), 0, 0, 100);

        form.getSlider3().setModel(zOriginAdapter);

        Bindings.bind(form.getValueLabel1(), viewportAdapter.getValueModel(Viewport3D.X_AXIS_MIN_PROPERTY));
        Bindings.bind(form.getValueLabel2(), viewportAdapter.getValueModel(Viewport3D.Y_AXIS_MIN_PROPERTY));
        Bindings.bind(form.getValueLabel3(), viewportAdapter.getValueModel(Viewport3D.Z_AXIS_MIN_PROPERTY));

        // Bindings.bind(control.getLowField(), adapter.getValueModel(LinearColorMapDeprecated.LOWER_ALPHA_PROPERTY));


    }


}
