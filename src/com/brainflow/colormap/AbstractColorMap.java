package com.brainflow.colormap;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 16, 2006
 * Time: 3:35:51 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractColorMap implements IColorMap {


    public static final String COLORS_CHANGED_PROPERTY = "colorsChanged";

    public static final String HIGH_CLIP_PROPERTY = "highClip";

    public static final String LOW_CLIP_PROPERTY = "lowClip";

    public static final String MAP_SIZE_PROPERTY = "mapSize";

    public static final String UPPER_ALPHA_PROPERTY = "upperAlphaThreshold";

    public static final String LOWER_ALPHA_PROPERTY = "lowerAlphaThreshold";

    public static final String ALPHA_MULTIPLIER_PROPERTY = "alphaMultiplier";

    public static final String MAXIMUM_VALUE_PROPERTY = "maximumValue";

    public static final String MINIMUM_VALUE_PROPERTY = "minimumValue";


    protected double minimumValue;

    protected double maximumValue;

    protected double lowClip;

    protected double highClip;

    protected double lowerAlphaThreshold;

    protected double upperAlphaThreshold;

    protected double alphaMultiplier = 1f;


    protected PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    public abstract int getMapSize();

    public abstract ColorInterval getInterval(int index);

    public abstract Color getColor(double value);

    public abstract void setMapSize(int size);


    public double getMaximumValue() {
        return maximumValue;
    }

    public double getMinimumValue() {
        return minimumValue;
    }

    public double getUpperAlphaThreshold() {
        return upperAlphaThreshold;
    }

    public abstract void setUpperAlphaThreshold(double _upperAlphaThreshold);

    public double getLowerAlphaThreshold() {
        return lowerAlphaThreshold;
    }

    public abstract void setLowerAlphaThreshold(double _lowerAlphaThreshold);


    protected double[] filterHighValue(double low, double high) {
        if (high > getMaximumValue()) {
            high = getMaximumValue();
        } else if (high < getMinimumValue()) {
            high = getMinimumValue();
        }

        if (high < low) {
            low = high;
        }

        return new double[]{low, high};


    }

    protected double[] filterLowValue(double low, double high) {
        if (low < getMinimumValue()) {
            low = getMinimumValue();
        } else if (low > getMaximumValue()) {
            low = getMaximumValue();
        }

        if (low > high) {
            high = low;
        }

        return new double[]{low, high};
    }

    public abstract void setAlphaMultiplier(Double amult);

    public Double getAlphaMultiplier() {
        return alphaMultiplier;
    }

    public double getHighClip() {
        return highClip;
    }

    public abstract void setHighClip(double _highClip);

    public double getLowClip() {
        return lowClip;
    }

    public abstract void setLowClip(double _lowClip);

    public abstract ListIterator<ColorInterval> iterator();


    public abstract AbstractColorBar createColorBar();

    public boolean isScaleable() {
        return false;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
