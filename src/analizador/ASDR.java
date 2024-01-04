// package analizador;

// import java.util.List;

// public class ASDR implements parser {

// private int i = 0;
// private boolean hayErrores = false;
// private Token preanalisis;
// private final List<Token> tokens;

// public ASDR(List<Token> tokens) {
// this.tokens = tokens;
// preanalisis = this.tokens.get(i);
// }

// @Override
// public boolean parse() {

// PROGRAM();

// if (preanalisis.tipo == TipoToken.EOF && !hayErrores) {
// System.out.println("Consulta correcta");
// return true;
// } else {
// System.out.println("Se encontraron errores");
// }

// return false;
// }

// private void PROGRAM() {
// DECLARATION();
// }

// private void DECLARATION() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.FUN) {
// FUN_DECL();
// DECLARATION();
// } else if (preanalisis.tipo == TipoToken.VAR) {
// VAR_DECL();
// DECLARATION();
// } else if (preanalisis.tipo == TipoToken.BANG ||
// preanalisis.tipo == TipoToken.FOR ||
// preanalisis.tipo == TipoToken.IF ||
// preanalisis.tipo == TipoToken.PRINT ||
// preanalisis.tipo == TipoToken.RETURN ||
// preanalisis.tipo == TipoToken.WHILE ||
// preanalisis.tipo == TipoToken.LEFT_BRACE ||
// preanalisis.tipo == TipoToken.MINUS ||
// preanalisis.tipo == TipoToken.TRUE ||
// preanalisis.tipo == TipoToken.FALSE ||
// preanalisis.tipo == TipoToken.NULL ||
// preanalisis.tipo == TipoToken.NUMBER ||
// preanalisis.tipo == TipoToken.STRING ||
// preanalisis.tipo == TipoToken.IDENTIFIER ||
// preanalisis.tipo == TipoToken.LEFT_PAREN) {
// STATEMENT();
// DECLARATION();
// }

// }

// private void FUN_DECL() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.FUN) {
// match(TipoToken.FUN);
// FUNCTION();
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba 'fun'.");
// }
// }

// private void VAR_DECL() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.VAR) {
// match(TipoToken.VAR);
// if (preanalisis.tipo == TipoToken.IDENTIFIER) {
// match(TipoToken.IDENTIFIER);
// VAR_INIT();
// if (preanalisis.tipo == TipoToken.SEMICOLON) {
// match(TipoToken.SEMICOLON);
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba ';' o '='.");
// }
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba 'identifier'.");
// }
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba 'var'.");
// }
// }

// private void VAR_INIT() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.EQUAL) {
// match(TipoToken.EQUAL);
// EXPRESSION();
// }
// }

// private void STATEMENT() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS
// || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE
// || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER
// || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo ==
// TipoToken.IDENTIFIER
// || preanalisis.tipo == TipoToken.LEFT_PAREN) {
// EXPR_STMT();
// } else if (preanalisis.tipo == TipoToken.FOR) {
// FOR_STMT();
// } else if (preanalisis.tipo == TipoToken.IF) {
// IF_STMT();
// } else if (preanalisis.tipo == TipoToken.PRINT) {
// PRINT_STMT();
// } else if (preanalisis.tipo == TipoToken.RETURN) {
// RETURN_STMT();
// } else if (preanalisis.tipo == TipoToken.WHILE) {
// WHILE_STMT();
// } else if (preanalisis.tipo == TipoToken.LEFT_BRACE) {
// BLOCK();
// }
// }

// private void EXPR_STMT() {
// if (hayErrores)
// return;
// EXPRESSION();
// if (preanalisis.tipo == TipoToken.SEMICOLON) {
// match(TipoToken.SEMICOLON);
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba ';'.");
// }
// }

// void FOR_STMT() {
// if (hayErrores)
// return;

// if (preanalisis.tipo == TipoToken.FOR) {
// match(TipoToken.FOR);
// if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
// match(TipoToken.LEFT_PAREN);
// FOR_STMT_1();
// FOR_STMT_2();
// FOR_STMT_3();
// if (preanalisis.tipo == TipoToken.RIGHT_PAREN) {
// match(TipoToken.RIGHT_PAREN);
// STATEMENT();
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba ')'.");
// }
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba '('.");
// }
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba 'for'.");
// }
// }

// void FOR_STMT_1() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.VAR) {
// VAR_DECL();
// } else if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo ==
// TipoToken.MINUS
// || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE
// || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER
// || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo ==
// TipoToken.IDENTIFIER
// || preanalisis.tipo == TipoToken.LEFT_PAREN) {
// EXPR_STMT();
// } else if (preanalisis.tipo == TipoToken.SEMICOLON) {
// match(TipoToken.SEMICOLON);
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba 'var', una 'expresion' ó ';'.");
// }

// }

// void FOR_STMT_2() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS
// || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE
// || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER
// || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo ==
// TipoToken.IDENTIFIER
// || preanalisis.tipo == TipoToken.LEFT_PAREN) {
// EXPRESSION();
// if (preanalisis.tipo == TipoToken.SEMICOLON) {
// match(TipoToken.SEMICOLON);
// }
// } else if (preanalisis.tipo == TipoToken.SEMICOLON) {
// match(TipoToken.SEMICOLON);
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba ';'.");
// }
// }

// void FOR_STMT_3() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS
// || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE
// || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER
// || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo ==
// TipoToken.IDENTIFIER
// || preanalisis.tipo == TipoToken.LEFT_PAREN) {
// EXPRESSION();
// }
// }

// void IF_STMT() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.IF) {
// match(TipoToken.IF);
// if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
// match(TipoToken.LEFT_PAREN);
// EXPRESSION();
// if (preanalisis.tipo == TipoToken.RIGHT_PAREN) {
// match(TipoToken.RIGHT_PAREN);
// STATEMENT();
// ELSE_STATEMENT();
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba ')'.");
// }
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba '('.");
// }
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba 'if'.");
// }
// }

// void ELSE_STATEMENT() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.ELSE) {
// match(TipoToken.ELSE);
// STATEMENT();
// }

// }

// void PRINT_STMT() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.PRINT) {
// match(TipoToken.PRINT);
// EXPRESSION();
// if (preanalisis.tipo == TipoToken.SEMICOLON) {
// match(TipoToken.SEMICOLON);
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba ';'.");
// }
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba 'print'.");
// }
// }

// void RETURN_STMT() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.RETURN) {
// match(TipoToken.RETURN);
// RETURN_EXP_OPC();
// if (preanalisis.tipo == TipoToken.SEMICOLON) {
// match(TipoToken.SEMICOLON);
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba ';'.");
// }
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba 'return'.");
// }
// }

// void RETURN_EXP_OPC() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS
// || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE
// || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER
// || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo ==
// TipoToken.IDENTIFIER
// || preanalisis.tipo == TipoToken.LEFT_PAREN) {
// EXPRESSION();
// }

// }

// void WHILE_STMT() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.WHILE) {
// match(TipoToken.WHILE);
// if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
// match(TipoToken.LEFT_PAREN);
// EXPRESSION();
// if (preanalisis.tipo == TipoToken.RIGHT_PAREN) {
// match(TipoToken.RIGHT_PAREN);
// STATEMENT();
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba ')'.");
// }
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba '('.");
// }
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba 'while'");
// }
// }

// void BLOCK() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.LEFT_BRACE) {
// match(TipoToken.LEFT_BRACE);
// DECLARATION();
// if (preanalisis.tipo == TipoToken.RIGHT_BRACE) {
// match(TipoToken.RIGHT_BRACE);
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba '}'.");
// }
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba '{'.");
// }
// }

// public void EXPRESSION() {
// if (hayErrores)
// return;
// ASSIGNMENT();
// }

// public void ASSIGNMENT() {
// if (hayErrores)
// return;
// LOGIC_OR();
// ASSIGNMENT_OPC();
// }

// public void ASSIGNMENT_OPC() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.EQUAL) {
// match(TipoToken.EQUAL);
// EXPRESSION();
// }

// }

// public void LOGIC_OR() {
// if (hayErrores)
// return;
// LOGIC_AND();
// LOGIC_OR_2();
// }

// public void LOGIC_OR_2() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.OR) {
// match(TipoToken.OR);
// LOGIC_AND();
// LOGIC_OR_2();
// }
// }

// public void LOGIC_AND() {
// if (hayErrores)
// return;
// EQUALITY();
// LOGIC_AND_2();
// }

// public void LOGIC_AND_2() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.AND) {
// match(TipoToken.AND);
// EQUALITY();
// LOGIC_AND_2();
// }
// }

// public void EQUALITY() {
// if (hayErrores)
// return;
// COMPARISON();
// EQUALITY_2();
// }

// public void EQUALITY_2() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.BANG_EQUAL) {
// match(TipoToken.BANG_EQUAL);
// COMPARISON();
// EQUALITY_2();
// } else if (preanalisis.tipo == TipoToken.EQUAL_EQUAL) {
// match(TipoToken.EQUAL_EQUAL);
// COMPARISON();
// EQUALITY_2();
// }
// }

// public void COMPARISON() {
// if (hayErrores)
// return;
// TERM();
// COMPARISON_2();
// }

// public void COMPARISON_2() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.LESS
// || preanalisis.tipo == TipoToken.LESS_EQUAL
// || preanalisis.tipo == TipoToken.GREATER
// || preanalisis.tipo == TipoToken.GREATER_EQUAL) {
// match(TipoToken.LESS);
// match(TipoToken.LESS_EQUAL);
// match(TipoToken.GREATER);
// match(TipoToken.GREATER_EQUAL);

// TERM();
// COMPARISON_2();
// }
// }

// public void TERM() {
// if (hayErrores)
// return;
// FACTOR();
// TERM_2();
// }

// public void TERM_2() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.PLUS || preanalisis.tipo ==
// TipoToken.MINUS) {
// match(TipoToken.MINUS);
// match(TipoToken.PLUS);
// FACTOR();
// TERM_2();
// }
// }

// public void FACTOR() {
// if (hayErrores)
// return;
// UNARY();
// FACTOR_2();
// }

// public void FACTOR_2() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.SLASH || preanalisis.tipo ==
// TipoToken.STAR) {
// match(TipoToken.SLASH);
// match(TipoToken.STAR);
// UNARY();
// FACTOR_2();
// }
// }

// public void UNARY() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo ==
// TipoToken.MINUS) {
// match(TipoToken.BANG);
// match(TipoToken.MINUS);
// UNARY();
// } else if (preanalisis.tipo == TipoToken.TRUE ||
// preanalisis.tipo == TipoToken.FALSE ||
// preanalisis.tipo == TipoToken.NULL ||
// preanalisis.tipo == TipoToken.NUMBER ||
// preanalisis.tipo == TipoToken.STRING ||
// preanalisis.tipo == TipoToken.IDENTIFIER ||
// preanalisis.tipo == TipoToken.LEFT_PAREN) {
// CALL();
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba '!', '-', 'true', 'false', 'null', 'number', 'string' o
// 'identifier'.");
// }
// }

// public void CALL() {
// if (hayErrores)
// return;
// PRIMARY();
// CALL_2();
// }

// public void CALL_2() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
// match(TipoToken.LEFT_PAREN);
// ARGUMENTS_OPC();
// if (preanalisis.tipo == TipoToken.RIGHT_PAREN) {
// match(TipoToken.RIGHT_PAREN);
// CALL_2();
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba '('.");
// }
// }
// }

// public void PRIMARY() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.TRUE ||
// preanalisis.tipo == TipoToken.FALSE ||
// preanalisis.tipo == TipoToken.NULL ||
// preanalisis.tipo == TipoToken.NUMBER ||
// preanalisis.tipo == TipoToken.STRING ||
// preanalisis.tipo == TipoToken.IDENTIFIER) {

// match(TipoToken.TRUE);
// match(TipoToken.FALSE);
// match(TipoToken.NULL);
// match(TipoToken.NUMBER);
// match(TipoToken.STRING);
// match(TipoToken.IDENTIFIER);
// } else if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
// match(TipoToken.LEFT_PAREN);
// EXPRESSION();
// if (preanalisis.tipo == TipoToken.RIGHT_PAREN) {
// match(TipoToken.RIGHT_PAREN);
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba ')'.");
// }
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba 'true', 'false', 'null', 'number', 'string' o
// 'identifier'.");
// }

// }

// private void FUNCTION() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.IDENTIFIER) {
// match(TipoToken.IDENTIFIER);
// if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
// match(TipoToken.LEFT_PAREN);
// PARAMETERS_OPC();
// if (preanalisis.tipo == TipoToken.RIGHT_PAREN) {
// match(TipoToken.RIGHT_PAREN);
// BLOCK();
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba ')'.");
// }
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba '('.");
// }
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba 'identifier'.");
// }
// }

// private void FUNCTIONS() {
// if (hayErrores)
// return;
// FUN_DECL();
// FUNCTIONS();
// }

// private void PARAMETERS_OPC() {
// if (hayErrores)
// return;
// PARAMETERS();
// }

// private void PARAMETERS() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.IDENTIFIER) {
// match(TipoToken.IDENTIFIER);
// PARAMETERS_2();
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba 'identifier'.");
// }
// }

// private void PARAMETERS_2() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.COMMA) {
// match(TipoToken.COMMA);
// if (preanalisis.tipo == TipoToken.IDENTIFIER) {
// match(TipoToken.IDENTIFIER);
// PARAMETERS_2();
// } else {
// hayErrores = true;
// System.out.println("Error en la línea " + preanalisis.linea + ", columna: " +
// preanalisis.columnaE
// + ". Se esperaba 'identifier'.");
// }
// }
// }

// private void ARGUMENTS_OPC() {
// if (hayErrores)
// return;
// EXPRESSION();
// ARGUMENTS();
// }

// private void ARGUMENTS() {
// if (hayErrores)
// return;
// if (preanalisis.tipo == TipoToken.COMMA) {
// match(TipoToken.COMMA);
// EXPRESSION();
// ARGUMENTS();
// }
// }

// private void match(TipoToken tt) {
// if (preanalisis.tipo == tt) {
// i++;
// preanalisis = tokens.get(i);
// }
// }
// }