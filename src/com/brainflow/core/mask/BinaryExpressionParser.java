package com.brainflow.core.mask;

import jfun.parsec.*;
import test.Testable;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 7, 2008
 * Time: 1:51:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class BinaryExpressionParser {


    private static Binary<INode> toMap2(final Operation op) {
        return new Binary<INode>() {
            public INode map(INode o1, INode o2) {
                return new ComparisonNode(o1,o2,op);
            }
        };
    }

    @Testable
    public Parser<INode> createParser() {

        final Parser<_> s_whitespace = Scanners.isWhitespaces();
        final Parser<Tok> l_number = Lexers.decimal("number");
        final Parser<Tok> l_variable = Lexers.word("variable");

        final Terms ops = Terms.getOperatorsInstance("or", "and", "not", "-", "<", ">", "(", ")");

        final Parser<Tok> l_tok = Parsers.plus(ops.getLexer(), l_number, l_variable);
        final Parser<Tok[]> lexer = Lexers.lexeme(s_whitespace.many(), l_tok).followedBy(Parsers.eof());

        final Parser<INode> p_constant = Terms.decimalParser(new FromString<INode>() {
            public INode fromString(int from, int len, String s) {
                return new ConstantNode(Double.valueOf(s));
            }
        });

        final Parser<INode> p_variable = Terms.wordParser(new FromString<INode>() {
            public INode fromString(int from, int len, String s) {
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

        final Parser<Binary<INode>> p_lt = lt_op.seq(Parsers.retn(toMap2(Operation.LT)));
        final Parser<Binary<INode>> p_gt = gt_op.seq(Parsers.retn(toMap2(Operation.GT)));
        final Parser<Binary<INode>> p_and = and_op.seq(Parsers.retn(toMap2(Operation.AND)));
        final Parser<Binary<INode>> p_or = or_op.seq(Parsers.retn(toMap2(Operation.OR)));


        //lt_op.seq(Parsers.retn())


        final Parser<INode>[] expr_holder = new Parser[1];
        final Parser<INode> p_lazy_expr = Parsers.lazy(expr_holder);
        final Parser<INode> p_term = Parsers.plus(
                 Parsers.between(p_lparen, p_rparen, p_lazy_expr),
                 p_constant,
                 p_variable
         );

        final OperatorTable<INode> optable = new OperatorTable<INode>()
        .infixl(p_lt, 10)
        .infixl(p_gt, 10)
        .infixl(p_and, 20)
        .infixl(p_or, 20);


        final Parser<INode> p_expr = Expressions.buildExpressionParser(p_term, optable);
        expr_holder[0] = p_expr;
        return Parsers.parseTokens(lexer, p_expr.followedBy(Parsers.eof()), "mask calculator");

       


    }


    public static void main(String[] args) {

        Parser<INode> parser = new BinaryExpressionParser().createParser();
        INode node = parser.parse("(V1 > 5) and (V2 < 4)");
        VariableSubstitution builder = new VariableSubstitution(null);
        builder.start(node);
        DepthFirstAdapter walker = new DepthFirstAdapter();
        walker.start(node);
        
        

    }
}
