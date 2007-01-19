package com.brainflow.utils;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 1, 2006
 * Time: 11:31:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConstantStringGenerator implements StringGenerator {

    String constant;


    public ConstantStringGenerator(String _constant) {
        constant = _constant;
    }

    public String getString() {
        return constant;
    }
}
