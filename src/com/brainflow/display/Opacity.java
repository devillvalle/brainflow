package com.brainflow.display;

import com.jgoodies.binding.beans.Model;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2007
 * Time: 3:55:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Opacity extends Model {

    public static final String OPACITY_PROPERTY = "opacity";


    private double opacity = 1f;


    public Opacity() {
    }

    public Opacity(float opacity) {
        this.opacity = opacity;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        double old = getOpacity();
        this.opacity = opacity;

       firePropertyChange(OPACITY_PROPERTY, old, getOpacity());
    }
}
