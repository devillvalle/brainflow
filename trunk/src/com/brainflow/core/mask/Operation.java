package com.brainflow.core.mask;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 3:17:52 PM
 * To change this template use File | Settings | File Templates.
 */
public enum Operation {

    AND("and"),
    OR("or"),
    NOT("not"),
    GT(">"),
    LT("<"),
    EQ("=="),
    GT_EQ(">="),
    LT_EQ("<=");

    private String id;

    Operation(String _id) {
        id = _id;
    }

    public String getID() {
        return id;
    }


    
}
