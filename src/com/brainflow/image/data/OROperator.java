package com.brainflow.image.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 10, 2007
 * Time: 1:08:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class OROperator implements IOperator {

    public int filter(int a, int b) {
        return a | b;
    }
}
