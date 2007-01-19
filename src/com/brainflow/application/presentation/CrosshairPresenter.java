package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.CrosshairForm;
import com.brainflow.core.annotations.CrosshairAnnotation;
import com.brainflow.gui.AbstractPresenter;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.value.ValueModel;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 12, 2007
 * Time: 9:36:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrosshairPresenter extends AbstractPresenter {

    private CrosshairAnnotation annotation;
    private CrosshairForm form;

    public CrosshairPresenter(CrosshairAnnotation _annotation) {
        annotation = _annotation;
        form = new CrosshairForm();
        initBindings();
    }


    public CrosshairAnnotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(CrosshairAnnotation annotation) {
        this.annotation = annotation;
    }

    private void initBindings() {
        JSpinner lineSpinner = form.getLineWidthSpinner();

        ValueModel lineWidth = new PropertyAdapter(annotation, CrosshairAnnotation.LINE_WIDTH_PROPERTY, true);
        SpinnerModel spinnerModel = new SpinnerNumberModel(1.0, 1.0, 10.0, .3);
        SpinnerAdapterFactory.connect(spinnerModel, lineWidth, 1);
        lineSpinner.setModel(spinnerModel);

        Bindings.bind(form.getVisibleBox(), new PropertyAdapter(annotation, CrosshairAnnotation.VISIBLE_PROPERTY));

    }


    public JComponent getComponent() {
        return form;

    }
}
