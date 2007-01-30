package com.brainflow.application.presentation;

import com.brainflow.image.axis.ImageAxis;
import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;

class CoordinateToIndexConverter extends AbstractConverter {

    private ImageAxis axis;

    public CoordinateToIndexConverter(ValueModel valueModel, ImageAxis _axis) {
        super(valueModel);
        axis = _axis;

    }

    

    public Object convertFromSubject(Object object) {
        Double val = (Double) object;
        return axis.nearestSample(val);
    }


    public ImageAxis getAxis() {
        return axis;
    }

    public void setAxis(ImageAxis axis) {
        this.axis = axis;
    }

    public void setValue(Object object) {
        Integer val = (Integer) object;
        double newval = axis.valueOf(val).getX();
        subject.setValue(newval);

    }
}
