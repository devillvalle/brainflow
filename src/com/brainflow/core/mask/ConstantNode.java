package com.brainflow.core.mask;

import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 7, 2008
 * Time: 5:04:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConstantNode implements BaseNode {
        
    private double value;

    public ConstantNode(double value) {
        this.value = value;
    }

    public String toString() {
        return "value : " + value;
    }

    public List<BaseNode> getChildren() {
        return Arrays.asList();
    }
}
