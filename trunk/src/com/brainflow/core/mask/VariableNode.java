package com.brainflow.core.mask;

import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 7, 2008
 * Time: 5:05:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class VariableNode implements BaseNode {

    private String varName;

    public VariableNode(String varName) {
        this.varName = varName;
    }

    public String toString() {
        return "var : " + varName;
    }

    public List<BaseNode> getChildren() {
        return Arrays.asList();
    }
}