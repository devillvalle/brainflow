package com.brainflow.colormap;

import com.brainflow.utils.Range;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 23, 2005
 * Time: 10:59:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColorInterval extends AbstractInterval {

    private Color clr;

    public ColorInterval(double _min, double _max) {
        super(_min, _max);

    }

    public ColorInterval(double _min, double _max, Color c) {
        super(_min, _max);
        clr = c;
    }

    public Color getColor() {
        return clr;
    }

    public int getRed() {
        return clr.getRed();
    }

    public int getGreen() {
        return clr.getGreen();
    }

    public int getBlue() {
        return clr.getBlue();
    }

    public int getAlpha() {
        return clr.getAlpha();
    }

    public boolean equals(Object other) {

        if (!(other instanceof ColorInterval)) return false;

        ColorInterval o1 = (ColorInterval) other;

        int c1 = Double.compare(o1.getMinimum(), min);

        if (c1 != 0) return false;

        int c2 = Double.compare(o1.getMaximum(), max);

        if (c2 != 0) return false;

        if (o1.getColor().equals(getColor())) {
            return false;
        }

        return true;


    }


    public ColorInterval truncate(double newMaximum) {
        if (newMaximum > getMaximum()) {
            throw new IllegalArgumentException("truncated interval must be smaller than current interval" +
                " old range = " + new Range(getMinimum(), getMaximum()) +
                 " requested range" + new Range(getMinimum(), newMaximum));
        } else if (newMaximum <= getMinimum()) {
            throw new IllegalArgumentException("truncated interval must overlap current interval" +
                    " old range = " + new Range(getMinimum(), getMaximum()) +
                    " requested range" + new Range(getMinimum(), newMaximum));


        }

        return new ColorInterval(min, newMaximum, clr);
    }


    public ColorInterval clone() {
        return new ColorInterval(min, max, clr);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("range(" + this.getMinimum() + ", " + this.getMaximum() + ") RGBA: " + clr);
        return sb.toString();


    }

}
