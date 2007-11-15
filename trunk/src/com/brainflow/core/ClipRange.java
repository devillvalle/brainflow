package com.brainflow.core;


import net.java.dev.properties.Property;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableProperty;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 14, 2007
 * Time: 9:34:24 PM
 * To change this template use File | Settings | File Templates.
 */


public class ClipRange {

    public final Property<Double> minValue = ObservableProperty.create();

    public final Property<Double> maxValue = ObservableProperty.create();

    public final Property<Double> lowClip = ObservableProperty.create();

    public final Property<Double> highClip = ObservableProperty.create();


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



}
