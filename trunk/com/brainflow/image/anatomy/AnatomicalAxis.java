package com.brainflow.image.anatomy;

import com.brainflow.image.IndexConverter1D;
import com.brainflow.image.axis.ImageAxis;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Jun 30, 2003
 * Time: 12:35:21 PM
 * To change this template use Options | File Templates.
 */
public class AnatomicalAxis implements Anatomy {

    private static AnatomicalAxis[] instances = new AnatomicalAxis[6];

    public static final AnatomicalAxis ANTERIOR_POSTERIOR = new AnatomicalAxis(AnatomicalDirection.ANTERIOR, AnatomicalDirection.POSTERIOR);
    public static final AnatomicalAxis POSTERIOR_ANTERIOR = new AnatomicalAxis(AnatomicalDirection.POSTERIOR, AnatomicalDirection.ANTERIOR);

    public static final AnatomicalAxis INFERIOR_SUPERIOR = new AnatomicalAxis(AnatomicalDirection.INFERIOR, AnatomicalDirection.SUPERIOR);
    public static final AnatomicalAxis SUPERIOR_INFERIOR = new AnatomicalAxis(AnatomicalDirection.SUPERIOR, AnatomicalDirection.INFERIOR);

    public static final AnatomicalAxis LEFT_RIGHT = new AnatomicalAxis(AnatomicalDirection.LEFT, AnatomicalDirection.RIGHT);
    public static final AnatomicalAxis RIGHT_LEFT = new AnatomicalAxis(AnatomicalDirection.RIGHT, AnatomicalDirection.LEFT);


    public final AnatomicalDirection min;
    public final AnatomicalDirection max;


    private static int count = 0;


    private AnatomicalAxis(AnatomicalDirection min, AnatomicalDirection max) {
        this.min = min;
        this.max = max;

        instances[count] = this;
        count++;
    }

    private AnatomicalAxis lookupAxis(AnatomicalDirection min, AnatomicalDirection max) {
        for (int i = 0; i < instances.length; i++) {
            if ((instances[i].min == min) && (instances[i].max == max)) {
                return instances[i];
            }
        }

        throw new IllegalArgumentException("Illegal axis parameters: " + min + ", " + max);
    }

    public static AnatomicalAxis matchAnatomy(AnatomicalDirection minDirection) {
        if (minDirection == AnatomicalDirection.LEFT) {
            return LEFT_RIGHT;
        } else if (minDirection == AnatomicalDirection.RIGHT) {
            return RIGHT_LEFT;
        } else if (minDirection == AnatomicalDirection.ANTERIOR) {
            return ANTERIOR_POSTERIOR;
        } else if (minDirection == AnatomicalDirection.POSTERIOR) {
            return POSTERIOR_ANTERIOR;
        } else if (minDirection == AnatomicalDirection.INFERIOR) {
            return INFERIOR_SUPERIOR;
        } else if (minDirection == AnatomicalDirection.SUPERIOR) {
            return SUPERIOR_INFERIOR;
        } else {
            throw new AssertionError();
        }
    }


    public AnatomicalDirection getMinDirection() {
        return min;
    }

    public AnatomicalDirection getMaxDirection() {
        return max;
    }

    public boolean sameAxis(AnatomicalAxis other) {
        if (other == this) return true;
        if (other == getFlippedAxis()) return true;

        return false;
    }

    public boolean sameDirection(AnatomicalAxis other) {
        if (other == this) return true;
        return false;
    }

    public double convertValue(AnatomicalAxis other, double value) {
        if (!other.sameAxis(this)) {
            throw new ImageAxis.IncompatibleAxisException("axis: " + other + " is incompatible with this axis " + this);
        }

        if (other == this) {
            return value;
        } else if (other == getFlippedAxis()) {
            return -value;
        }

        throw new AssertionError("Shouldn't ever get here.");
    }

    public AnatomicalAxis getFlippedAxis() {
        return lookupAxis(max, min);
    }

    public String toString() {
        return min + "-" + max;
    }

    public static class IndexDoNothing implements IndexConverter1D {
        public final int convert(int in) {
            return in;
        }
    }

    public static class IndexFlipper implements IndexConverter1D {

        int maxIdx;

        public IndexFlipper(int dimLen) {
            maxIdx = dimLen - 1;
        }

        public int convert(int in) {
            return maxIdx - in;
        }
    }


}
