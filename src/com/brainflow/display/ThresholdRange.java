package com.brainflow.display;

import com.brainflow.image.data.MaskPredicate;
import com.brainflow.utils.ExclusiveRange;
import com.brainflow.utils.IRange;
import com.brainflow.utils.Range;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 28, 2007
 * Time: 8:36:04 PM
 * To change this template use File | Settings | File Templates.
 */


public class ThresholdRange extends LayerProperty implements MaskPredicate, IRange {


    public static final String SYMMETRICAL_PROPERTY = "symmetrical";

    public static final String INCLUSIVE_PROPERTY = "inclusive";

    public static final String MIN_PROPERTY = "min";

    public static final String MAX_PROPERTY = "max";

    public boolean inclusive = false;

    public boolean symmetrical = false;

    private IRange thresholdRange = new ExclusiveRange(0, 0);

    // todo make a bound property with min and max
    private IRange globalRange = new Range(Double.MIN_VALUE, Double.MAX_VALUE);


    public ThresholdRange(double tmin, double tmax) {
        thresholdRange = new ExclusiveRange(tmin, tmax);
        globalRange = new Range(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public ThresholdRange(double min, double max, IRange globalRange) {
        if (inclusive) {
            thresholdRange = new Range(min, max);
        } else {
            thresholdRange = new ExclusiveRange(min, max);
        }

        // todo check if valid
        this.globalRange = globalRange;


    }


    public ThresholdRange(double min, double max, IRange globalRange, boolean inclusive, boolean symmetrical) {
        if (inclusive) {
            thresholdRange = new Range(min, max);
        } else {
            thresholdRange = new ExclusiveRange(min, max);
        }

        // todo check if valid
        this.globalRange = globalRange;

        this.symmetrical = symmetrical;
    }

    public String getName() {
        return "thresholdRange";
    }

    public Object getValue() {
        return thresholdRange;
    }

    public ThresholdRange copy() {
        ThresholdRange trange = new ThresholdRange(thresholdRange.getMin(), thresholdRange.getMax(),
                globalRange, inclusive, symmetrical);

        trange.inclusive = inclusive;
        trange.symmetrical = symmetrical;
        return trange;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    public void setInclusive(boolean inclusive) {
        if (inclusive == isInclusive()) return;

        boolean old = isInclusive();

        this.inclusive = inclusive;

        if (isInclusive()) {
            thresholdRange = new Range(thresholdRange);
        } else {
            thresholdRange = new ExclusiveRange(thresholdRange);
        }


        firePropertyChange(ThresholdRange.INCLUSIVE_PROPERTY, old, isInclusive());
    }

    public void setRange(double min, double max) {
        if (isSymmetrical()) {
            max = Math.abs(max);
            min = -Math.abs(max);
        }

        setMin(min);
        setMax(max);
    }

    public boolean isSymmetrical() {
        return symmetrical;
    }

    public void setSymmetrical(boolean symmetrical) {
        boolean old = isSymmetrical();
        this.symmetrical = symmetrical;

        firePropertyChange(ThresholdRange.SYMMETRICAL_PROPERTY, old, isSymmetrical());

        setMax(Math.abs(thresholdRange.getMax()));
        setMin(-getMax());
    }


    protected double[] filterHighValue(double low, double high) {

        if (high < low) {
            low = high;
        }

        if (isSymmetrical()) {
            high = Math.abs(high);
            low = -high;
        }

        return new double[]{low, high};


    }


    protected double[] filterLowValue(double low, double high) {

        if (low > high) {
            high = low;
        }

        if (isSymmetrical()) {
            high = Math.abs(high);
            low = -high;
        }

        if (high > globalRange.getMax()) {
            high = globalRange.getMax();
        }

        if (low < globalRange.getMin()) {
            low = globalRange.getMin();
        }


        return new double[]{low, high};
    }

    public double getMin() {
        return thresholdRange.getMin();
    }

    public void setMin(double min) {
        double old = getMin();

        double[] rvals = filterLowValue(min, getMax());


        if (rvals[1] != getMax()) {
            double oldThresh = getMax();
            thresholdRange.setMax(rvals[1]);
            firePropertyChange(ThresholdRange.MAX_PROPERTY,
                    oldThresh, getMax());
        }

        thresholdRange.setMin(rvals[0]);
        firePropertyChange(ThresholdRange.MIN_PROPERTY, old, getMin());

    }

    public double getMax() {
        return thresholdRange.getMax();
    }

    public void setMax(double max) {

        double old = getMax();

        double[] rvals = filterHighValue(getMin(), max);


        if (rvals[0] != getMin()) {
            double oldThresh = getMin();
            thresholdRange.setMin(rvals[0]);
            firePropertyChange(ThresholdRange.MIN_PROPERTY,
                    oldThresh, getMin());
        }


        thresholdRange.setMax(rvals[1]);
        firePropertyChange(ThresholdRange.MAX_PROPERTY, old, getMax());


    }

    public double getInterval() {
        return thresholdRange.getInterval();
    }

    public boolean contains(double val) {
        return thresholdRange.contains(val);
    }

    public final int mask(double value) {
        if (thresholdRange.contains(value)) {
            return 1;
        } else {
            return 0;
        }
    }


    public String toString() {
        return thresholdRange.toString();
    }
}
