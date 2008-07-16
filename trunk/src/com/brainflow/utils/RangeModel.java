package com.brainflow.utils;

import com.jgoodies.binding.beans.Model;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 12, 2006
 * Time: 6:48:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class RangeModel extends Model implements IRange {

    public static final String RANGE_MIN_PROPERTY = "min";
    
    public static final String RANGE_MAX_PROPERTY = "max";

    private IRange range;

    public RangeModel(IRange _range) {
        range = _range;
    }

    public double getInterval() {
        return range.getInterval();
    }

    public boolean contains(double val) {
        return range.contains(val);
    }

    public void setRange(double min, double max) {
        range.setRange(min, max);
    }

    public void setMin(double min) {
        double old = range.getMin();
        range.setMin(min);
        this.firePropertyChange(RANGE_MIN_PROPERTY, old, range.getMin());
    }

    public void setMax(double max) {
        double old = range.getMax();
        range.setMax(max);
        this.firePropertyChange(RANGE_MAX_PROPERTY, old, range.getMax());
    }

    public double getMax() {
        return range.getMax();
    }

    public double getMin() {
        return range.getMin();
    }

    public String toString() {
        return range.toString();
    }
}
