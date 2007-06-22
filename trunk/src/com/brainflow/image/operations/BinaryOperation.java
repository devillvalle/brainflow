package com.brainflow.image.operations;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 28, 2007
 * Time: 11:29:58 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BinaryOperation {

    public static final BinaryOperation AND = new _AND_();

    public static final BinaryOperation OR = new _OR_();

    public abstract int compute(int left, int right);


    private static class _AND_ extends BinaryOperation {
        public final int compute(int left, int right) {
            return left & right;
        }
    }

    private static class _OR_ extends BinaryOperation {
        public final int compute(int left, int right) {
            return left | right;
        }
    }



    
}
