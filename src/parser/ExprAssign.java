package src.parser;

import src.analizador.Token;

import src.parser.Expression;

public class ExprAssign extends Expression {
    final Token name;
    final Expression value;

    ExprAssign(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }
}
