package com.brainflow.utils;

import com.jgoodies.binding.beans.Model;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 12, 2006
 * Time: 6:48:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class RangeModel extends Model {

    public static final String RANGE_MIN_PROPERTY = "minRange";
    public static final String RANGE_MAX_PROPERTY = "maxRange";

    private Range range;

    public RangeModel(Range _range) {
        range = _range;
    }


    public void setMinRange(double min) {
        double old = range.getMin();
        range.setMin(min);
        this.firePropertyChange(RANGE_MIN_PROPERTY, old, range.getMin());
    }

    public void setMaxRange(double max) {
        double old = range.getMax();
        range.setMax(max);
        this.firePropertyChange(RANGE_MAX_PROPERTY, old, range.getMax());
    }

    public double getMaxRange() {
        return range.getMax();
    }

    public double getMinRange() {
        return range.getMin();
    }

    public String toString() {
        return range.toString();
    }
}
