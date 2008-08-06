package com.brainflow.core;


import net.java.dev.properties.Property;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableProperty;
import net.java.dev.properties.container.ObservableWrapper;
import com.brainflow.utils.IRange;
import com.brainflow.utils.Range;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 14, 2007
 * Time: 9:34:24 PM
 * To change this template use File | Settings | File Templates.
 */


public class ClipRange implements IRange {

    public final Property<Double> minValue = ObservableProperty.create();

    public final Property<Double> maxValue = ObservableProperty.create();

    public final Property<Double> lowClip = new ObservableProperty<Double>(0.0) {
        public void set(Double aDouble) {

            if (aDouble > highClip.get()) {

                highClip.set(aDouble);
            }

            super.set(aDouble);


        }

        


    };

    public final Property<Double> highClip = new ObservableProperty<Double>(0.0) {
        public void set(Double aDouble) {
        
            if (aDouble < lowClip.get().doubleValue()) {

                lowClip.set(get());
            }

            super.set(aDouble);
        }
    };



    public ClipRange(double min, double max, double lowClip, double highClip) {
        BeanContainer.bind(this);

        if (min > lowClip) throw new IllegalArgumentException("min cannot exceed lowClip");
        if (max < highClip) throw new IllegalArgumentException("max cannot be less than highClip");
        if (lowClip > highClip) throw new IllegalArgumentException("lowClip cannot exceed highClip");
        if (min > max) throw new IllegalArgumentException("min cannot exceed max");


        this.highClip.set(highClip);
        this.lowClip.set(lowClip);
        this.maxValue.set(max);
        this.minValue.set(min);
    }


    public double getHighClip() {
        return highClip.get();
    }

    public void setClipRange(double low, double high) {
        lowClip.set(low);
        highClip.set(high);
    }

    public void setHighClip(double highClip) {
        this.highClip.set(highClip);
    }

    public double getLowClip() {
        return lowClip.get();
    }

    public void setLowClip(double lowClip) {
       this.lowClip.set(lowClip);
    }

    public double getInterval() {
        return highClip.get() - lowClip.get();
    }

    public Range getInnerRange() {
        return new Range(lowClip.get(), highClip.get());
    }

    public double getMin() {
        throw new UnsupportedOperationException("bad method");

    }

    public double getMax() {
        throw new UnsupportedOperationException("bad method");
                
    }

    public void setMax(double max) {
        if (max > maxValue.get()) {
            throw new IllegalArgumentException("value " + max + " out of range ");
        }

        highClip.set(max);
    }

    public void setMin(double min) {
         if (min < minValue.get()) {
            throw new IllegalArgumentException("value " + min + " out of range ");
        }
        lowClip.set(min);
    }

    public void setRange(double min, double max) {
        //todo check for correctness
        setMin(min);
        setMax(max);
    }

    public final boolean contains(double val) {
        if (lowClip.get() <= val && highClip.get() >= val) {
            return true;
        }

        return false;
    }


}
