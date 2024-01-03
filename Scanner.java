package interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TipoToken> palabrasReservadas;
    private static final Map<String, TipoToken> tokensUnSimbolo;
    private static final Map<String, TipoToken> tokensDosSimbolos;

    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and", TipoToken.AND);
        palabrasReservadas.put("else", TipoToken.ELSE);
        palabrasReservadas.put("false", TipoToken.FALSE);
        palabrasReservadas.put("for", TipoToken.FOR);
        palabrasReservadas.put("fun", TipoToken.FUN);
        palabrasReservadas.put("if", TipoToken.IF);
        palabrasReservadas.put("null", TipoToken.NULL);
        palabrasReservadas.put("or", TipoToken.OR);
        palabrasReservadas.put("print", TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true", TipoToken.TRUE);
        palabrasReservadas.put("var", TipoToken.VAR);
        palabrasReservadas.put("while", TipoToken.WHILE);
    }
    static {
        tokensUnSimbolo = new HashMap<>();
        tokensUnSimbolo.put("(", TipoToken.LEFT_PAREN);
        tokensUnSimbolo.put(")", TipoToken.RIGHT_PAREN);
        tokensUnSimbolo.put("{", TipoToken.LEFT_BRACE);
        tokensUnSimbolo.put("}", TipoToken.RIGHT_BRACE);
        tokensUnSimbolo.put(",", TipoToken.COMMA);
        tokensUnSimbolo.put(".", TipoToken.DOT);
        tokensUnSimbolo.put("-", TipoToken.MINUS);
        tokensUnSimbolo.put("+", TipoToken.PLUS);
        tokensUnSimbolo.put(";", TipoToken.SEMICOLON);
        tokensUnSimbolo.put("*", TipoToken.STAR);
        tokensUnSimbolo.put("/", TipoToken.SLASH);

    }
    static {
        tokensDosSimbolos = new HashMap<>();
        tokensDosSimbolos.put("!", TipoToken.BANG);
        tokensDosSimbolos.put(">", TipoToken.GREATER);
        tokensDosSimbolos.put("<", TipoToken.LESS);
        tokensDosSimbolos.put("=", TipoToken.EQUAL);
        tokensDosSimbolos.put("==", TipoToken.EQUAL_EQUAL);
        tokensDosSimbolos.put("!=", TipoToken.BANG_EQUAL);
        tokensDosSimbolos.put(">=", TipoToken.GREATER_EQUAL);
        tokensDosSimbolos.put("<=", TipoToken.LESS_EQUAL);
    }

    public Scanner(String source) {
        this.source = source + " ";
    }

    public List<Token> scan() {
        String lexema = "";
        int estado = 0;
        int columna = 0;
        int linea = 1;
        char c;

        for (int i = 0; i < source.length(); i++) {
            c = source.charAt(i);
            if (c == '\n') {
                linea++;
            }
            switch (estado) {
                case 0:
                    if (Character.isLetter(c)) {
                        estado = 9;
                        lexema += c;
                    } else if (Character.isDigit(c)) {
                        estado = 11;
                        lexema += c;
                    } else {
                        if (c == '=' || c == '!' || c == '<' || c == '>') {
                            estado = 12;
                            lexema += c;
                        } else if (c == '/') {
                            estado = 14;
                            lexema += c;
                        } else if (c == '[' || c == ']') {

                            lexema += c;
                            Token tu = new Token(TipoToken.ERROR_LEXICAL, lexema, columna, linea);
                            tokens.add(tu);

                            lexema = "";
                            estado = 0;
                        } else if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                            // Ignora los espacios, tabulaciones y saltos de línea
                            estado = 0; // Esta línea es redundante
                        } else if (c == '"') {
                            estado = 21;
                            lexema += c;
                        } else {
                            estado = 10;
                            lexema += c;
                        }
                    }

                    break;

                case 9:
                    if (Character.isLetter(c) || Character.isDigit(c)) {
                        estado = 9;
                        lexema += c;
                    } else {
                        // Vamos a crear el Token de identificador o palabra reservada
                        TipoToken tt = palabrasReservadas.get(lexema);

                        if (tt == null) {
                            Token t = new Token(TipoToken.IDENTIFIER, lexema, columna, linea);
                            tokens.add(t);
                        } else {
                            Token t = new Token(tt, lexema, columna, linea);
                            tokens.add(t);
                        }

                        estado = 0;
                        lexema = "";
                        i--;
                        columna++;
                    }
                    break;
                case 10: // estado para detectar los tokens que son simbolos
                    TipoToken tt = tokensUnSimbolo.get(lexema);
                    Token t = new Token(tt, lexema, columna, linea);
                    tokens.add(t);

                    lexema = "";
                    estado = 0;
                    i--;
                    columna++;
                    break;
                case 11:
                    if (Character.isDigit(c)) {
                        estado = 11;
                        lexema += c;
                    } else if (c == '.') {
                        estado = 18;
                        lexema += c;
                    } else if (c == 'E') {
                        estado = 19;
                        lexema += c;
                    } else {
                        Token ta = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema), columna, linea);
                        tokens.add(ta);

                        estado = 0;
                        lexema = "";
                        i--;
                        columna++;
                    }
                    break;
                case 12:
                    TipoToken td = tokensDosSimbolos.get(lexema);

                    if (c == '=' || c == '!' || c == '<' || c == '>') {
                        estado = 13;
                        lexema += c;
                    } else {
                        Token tn = new Token(td, lexema, columna, linea);
                        tokens.add(tn);

                        estado = 0;
                        lexema = "";
                        i--;
                        columna--;
                    }
                    break;
                case 13:
                    TipoToken te = tokensDosSimbolos.get(lexema);
                    Token tr = new Token(te, lexema, columna, linea);
                    tokens.add(tr);

                    estado = 0;
                    lexema = "";
                    i--;
                    columna--;
                    break;
                case 14:
                    if (c == '/') {
                        estado = 15;
                        lexema += c;
                    } else if (c == '*') {
                        estado = 16;
                        lexema += c;
                    } else {
                        TipoToken ti = tokensUnSimbolo.get(lexema);
                        Token tp = new Token(ti, lexema, columna, linea);
                        tokens.add(tp);

                        estado = 0;
                        lexema = "";
                        i--;
                        columna--;
                    }
                    break;
                case 15:
                    if (c == '\n') {
                        estado = 0;
                        lexema = "";
                        i--;
                    } else {
                        // Token ti = new Token(TipoToken.COMMENT, lexema);
                        // tokens.add(ti);
                        estado = 15;
                        lexema += c;
                    }
                    break;
                case 16:
                    if (c == '*') {
                        estado = 17;
                        lexema += c;
                    } else {
                        estado = 16;
                        lexema += c;
                    }
                    break;
                case 17:
                    if (c == '/') {
                        estado = 0;
                        lexema = "";
                    } else {
                        estado = 16;
                        lexema += c;
                    }
                    break;
                case 18:
                    if (Character.isDigit(c)) {
                        estado = 18;
                        lexema += c;
                    } else if (c == 'E') {
                        estado = 19;
                        lexema += c;
                    } else {
                        Token tu = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema), columna, linea);
                        tokens.add(tu);

                        estado = 0;
                        lexema = "";
                        i--;
                        columna--;
                    }
                    break;
                case 19:
                    if (c == '+' || c == '-' || Character.isDigit(c)) {
                        estado = 20;
                        lexema += c;
                    } else {

                        lexema += c;
                        Token tu = new Token(TipoToken.error, lexema, columna, linea);
                        tokens.add(tu);

                        estado = 0;
                        lexema = "";
                        i--;
                        columna--;
                    }
                    break;
                case 20:
                    if (Character.isDigit(c)) {
                        estado = 20;
                        lexema += c;
                    } else {
                        Token tu = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema), columna, linea);
                        tokens.add(tu);

                        estado = 0;
                        lexema = "";
                        i--;
                        columna--;
                    }
                    break;
                case 21:
                    if (c == '"') {
                        lexema += c;
                        estado = 22;
                    } else if (c == '\n') {
                        Token tu = new Token(TipoToken.error, lexema, columna, linea);
                        tokens.add(tu);

                        estado = 0;
                        lexema = "";
                        i--;
                        columna--;
                    } else {
                        lexema += c;
                        estado = 21;
                    }
                    break;
                case 22:
                    Token tu = new Token(TipoToken.STRING, lexema, columna, linea);
                    tokens.add(tu);

                    lexema = "";
                    estado = 0;
                    i--;
                    columna--;
                    break;
            }
        }

        return tokens;
    }

}
