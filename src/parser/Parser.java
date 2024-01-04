package src.parser;

import src.analizador.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    private Token preanalisis;
    private int i = 0;
    private boolean hayErrores = false;
    private final List<Token> tokens;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    public boolean parse() {

        List<Statement> arbol = PROGRAM();

        if (preanalisis.tipo == TipoToken.EOF && !hayErrores) {
            System.out.println("Correcto");
            return true;
        } else {
            System.out.println("Se encontraron errores");
        }
        return false;
    }

    private List<Statement> PROGRAM() {
        List<Statement> statements = new ArrayList();
        DECLARATION(statements);
        return statements;
    }

    private void DECLARATION(List<Statement> statements) {
        if (hayErrores)
            return;
        if (preanalisis.tipo == TipoToken.FUN) {
            Statement stmt = FUN_DECL();
            statements.add(stmt);
            DECLARATION(statements);
        } else if (preanalisis.tipo == TipoToken.VAR) {
            Statement stmt = VAR_DECL();
            statements.add(stmt);
            DECLARATION(statements);
        } else if (preanalisis.tipo == TipoToken.BANG ||
                preanalisis.tipo == TipoToken.FOR ||
                preanalisis.tipo == TipoToken.IF ||
                preanalisis.tipo == TipoToken.PRINT ||
                preanalisis.tipo == TipoToken.RETURN ||
                preanalisis.tipo == TipoToken.WHILE ||
                preanalisis.tipo == TipoToken.LEFT_BRACE ||
                preanalisis.tipo == TipoToken.MINUS ||
                preanalisis.tipo == TipoToken.TRUE ||
                preanalisis.tipo == TipoToken.FALSE ||
                preanalisis.tipo == TipoToken.NULL ||
                preanalisis.tipo == TipoToken.NUMBER ||
                preanalisis.tipo == TipoToken.STRING ||
                preanalisis.tipo == TipoToken.IDENTIFIER ||
                preanalisis.tipo == TipoToken.LEFT_PAREN) {
            Statement stmt = STATEMENT();
            statements.add(stmt);
            DECLARATION(statements);
        }

    }

    private Statement FUN_DECL() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.FUN) {
            match(TipoToken.FUN);
            return FUNCTION();
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba 'fun'.");
            return null;
        }
    }

    private Statement VAR_DECL() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.VAR) {
            match(TipoToken.VAR);
            if (preanalisis.tipo == TipoToken.IDENTIFIER) {
                match(TipoToken.IDENTIFIER);
                Token name = tokens.get(i - 1);
                Expression exp = VAR_INIT();
                if (preanalisis.tipo == TipoToken.SEMICOLON) {
                    match(TipoToken.SEMICOLON);
                    return new StmtVar(name, exp);
                } else {
                    hayErrores = true;
                    System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                            + ". Se esperaba ';' o '='.");
                }
            } else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                        + ". Se esperaba 'identifier'.");
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba 'var'.");
        }
        return null;
    }

    private Expression VAR_INIT() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.EQUAL) {
            match(TipoToken.EQUAL);
            return EXPRESSION();
        }
        return null;
    }

    private Statement STATEMENT() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            return EXPR_STMT();
        } else if (preanalisis.tipo == TipoToken.FOR) {
            return FOR_STMT();
        } else if (preanalisis.tipo == TipoToken.IF) {
            return IF_STMT();
        } else if (preanalisis.tipo == TipoToken.PRINT) {
            return PRINT_STMT();
        } else if (preanalisis.tipo == TipoToken.RETURN) {
            return RETURN_STMT();
        } else if (preanalisis.tipo == TipoToken.WHILE) {
            return WHILE_STMT();
        } else if (preanalisis.tipo == TipoToken.LEFT_BRACE) {
            return BLOCK();
        }
        return null;
    }

    private Statement EXPR_STMT() {
        if (hayErrores)
            return null;
        EXPRESSION();
        if (preanalisis.tipo == TipoToken.SEMICOLON) {
            Expression expr = EXPRESSION();
            match(TipoToken.SEMICOLON);
            return new StmtExpression(expr);
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba ';'.");
        }
        return null;
    }

    Statement FOR_STMT() {
        if (hayErrores)
            return null;

        if (preanalisis.tipo == TipoToken.FOR) {
            match(TipoToken.FOR);
            if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
                match(TipoToken.LEFT_PAREN);
                Statement initializer = FOR_STMT_1();
                Expression condition = FOR_STMT_2();
                Expression increment = FOR_STMT_3();
                if (preanalisis.tipo == TipoToken.RIGHT_PAREN) {
                    match(TipoToken.RIGHT_PAREN);
                    Statement body = STATEMENT();

                    if (increment != null) {
                        body = new StmtBlock(Arrays.asList(body, new StmtExpression(increment)));
                    }
                    if (condition == null) {
                        condition = new ExprLiteral(true);
                    }
                    body = new StmtLoop(condition, body);
                    if (initializer != null) {
                        body = new StmtBlock(Arrays.asList(initializer, body));

                    }
                    return body;
                } else {
                    hayErrores = true;
                    System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                            + ". Se esperaba ')'.");
                }
            } else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                        + ". Se esperaba '('.");
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba 'for'.");
        }
        return null;
    }

    Statement FOR_STMT_1() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.VAR) {
            return VAR_DECL();
        } else if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            return EXPR_STMT();
        } else if (preanalisis.tipo == TipoToken.SEMICOLON) {
            match(TipoToken.SEMICOLON);
            return null;
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba 'var', una 'expresion' ó ';'.");
        }
        return null;

    }

    Expression FOR_STMT_2() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            Expression expr = EXPRESSION();
            match(TipoToken.SEMICOLON);
            return expr;

        } else if (preanalisis.tipo == TipoToken.SEMICOLON) {
            match(TipoToken.SEMICOLON);
            return null;
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba ';'.");
        }
        return null;
    }

    Expression FOR_STMT_3() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            return EXPRESSION();
        }
        return null;
    }

    public Statement IF_STMT() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.IF) {
            match(TipoToken.IF);
            if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
                match(TipoToken.LEFT_PAREN);

                Expression exp = EXPRESSION();
                if (preanalisis.tipo == TipoToken.RIGHT_PAREN) {
                    match(TipoToken.RIGHT_PAREN);

                    Statement thenBranch = STATEMENT();
                    Statement elseBranch = ELSE_STATEMENT();

                    return new StmtIf(exp, thenBranch, elseBranch);

                } else {
                    hayErrores = true;
                    System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                            + ". Se esperaba ')'.");
                }
            } else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                        + ". Se esperaba '('.");
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba 'if'.");
        }
        return null;
    }

    public Statement ELSE_STATEMENT() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.ELSE) {
            match(TipoToken.ELSE);
            return STATEMENT();
        }
        return null;
    }

    public Statement PRINT_STMT() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.PRINT) {
            match(TipoToken.PRINT);
            Expression exp = EXPRESSION();
            StmtPrint stm = new StmtPrint(exp);
            if (preanalisis.tipo == TipoToken.SEMICOLON) {
                match(TipoToken.SEMICOLON);
                return stm;
            } else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                        + ". Se esperaba ';'.");
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba 'print'.");
        }
        return null;
    }

    public Statement RETURN_STMT() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.RETURN) {
            match(TipoToken.RETURN);
            Expression exp = RETURN_EXP_OPC();
            if (preanalisis.tipo == TipoToken.SEMICOLON) {
                match(TipoToken.SEMICOLON);
                return new StmtReturn(exp);
            } else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                        + ". Se esperaba ';'.");
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba 'return'.");
        }
        return null;
    }

    public Expression RETURN_EXP_OPC() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            return EXPRESSION();
        }
        // EPSILON
        return null;
    }

    public Statement WHILE_STMT() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.WHILE) {
            match(TipoToken.WHILE);
            if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
                match(TipoToken.LEFT_PAREN);
                Expression exp = EXPRESSION();
                if (preanalisis.tipo == TipoToken.RIGHT_PAREN) {
                    match(TipoToken.RIGHT_PAREN);
                    Statement statement = STATEMENT();
                    return new StmtLoop(exp, statement);
                } else {
                    hayErrores = true;
                    System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                            + ". Se esperaba ')'.");
                }
            } else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                        + ". Se esperaba '('.");
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba 'while'");
        }
        return null;
    }

    public Statement BLOCK() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.LEFT_BRACE) {
            match(TipoToken.LEFT_BRACE);
            List<Statement> statements = new ArrayList<>();
            DECLARATION(statements);
            if (preanalisis.tipo == TipoToken.RIGHT_BRACE) {
                match(TipoToken.RIGHT_BRACE);
                return new StmtBlock(statements);
            } else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                        + ". Se esperaba '}'.");
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba '{'.");
        }
        return null;
    }

    public Expression EXPRESSION() {

        if (hayErrores)
            return null;
        return ASSIGNMENT();

    }

    public Expression ASSIGNMENT() {
        if (hayErrores)
            return null;
        Expression exp = LOGIC_OR();
        return ASSIGNMENT_OPC(exp);
    }

    public Expression ASSIGNMENT_OPC(Expression exp) {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.EQUAL) {
            match(TipoToken.EQUAL);
            Token previous = tokens.get(i - 1);
            Expression expR = EXPRESSION();
            return new ExprAssign(previous, expR);
        }
        return exp;
    }

    public Expression LOGIC_OR() {
        if (hayErrores)
            return null;
        Expression exp = LOGIC_AND();
        exp = LOGIC_OR_2(exp);
        return exp;
    }

    public Expression LOGIC_OR_2(Expression exp) {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.OR) {
            match(TipoToken.OR);
            Token previous = tokens.get(i - 1);
            Expression expR = LOGIC_AND();
            ExprLogical expI = new ExprLogical(exp, previous, expR);
            return LOGIC_OR_2(expI);
        }
        return null;
    }

    public Expression LOGIC_AND() {
        if (hayErrores)
            return null;
        Expression exp = EQUALITY();
        exp = LOGIC_AND_2(exp);
        return exp;
    }

    public Expression LOGIC_AND_2(Expression exp) {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.AND) {
            match(TipoToken.AND);
            Token previous = tokens.get(i - 1);
            Expression expR = EQUALITY();
            ExprLogical expI = new ExprLogical(exp, previous, expR);
            return LOGIC_AND_2(expI);
        }
        return null;
    }

    public Expression EQUALITY() {
        if (hayErrores)
            return null;
        Expression exp = COMPARISON();
        exp = EQUALITY_2(exp);
        return exp;
    }

    public Expression EQUALITY_2(Expression exp) {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.BANG_EQUAL) {
            match(TipoToken.BANG_EQUAL);
            Token previous = tokens.get(i - 1);
            Expression expR = COMPARISON();
            ExprBinary expb = new ExprBinary(exp, previous, expR);
            return EQUALITY_2(expb);
        } else if (preanalisis.tipo == TipoToken.EQUAL_EQUAL) {
            match(TipoToken.EQUAL_EQUAL);
            Token previous = tokens.get(i - 1);
            Expression expR = COMPARISON();
            ExprBinary expb = new ExprBinary(exp, previous, expR);
            return EQUALITY_2(expb);
        }
        return null;
    }

    public Expression COMPARISON() {
        if (hayErrores)
            return null;
        Expression exp = TERM();
        return COMPARISON_2(exp);
    }

    public Expression COMPARISON_2(Expression exp) {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.LESS
                || preanalisis.tipo == TipoToken.LESS_EQUAL
                || preanalisis.tipo == TipoToken.GREATER
                || preanalisis.tipo == TipoToken.GREATER_EQUAL) {
            match(TipoToken.LESS);
            match(TipoToken.LESS_EQUAL);
            match(TipoToken.GREATER);
            match(TipoToken.GREATER_EQUAL);

            Token previous = tokens.get(i - 1);
            Expression expR = TERM();
            ExprBinary expI = new ExprBinary(exp, previous, expR);
            return COMPARISON_2(expI);
        }
        return null;
    }

    public Expression TERM() {
        if (hayErrores)
            return null;
        Expression exp = FACTOR();
        return TERM_2(exp);
    }

    public Expression TERM_2(Expression exp) {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.PLUS || preanalisis.tipo == TipoToken.MINUS) {
            match(TipoToken.MINUS);
            match(TipoToken.PLUS);
            Token previous = tokens.get(i - 1);
            Expression exR = FACTOR();
            ExprBinary exb = new ExprBinary(exp, previous, exR);
            return TERM_2(exb);
        }
        return null;
    }

    public Expression FACTOR() {
        if (hayErrores)
            return null;
        Expression exp = UNARY();
        exp = FACTOR_2(exp);
        return exp;
    }

    public Expression FACTOR_2(Expression exp) {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.SLASH) {
            match(TipoToken.SLASH);
            Token previous = tokens.get(i - 1);
            Expression expR = UNARY();
            ExprBinary expb = new ExprBinary(exp, previous, expR);
            return FACTOR_2(expb);
        } else if (preanalisis.tipo == TipoToken.STAR) {
            match(TipoToken.STAR);
            Token previous = tokens.get(i - 1);
            Expression expR = UNARY();
            ExprBinary expb = new ExprBinary(exp, previous, expR);
            return FACTOR_2(expb);
        }
        return null;
    }

    public Expression UNARY() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.BANG) {
            match(TipoToken.BANG);
            Token previous = tokens.get(i - 1);
            Expression exp = UNARY();
            return new ExprUnary(previous, exp);
        } else if (preanalisis.tipo == TipoToken.MINUS) {
            match(TipoToken.MINUS);
            Token previus = tokens.get(i - 1);
            Expression exp = UNARY();
            return new ExprUnary(previus, exp);
        } else if (preanalisis.tipo == TipoToken.TRUE ||
                preanalisis.tipo == TipoToken.FALSE ||
                preanalisis.tipo == TipoToken.NULL ||
                preanalisis.tipo == TipoToken.NUMBER ||
                preanalisis.tipo == TipoToken.STRING ||
                preanalisis.tipo == TipoToken.IDENTIFIER ||
                preanalisis.tipo == TipoToken.LEFT_PAREN) {
            return CALL();
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba '!', '-', 'true', 'false', 'null', 'number', 'string' o 'identifier'.");
            return null;
        }
    }

    public Expression CALL() {
        if (hayErrores)
            return null;
        Expression exp = PRIMARY();
        exp = CALL_2(exp);
        return exp;
    }

    public Expression CALL_2(Expression exp) {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
            match(TipoToken.LEFT_PAREN);
            List<Expression> arguments = ARGUMENTS_OPC();
            if (preanalisis.tipo == TipoToken.RIGHT_PAREN) {
                match(TipoToken.RIGHT_PAREN);
                ExprCallFunction ecf = new ExprCallFunction(exp, arguments);
                return CALL_2(ecf);
            } else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                        + ". Se esperaba '('.");
            }
        }
        return null;
        // epsilon
    }

    public Expression PRIMARY() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.TRUE) {
            match(TipoToken.TRUE);
            return new ExprLiteral(true);
        } else if (preanalisis.tipo == TipoToken.FALSE) {
            match(TipoToken.FALSE);
            return new ExprLiteral(false);
        } else if (preanalisis.tipo == TipoToken.NUMBER) {
            match(TipoToken.NUMBER);
            return new ExprLiteral(preanalisis.literal);
        } else if (preanalisis.tipo == TipoToken.STRING) {
            match(TipoToken.STRING);
            return new ExprLiteral(preanalisis.literal);
        } else if (preanalisis.tipo == TipoToken.IDENTIFIER) {
            match(TipoToken.IDENTIFIER);
            return new ExprVariable(preanalisis.lexema);
        } else if (preanalisis.tipo == TipoToken.NULL) {
            match(TipoToken.NULL);
            return new ExprLiteral(null);
        } else if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
            match(TipoToken.LEFT_PAREN);

            Expression expr = EXPRESSION();
            if (preanalisis.tipo == TipoToken.RIGHT_PAREN) {
                match(TipoToken.RIGHT_PAREN);
                return new ExprGrouping(expr);
            } else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                        + ". Se esperaba ')'.");
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba 'true', 'false', 'null', 'number', 'string' o 'identifier'.");
        }
        return null;
    }

    private Statement FUNCTION() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.IDENTIFIER) {
            match(TipoToken.IDENTIFIER);
            Token name = tokens.get(i - 1);
            if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
                match(TipoToken.LEFT_PAREN);
                List<Token> parameters = PARAMETERS_OPC();
                if (preanalisis.tipo == TipoToken.RIGHT_PAREN) {
                    match(TipoToken.RIGHT_PAREN);
                    StmtBlock body = (StmtBlock) BLOCK();
                    return new StmtFunction(name, parameters, body);
                } else {
                    hayErrores = true;
                    System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                            + ". Se esperaba ')'.");
                }
            } else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                        + ". Se esperaba '('.");
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba 'identifier'.");
        }
        return null;
    }

    private List<Token> PARAMETERS_OPC() {
        if (hayErrores)
            return null;
        return PARAMETERS();
    }

    private List<Token> PARAMETERS() {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.IDENTIFIER) {
            match(TipoToken.IDENTIFIER);
            List<Token> parameters = new ArrayList<>();
            parameters.add(tokens.get(i - 1));
            PARAMETERS_2(parameters);
            return parameters;
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                    + ". Se esperaba 'identifier'.");
        }
        return null;
    }

    private List<Token> PARAMETERS_2(List<Token> parameters) {
        if (hayErrores)
            return null;
        if (preanalisis.tipo == TipoToken.COMMA) {
            match(TipoToken.COMMA);
            if (preanalisis.tipo == TipoToken.IDENTIFIER) {
                match(TipoToken.IDENTIFIER);
                parameters.add(tokens.get(i - 1));
                PARAMETERS_2(parameters);
            } else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea + ", columna: " + preanalisis.columnaE
                        + ". Se esperaba 'identifier'.");
            }
        }
        return null;
    }

    // ARGUMENTS_OPC -> EXPRESSION ARGUMENTS | e
    private List<Expression> ARGUMENTS_OPC() {
        if (hayErrores)
            return null;
        List<Expression> expressions = new ArrayList<>();
        Expression exp = EXPRESSION();
        expressions.add(exp);
        ARGUMENTS(expressions);
        return expressions;
    }

    // ARGUMENTS -> , EXPRESSION ARGUMENTS | e
    private void ARGUMENTS(List<Expression> expressions) {
        if (hayErrores)
            return;
        if (preanalisis.tipo == TipoToken.COMMA) {
            match(TipoToken.COMMA);
            Expression expr = EXPRESSION();
            expressions.add(expr);
            ARGUMENTS(expressions);
        }
        // epsilon
    }

    private void match(TipoToken tt) {
        if (preanalisis.tipo == tt) {
            i++;
            preanalisis = tokens.get(i);
        }
    }
}
