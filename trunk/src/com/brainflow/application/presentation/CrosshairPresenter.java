package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.CrosshairForm;
import com.brainflow.core.annotations.CrosshairAnnotation;
import com.brainflow.gui.AbstractPresenter;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.jidesoft.combobox.ColorComboBox;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.*;

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

        // bind line width property to JSpinner
        JSpinner lineWidthSpinner = form.getLineWidthSpinner();

        ValueModel lineWidth = new PropertyAdapter(annotation, CrosshairAnnotation.LINE_WIDTH_PROPERTY, true);
        SpinnerModel spinnerModel = new SpinnerNumberModel(annotation.getLineWidth(), 1.0, 10.0, .3);
        SpinnerAdapterFactory.connect(spinnerModel, lineWidth, 1);
        lineWidthSpinner.setModel(spinnerModel);

        // bind visible property to JCheckBox
        Bindings.bind(form.getVisibleBox(), new PropertyAdapter(annotation, CrosshairAnnotation.VISIBLE_PROPERTY));

        // bind linePaint to ColorComboBox
        form.getColorChooser().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName() == ColorComboBox.PROPERTY_SELECTED_ITEM) {
                    Color clr = (Color)evt.getNewValue();
                    annotation.setLinePaint(clr);
                }
            }
        });

        // todo cast here assumes Color class.
        form.getColorChooser().setSelectedColor((Color)annotation.getLinePaint());

        // bind gap to JSpinner

        JSpinner gapSpinner = form.getGapSpinner();
        ValueModel gapModel = new PropertyAdapter(annotation, CrosshairAnnotation.GAP_PROPERTY, true);
        spinnerModel = new SpinnerNumberModel(annotation.getGap(), 0, 20, 1);
        SpinnerAdapterFactory.connect(spinnerModel, gapModel, 1);
        gapSpinner.setModel(spinnerModel);

        //form.getCancelButton().addActionListener();


    }


    public JComponent getComponent() {
        return form;

    }
}
