package src.parser;

import src.analizador.Token;

public class ExprUnary extends Expression {
    final Token operator;
    final Expression right;

    ExprUnary(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }
}
