package org.boxwood;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 3, 2008
 * Time: 9:39:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class NumberVariable implements IVariable<Double> {

    private final double[] vals;

    public NumberVariable(int length) {
        vals = new double[length];
    }

    public NumberVariable(double[] vals) {
        this.vals = vals;
    }

    public int length() {
        return vals.length;
    }

    public Double valueAt(int i) {
        return vals[i];
    }
}
