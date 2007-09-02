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

    protected BinaryOperand operand = BinaryOperand.AND;


    private static class _AND_ extends BinaryOperation {

        public _AND_() {
            operand = BinaryOperand.AND;
        }

        public final int compute(int left, int right) {
            return left & right;
        }

        public String toString() {
            return "AND";
        }
    }

    private static class _OR_ extends BinaryOperation {

        public _OR_() {
            operand = BinaryOperand.OR;
        }

        public final int compute(int left, int right) {
            return left | right;
        }

        public String toString() {
            return "OR";
        }
    }


    public BinaryOperand getOperand() {
        return operand;

    }


}
