package com.brainflow.core;

import net.java.dev.properties.container.BeanContainer;
import com.brainflow.utils.NumberUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Dec 1, 2008
 * Time: 9:56:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbsClipRange extends ClipRange {

    private double absval;

    public AbsClipRange(double min, double max, double absClip) {
        super(min, max, Math.max(min, -Math.abs(absClip)), Math.min(Math.abs(absClip), max));
        absval = Math.abs(super.getHighClip());
        System.out.println("absval is " + absval);
        //System.out.println("lowclip = " + getLowClip());

        //System.out.println("highclip = " + getHighClip());
        //BeanContainer.bind(this);
          //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public boolean contains(double val) {
        if (absval >= Math.abs(val)) {
            return true;
        }

        return false;

    }

    //@Override
    public double getHighClip() {
        return Math.min(absval, getMax());
    }

    public double getLowClip() {
        return Math.max(-absval, getMin());
    }

    @Override
    public IClipRange newClipRange(double min, double max, double lowclip, double highclip) {
        double absclip;

        double d1 = Math.abs(Math.abs(highclip) - Math.abs(getHighClip()));
        double d2 = Math.abs(Math.abs(lowclip) -  Math.abs(getLowClip()));

        System.out.println("lowclip " + lowclip);
        System.out.println("highclip " + highclip);
        System.out.println("d1 " + d1);
        System.out.println("d2 " + d2);

        if (NumberUtils.equals(d1, d2, .0001)) {
            System.out.println("d1 roughly equal to d2");
            absclip = Math.abs(highclip);
        }
        else if (d1 > d2) {
            System.out.println("d1 > d2");
            absclip = Math.abs(highclip);
        } else {
            System.out.println("d2 > d1");
            absclip = Math.abs(lowclip);
        }

        System.out.println("new abs clip : " + absclip);
        return new AbsClipRange(min, max, absclip);
    }
}
