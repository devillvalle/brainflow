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
        //System.out.println("min " + dmin);
        //System.out.println("max " + dmax);
        //System.out.println("num units " + numUnits);

        double newval = (val / numUnits) * (dmax.doubleValue() - dmin.doubleValue()) + dmin.doubleValue();
        //System.out.println("PercentageConverter: new val = " + newval);
        subject.setValue(newval);

    }
}