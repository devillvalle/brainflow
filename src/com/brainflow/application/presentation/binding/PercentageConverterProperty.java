package com.brainflow.application.presentation.binding;

import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.RProperty;
import net.java.dev.properties.WProperty;
import net.java.dev.properties.container.ObservableWrapper;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Nov 9, 2007
 * Time: 9:56:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class PercentageConverterProperty extends ObservableWrapper.ReadWrite<Integer> {

    private double min;

    private double max;

    private int numUnits;

    public PercentageConverterProperty(BaseProperty<Double> property, double min, double max, int numUnits) {
        super(property);
        this.min = min;
        this.max = max;
        this.numUnits = numUnits;



    }

    



    private double getValue() {
        RProperty<Double> prop = (RProperty<Double>) getProperty();
        return prop.get().doubleValue();
    }

    @Override
    public void set(Integer o) {
        double val = o.intValue();
        double newval = (val / numUnits) * (max - min) + min;
        WProperty<Double> wprop = (WProperty<Double>) getProperty();
        wprop.set(new Double(newval));


    }

    @Override
    public Integer get() {
        double val = getValue();


        double percent = (val - min) / (max - min) * numUnits;

      
        return (int)Math.round(percent);
    }

    public static void main(String[] args) {

    }
}
