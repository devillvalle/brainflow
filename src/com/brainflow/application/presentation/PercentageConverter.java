package com.brainflow.application.presentation;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;


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


    public ValueModel getMax() {
        return max;
    }

    public void setMax(ValueModel max) {
        this.max = max;
    }

    public ValueModel getMin() {
        return min;
    }

    public void setMin(ValueModel min) {
        this.min = min;
    }

    public Object convertFromSubject(Object object) {
        Double val = (Double) object;

        Number dmin = (Number) min.getValue();
        Number dmax = (Number) max.getValue();
        double percent = (val - dmin.doubleValue()) / (dmax.doubleValue() - dmin.doubleValue()) * numUnits;


        return new Integer((int) Math.round(percent));


    }

    public void setValue(Object object) {
        Number dmin = (Number) min.getValue();
        Number dmax = (Number) max.getValue();
        Integer val = (Integer) object;

        double newval = (val / numUnits) * (dmax.doubleValue() - dmin.doubleValue()) + dmin.doubleValue();
       

        subject.setValue(newval);

    }
}