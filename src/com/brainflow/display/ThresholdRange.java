package com.brainflow.display;

import com.jgoodies.binding.beans.Model;
import com.brainflow.image.data.MaskPredicate;
import com.brainflow.utils.IRange;
import com.brainflow.utils.Range;

import java.beans.PropertyChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 28, 2007
 * Time: 8:36:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThresholdRange extends Model implements MaskPredicate, IRange {


    //public static final String SYMMETRICAL_PROPERTY = "symmetrical";
    public static final String INCLUSIVE_PROPERTY = "inclusive";

    public static final String MIN_PROPERTY = "min";

    public static final String MAX_PROPERTY = "max";


    public boolean inclusive = true;

    public boolean symmetrical;

    IRange range = new Range(0,0);

    public ThresholdRange(double min, double max) {
        range = new Range(min,max);

    }

    public boolean isInclusive() {
        return inclusive;
    }

    public void setInclusive(boolean inclusive) {
        boolean old = isInclusive();
        this.inclusive = inclusive;
        firePropertyChange(ThresholdRange.INCLUSIVE_PROPERTY, old, isInclusive());
    }

    /*public boolean isSymmetrical() {
       return symmetrical;
   }

   public void setSymmetrical(boolean symmetrical) {
       boolean old = isSymmetrical();
       this.symmetrical = symmetrical;
       firePropertyChange(ThresholdRange.SYMMETRICAL_PROPERTY, old, isSymmetrical());
   } */


    protected double[] filterHighValue(double low, double high) {

        if (high < low) {
            low = high;
        }

        return new double[]{low, high};


    }

    protected double[] filterLowValue(double low, double high) {

        if (low > high) {
            high = low;
        }

        return new double[]{low, high};
    }

    public double getMin() {
        return range.getMin();
    }

    public void setMin(double min) {
        double old = getMin();

        double[] rvals = filterLowValue(min, getMax());
        range.setMin(rvals[0]);
        firePropertyChange(ThresholdRange.MIN_PROPERTY, old, getMin());

        if (rvals[1] != getMax()) {
            double oldThresh = getMax();
            range.setMax(rvals[1]);
            firePropertyChange(ThresholdRange.MAX_PROPERTY,
                    oldThresh, getMax());
        }


    }

    public double getMax() {
        return range.getMax();
    }

    public void setMax(double max) {

        double old = getMax();

        double[] rvals= filterHighValue(getMin(), max);
        range.setMax(rvals[1]);
        firePropertyChange(ThresholdRange.MAX_PROPERTY, old, getMax());


        if (rvals[0] != getMin()) {
            double oldThresh = getMin();
            range.setMin(rvals[0]);
            firePropertyChange(ThresholdRange.MIN_PROPERTY,
                    oldThresh, getMin());
        }


    }

    public double getInterval() {
        return getMax() - getMin();
    }

    public boolean contains(double val) {
        if (val >= getMin() && val <= getMax())
            return true;
        return false;
    }

    public int mask(double value) {
        if (value <= getMin() || value >= getMax()) return 1;
        return 0;
    }


    public String toString() {
        return range.toString();
    }
}
