package com.brainflow.display;

import com.jgoodies.binding.beans.Model;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 28, 2007
 * Time: 9:29:51 PM
 */
public class SmoothingRadius extends Model {

    public static final String RADIUS_PROPERTY = "radius";


    private int radius = 0;


    public SmoothingRadius() {
    }

    public SmoothingRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        double old = getRadius();
        this.radius = radius;

        firePropertyChange(RADIUS_PROPERTY, old, getRadius());
    }
}
