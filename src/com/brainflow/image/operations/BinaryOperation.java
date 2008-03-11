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

    public static final BinaryOperation GT = new _GT_();

    public static final BinaryOperation GT_EQ = new _GTEQ_();

    public static final BinaryOperation LT = new _LT_();

    public static final BinaryOperation LT_EQ = new _LTEQ_();

    public abstract int compute(int left, int right);

     public abstract int compute(double left, double right);

    protected BinaryOperand operand = null;

    public BinaryOperand getOperand() {
        return operand;
    }

    private static class _AND_ extends BinaryOperation {

        public _AND_() {
            operand = BinaryOperand.AND;
        }


        public final int compute(int left, int right) {
            return left & right;
        }

        public final int compute(double left, double right) {
            return (left > 0 && right > 0) ? 1 : 0;
        }

        public String toString() {
            return operand.toString();
        }
    }

    private static class _OR_ extends BinaryOperation {

        public _OR_() {
            operand = BinaryOperand.OR;
        }

        public final int compute(int left, int right) {
           return (left > 0 || right > 0) ? 1 : 0;
        }

        public final int compute(double left, double right) {
            return (left > 0 || right > 0) ? 1 : 0;
        }

        public String toString() {
            return operand.toString();
        }
    }

    private static class _GT_ extends BinaryOperation {
        public _GT_() {
            operand = BinaryOperand.GT;
        }

        public final int compute(int left, int right) {
            return (left - right) > 0 ? 1 : 0;
        }

        public String toString() {
            return operand.toString();
        }

    }

    private static class _GTEQ_ extends BinaryOperation {
        public _GTEQ_() {
            operand = BinaryOperand.GT_EQ;
        }

        public final int compute(int left, int right) {
            return (left - right) >= 0 ? 1 : 0;
        }

        public String toString() {
            return operand.toString();
        }

    }

    private static class _LTEQ_ extends BinaryOperation {
        public _LTEQ_() {
            operand = BinaryOperand.LT_EQ;
        }

        public final int compute(int left, int right) {
            return (left - right) <= 0 ? 1 : 0;
        }

        public String toString() {
            return operand.toString();
        }

    }

    private static class _LT_ extends BinaryOperation {
        public _LT_() {
            operand = BinaryOperand.LT;
        }

        public final int compute(int left, int right) {
            return (left - right) < 0 ? 1 : 0;
        }

        public String toString() {
            return operand.toString();
        }

    }





}
