/*
 * ColorRangePanel.java
 *
 * Created on July 11, 2006, 2:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.RangeSliderControl;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.LinearColorBar;
import com.brainflow.colormap.LinearColorMapDeprecated;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;

import javax.swing.*;
import java.awt.*;

/**
 * @author buchs
 */
public class ColorRangePanel extends JPanel {

    private LinearColorMapDeprecated colorMap;
    private BeanAdapter adapter;
    private RangeSliderControl control;

    private BoundedRangeAdapter lowSliderAdapter;
    private BoundedRangeAdapter highSliderAdapter;

    /**
     * Creates a new instance of ColorRangePanel
     */
    public ColorRangePanel(LinearColorMapDeprecated cmap) {


        setLayout(new BorderLayout());

        colorMap = cmap;

        control = new RangeSliderControl();
        add(control, BorderLayout.CENTER);

        initBinding();


    }

    public ColorRangePanel() {


        setLayout(new BorderLayout());

        colorMap = new LinearColorMapDeprecated(0, 255, ColorTable.GRAYSCALE);

        control = new RangeSliderControl();
        add(control, BorderLayout.CENTER);

        initBinding();


    }


    public void setColorMap(LinearColorMapDeprecated cmap) {
        colorMap = cmap;
        adapter.setBean(colorMap);
    }


    private void initBinding() {
        adapter = new BeanAdapter(colorMap, true);


        lowSliderAdapter = new BoundedRangeAdapter(new PercentageConverter(adapter.getValueModel(LinearColorMapDeprecated.LOW_CLIP_PROPERTY),
                adapter.getValueModel(LinearColorMapDeprecated.MINIMUM_VALUE_PROPERTY),
                adapter.getValueModel(LinearColorMapDeprecated.MAXIMUM_VALUE_PROPERTY), 100), 0, 0, 100);

        highSliderAdapter = new BoundedRangeAdapter(new PercentageConverter(adapter.getValueModel(LinearColorMapDeprecated.HIGH_CLIP_PROPERTY),
                adapter.getValueModel(LinearColorMapDeprecated.MINIMUM_VALUE_PROPERTY),
                adapter.getValueModel(LinearColorMapDeprecated.MAXIMUM_VALUE_PROPERTY), 100), 0, 0, 100);

        control.getLowSlider().setModel(lowSliderAdapter);
        control.getHighSlider().setModel(highSliderAdapter);

        Bindings.bind(control.getHighField(), adapter.getValueModel(LinearColorMapDeprecated.HIGH_CLIP_PROPERTY));
        Bindings.bind(control.getLowField(), adapter.getValueModel(LinearColorMapDeprecated.LOW_CLIP_PROPERTY));

    }


    public static void main(String[] args) {
        LinearColorMapDeprecated cmap = new LinearColorMapDeprecated(0, 300, ColorTable.SPECTRUM);
        ColorRangePanel panel = new ColorRangePanel(cmap);
        LinearColorBar cbar = new LinearColorBar(cmap, SwingConstants.HORIZONTAL);
        cbar.setOrientation(SwingUtilities.HORIZONTAL);
        JPanel tmp = new JPanel();
        BoxLayout layout = new BoxLayout(tmp, BoxLayout.Y_AXIS);
        tmp.setLayout(layout);
        tmp.add(panel);
        tmp.add(cbar);


        JFrame jf = new JFrame();
        jf.add(tmp);
        jf.pack();
        jf.setVisible(true);


    }


    class PercentageConverter extends AbstractConverter {

        ValueModel min;
        ValueModel max;
        double numUnits = 100f;


        public PercentageConverter(ValueModel valueModel, ValueModel _min, ValueModel _max, int _numUnits) {
            super(valueModel);
            min = _min;
            max = _max;
            numUnits = _numUnits;

        }

        public Object convertFromSubject(Object object) {
            Double val = (Double) object;
            Number dmin = (Number) min.getValue();
            Number dmax = (Number) max.getValue();
            double percent = val / (dmax.doubleValue() - dmin.doubleValue()) * numUnits;

            return new Integer((int) Math.round(percent));


        }

        public void setValue(Object object) {
            Number dmin = (Number) min.getValue();
            Number dmax = (Number) max.getValue();
            Integer val = (Integer) object;
            double newval = (val / numUnits) * (dmax.doubleValue() - dmin.doubleValue());
            subject.setValue(newval);

        }
    }


}
