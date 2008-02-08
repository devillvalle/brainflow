package com.brainflow.core.mask;

import jfun.parsec.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 7, 2008
 * Time: 1:51:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class BinaryExpressionParser {


    private static Binary<BaseNode> toMap2(final String op) {
        return new Binary<BaseNode>() {
            public BaseNode map(BaseNode o1, BaseNode o2) {
                return new ComparisonNode(o1,o2,op);
            }
        };
    }

    public Parser<BaseNode> createParser() {

        final Parser<_> s_whitespace = Scanners.isWhitespaces();
        final Parser<Tok> l_number = Lexers.decimal("number");
        final Parser<Tok> l_variable = Lexers.word("variable");

        final Terms ops = Terms.getOperatorsInstance("or", "and", "not", "-", "<", ">", "(", ")");

        final Parser<Tok> l_tok = Parsers.plus(ops.getLexer(), l_number, l_variable);
        final Parser<Tok[]> lexer = Lexers.lexeme(s_whitespace.many(), l_tok).followedBy(Parsers.eof());

        final Parser<BaseNode> p_constant = Terms.decimalParser(new FromString<BaseNode>() {
            public BaseNode fromString(int from, int len, String s) {
                return new ConstantNode(Double.valueOf(s));
            }
        });

        final Parser<BaseNode> p_variable = Terms.wordParser(new FromString<BaseNode>() {
            public BaseNode fromString(int from, int len, String s) {
                return new VariableNode(s);
            }
        });

        final Parser<Tok> or_op = ops.getParser("or");

        final Parser<Tok> and_op = ops.getParser("and");
        final Parser<Tok> not_op = ops.getParser("not");
        final Parser<Tok> gt_op = ops.getParser(">");
        final Parser<Tok> lt_op = ops.getParser("<");
        final Parser<Tok> neg_op = ops.getParser("-");
        final Parser<Tok> p_lparen = ops.getParser("(");
        final Parser<Tok> p_rparen = ops.getParser(")");

        final Parser<Binary<BaseNode>> p_lt = lt_op.seq(Parsers.retn(toMap2("<")));
        final Parser<Binary<BaseNode>> p_gt = gt_op.seq(Parsers.retn(toMap2(">")));
        final Parser<Binary<BaseNode>> p_and = and_op.seq(Parsers.retn(toMap2("and")));
        final Parser<Binary<BaseNode>> p_or = or_op.seq(Parsers.retn(toMap2("not")));


        //lt_op.seq(Parsers.retn())


        final Parser<BaseNode>[] expr_holder = new Parser[1];
        final Parser<BaseNode> p_lazy_expr = Parsers.lazy(expr_holder);
        final Parser<BaseNode> p_term = Parsers.plus(
                 Parsers.between(p_lparen, p_rparen, p_lazy_expr),
                 p_constant,
                 p_variable
         );

        final OperatorTable<BaseNode> optable = new OperatorTable<BaseNode>()
        .infixl(p_lt, 10)
        .infixl(p_gt, 10)
        .infixl(p_and, 20)
        .infixl(p_or, 20);


        final Parser<BaseNode> p_expr = Expressions.buildExpressionParser(p_term, optable);
        expr_holder[0] = p_expr;
        return Parsers.parseTokens(lexer, p_expr.followedBy(Parsers.eof()), "mask calculator");

       


    }

    public static class MaskRef {

        MaskRef(String operation, double value, String variable) {
            this.operation = operation;
            this.value = value;
            this.variable = variable;
        }

        public String variable;

        public double value;

        public String operation;

        public String toString() {
            return variable + " " + operation + " " + value;
        }
    }

    public static void main(String[] args) {

        Parser<BaseNode> parser = new BinaryExpressionParser().createParser();
        BaseNode node = parser.parse("(a > 5) and (b < 4)");
        System.out.println(node);
        

    }
}
