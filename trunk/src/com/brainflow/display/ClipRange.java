package com.brainflow.display;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 14, 2007
 * Time: 9:34:24 PM
 * To change this template use File | Settings | File Templates.
 */

@XStreamAlias("clip-range")
public class ClipRange extends LayerProperty {

    public static final String HIGH_CLIP_PROPERTY = "highClip";

    public static final String LOW_CLIP_PROPERTY = "lowClip";

    @XStreamAlias("low-clip")
    @XStreamAsAttribute
    private double lowClip;

    @XStreamAlias("high-clip")
    @XStreamAsAttribute
    private double highClip;


    public ClipRange(double lowClip, double highClip) {
        this.highClip = highClip;
        this.lowClip = lowClip;
    }

    public String getName() {
        return "clipRange";
    }

    public Object getValue() {
        return this;
    }

    public double getHighClip() {
        return highClip;
    }

    public void setClipRange(double low, double high) {
        double oldLow = getLowClip();
        double oldHigh = getHighClip();
        setLowClip0(low);
        setHighClip0(high);

        firePropertyChange(LOW_CLIP_PROPERTY, oldLow, getLowClip());
        firePropertyChange(HIGH_CLIP_PROPERTY, oldHigh, getHighClip());
    }

    public void setHighClip(double highClip) {
        double old = highClip;
        this.highClip = highClip;
        firePropertyChange(HIGH_CLIP_PROPERTY, old, getHighClip());
    }

    public double getLowClip() {
        return lowClip;
    }

    public void setLowClip(double lowClip) {
        double old = this.lowClip;
        this.lowClip = lowClip;
        firePropertyChange(LOW_CLIP_PROPERTY, old, getLowClip());
    }

    public void setLowClip0(double lowClip) {
        this.lowClip = lowClip;

    }
     public void setHighClip0(double highClip) {
        this.highClip = highClip;

    }

}
