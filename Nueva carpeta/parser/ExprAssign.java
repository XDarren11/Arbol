package parser;

import interpreter.Token;

import parser.Expression;

public class ExprAssign extends Expression {
    final Token name;
    final Expression value;

    ExprAssign(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }
}