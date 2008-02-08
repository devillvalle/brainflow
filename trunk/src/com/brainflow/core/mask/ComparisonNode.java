package com.brainflow.core.mask;

import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 7, 2008
 * Time: 5:14:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ComparisonNode implements BaseNode {

    private BaseNode left;

    private BaseNode right;

    private String operation;

    public ComparisonNode(BaseNode left, BaseNode right, String operation) {
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    public List<BaseNode> getChildren() {
        return Arrays.asList(left, right);
    }


    public String toString() {
        return "ComparisonNode{" +
                "left=" + left +
                ", right=" + right +
                ", operation='" + operation + '\'' +
                '}';
    }
}
