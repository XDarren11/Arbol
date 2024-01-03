import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    private static final Map<String, TipoToken> palabrasReservadas;

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

    private static final ArrayList<Character> caracteresNP = new ArrayList<>();

    static {
        caracteresNP.add('#');
        caracteresNP.add('$');
        caracteresNP.add('@');
        caracteresNP.add('[');
        caracteresNP.add(']');
        caracteresNP.add('?');
        caracteresNP.add('&');
        caracteresNP.add('\'');
        caracteresNP.add(':');
        caracteresNP.add((char) 64);
        caracteresNP.add((char) 37);
        caracteresNP.add((char) 94);
        caracteresNP.add((char) 124);
        for (int i = 126; i < 256; i++) {
            caracteresNP.add((char) i);
        }
    }

    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    public Scanner(String source) {
        this.source = source + " ";
    }

    public List<Token> scan() {
        String lexema = "";
        int estado = 0;
        int columna = 0;
        int linea = 1;
        char c;

        for (int i = 0; i < source.length(); i++, columna++) {
            c = source.charAt(i);

            if (c == 10) {
                linea++;
                columna = 0;
            }

            switch (estado) {
                case 0:

                    if (Character.isLetter(c)) {
                        estado = 9;
                        lexema += c;
                    }

                    else if (Character.isDigit(c)) {
                        estado = 15;
                        lexema += c;
                    } else if (caracteresNP.contains(c)) {
                        estado = 999;
                        lexema += c;
                    }

                    else if (c == '(')
                        tokens.add(new Token(TipoToken.LEFT_PAREN, "" + c, columna, linea));

                    else if (c == ')')
                        tokens.add(new Token(TipoToken.RIGHT_PAREN, "" + c, columna, linea));

                    else if (c == '{')
                        tokens.add(new Token((TipoToken.LEFT_BRACE), "" + c, columna, linea));

                    else if (c == '}')
                        tokens.add(new Token(TipoToken.RIGHT_BRACE, "" + c, columna, linea));

                    else if (c == ',')
                        tokens.add(new Token(TipoToken.COMMA, "" + c, columna, linea));

                    else if (c == '.')
                        tokens.add(new Token(TipoToken.DOT, "" + c, columna, linea));

                    else if (c == '-')
                        tokens.add(new Token(TipoToken.MINUS, "" + c, columna, linea));

                    else if (c == '+')
                        tokens.add(new Token(TipoToken.PLUS, "" + c, columna, linea));

                    else if (c == ';')
                        tokens.add(new Token(TipoToken.SEMICOLON, "" + c, columna, linea));

                    else if (c == '*')
                        tokens.add(new Token(TipoToken.STAR, "" + c, columna, linea));

                    else if (c == '>') {
                        lexema += c;
                        estado = 1;
                    }

                    else if (c == '<') {
                        lexema += c;
                        estado = 4;
                    }

                    else if (c == '=') {
                        lexema += c;
                        estado = 7;
                    } else if (c == '!') {
                        lexema += c;
                        estado = 10;
                    }

                    else if (c == '/') {
                        lexema += c;
                        estado = 26;
                    } else if (c == '"') {

                        estado = 24;
                    }

                    break;
                case 1:
                    if (c == '=') {
                        lexema += c;
                        estado = 2;
                    } else {
                        tokens.add(new Token(TipoToken.GREATER, lexema, columna, linea));
                        lexema = "";
                        estado = 0;
                        i--;
                        columna--;
                    }
                    break;
                case 2: {
                    tokens.add(new Token(TipoToken.GREATER_EQUAL, lexema, columna, linea));
                    lexema = "";
                    estado = 0;
                    i--;
                    columna--;
                    break;
                }

                case 4: {
                    if (c == '=') {
                        lexema += c;
                        estado = 5;
                    } else {
                        tokens.add(new Token(TipoToken.LESS, lexema, columna, linea));
                        lexema = "";
                        estado = 0;
                        i--;
                        columna--;
                    }
                    break;
                }

                case 5: {
                    tokens.add(new Token(TipoToken.LESS_EQUAL, lexema, columna, linea));
                    lexema = "";
                    estado = 0;
                    i--;
                    columna--;
                    break;
                }

                case 7: {
                    if (c == '=') {
                        lexema += c;
                        estado = 8;
                    } else {
                        tokens.add(new Token(TipoToken.EQUAL, lexema, columna, linea));
                        lexema = "";
                        estado = 0;
                        i--;
                        columna--;
                    }
                    break;
                }
                case 8: {
                    tokens.add(new Token(TipoToken.EQUAL_EQUAL, lexema, columna, linea));
                    lexema = "";
                    estado = 0;
                    i--;
                    columna--;
                    break;
                }
                case 9:
                    if (Character.isLetter(c) || Character.isDigit(c)) {
                        lexema += c;
                    } else {
                        // Vamos a crear el Token de IDENTIFICADOR o palabra reservada
                        TipoToken tt = palabrasReservadas.get(lexema);

                        // identificador
                        if (tt == null) {
                            Token t = new Token(TipoToken.IDENTIFIER, lexema, columna, linea);
                            tokens.add(t);
                        }
                        // palabra reservada
                        else {
                            Token t = new Token(tt, lexema, columna, linea);
                            tokens.add(t);
                        }
                        estado = 0;
                        lexema = "";
                        i--;
                        columna--;
                    }
                    break;

                case 10: {
                    if (c == '=') {
                        lexema += c;
                        estado = 11;
                    } else {
                        tokens.add(new Token(TipoToken.BANG, lexema, columna, linea));
                        lexema = "";
                        estado = 0;
                        i--;
                        columna--;
                    }
                    break;
                }

                case 11: {
                    tokens.add(new Token(TipoToken.BANG_EQUAL, lexema, columna, linea));
                    lexema = "";
                    estado = 0;
                    i--;
                    columna--;
                    break;
                }

                case 15:
                    if (Character.isDigit(c)) {
                        lexema += c;
                    } else if (c == '.') {
                        estado = 16;
                        lexema += c;

                    } else if (c == 'E') {
                        estado = 18;
                        lexema += c;
                    } else {
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema), columna, linea);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                        columna--;
                    }
                    break;

                case 16:
                    if (Character.isDigit(c)) {
                        estado = 17;
                        lexema += c;
                    } else {
                        Token t = new Token(TipoToken.ERROR_LEXICAL, lexema + " numerical error", columna, linea);
                        tokens.add(t);

                        lexema = "";
                        estado = 0;
                        i--;
                        columna--;
                    }
                    break;

                case 17:
                    if (Character.isDigit(c)) {
                        lexema += c;
                    } else if (c == 'E') {
                        estado = 18;
                        lexema += c;
                    } else {
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema), columna, linea);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                        columna--;
                    }
                    break;

                case 18:
                    if (Character.isDigit(c)) {
                        estado = 20;
                        lexema += c;
                    } else if (c == '+' || c == '-') {
                        estado = 19;
                        lexema += c;
                    } else {
                        Token t = new Token(TipoToken.ERROR_LEXICAL, lexema + " numerical error", columna, linea);
                        tokens.add(t);

                        lexema = "";
                        estado = 0;
                        i--;
                        columna--;
                    }
                    break;

                case 19:
                    if (Character.isDigit(c)) {
                        estado = 20;
                        lexema += c;
                    } else if (c == '.') {
                        estado = 999;
                        lexema += c;
                    } else {
                        Token t = new Token(TipoToken.ERROR_LEXICAL, lexema + " numerical error", columna, linea);
                        tokens.add(t);

                        lexema = "";
                        estado = 0;
                        i--;
                        columna--;
                    }
                    break;

                case 20:
                    if (Character.isDigit(c)) {
                        lexema += c;
                    } else if (c == '.') {
                        estado = 999;
                        lexema += c;
                    } else {
                        Token t = new Token(TipoToken.NUMBER, lexema, lexema, columna, linea);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                        columna--;
                    }
                    break;

                case 24: {
                    if (c == 10) {
                        tokens.add(new Token(TipoToken.ERROR_LEXICAL,
                                " Unexpected line break", columna, linea));
                        lexema = "";
                        estado = 0;
                        break;
                    } else if (c == '"') {

                        tokens.add(new Token(TipoToken.STRING, "\"" + lexema + "\"", lexema, columna, linea));

                        lexema = "";
                        estado = 0;
                        break;
                    }
                    lexema += c;
                    break;
                }

                case 26: {
                    if (c == '/') {
                        lexema += c;
                        estado = 30;
                    } else if (c == '*') {
                        lexema += c;
                        estado = 27;
                    } else {
                        tokens.add(new Token(TipoToken.SLASH, lexema, columna, linea));

                        lexema = "";
                        estado = 0;
                        i--;
                        columna--;
                    }
                    break;
                }

                case 27: {
                    if (c == '*')
                        estado = 28;
                    break;
                }
                case 28: {
                    if (c == '/') {
                        estado = 0;
                        lexema = "";
                    } else if (c != '*')
                        estado = 27;

                    break;
                }

                case 30: {
                    if (c == 10) {
                        lexema = "";
                        estado = 0;
                    }
                    break;
                }

                case 999: {
                    if (caracteresNP.contains(c)) {
                        lexema += c;
                    } else {
                        tokens.add(new Token(TipoToken.ERROR_LEXICAL, lexema + " illegal characters", columna, linea));

                        lexema = "";
                        estado = 0;
                        i--;
                        columna--;
                    }
                    break;
                }
            }
        }
        if (estado != 0 && estado != 27 && estado != 28) {
            tokens.add(new Token(TipoToken.ERROR_LEXICAL, lexema + " Unexpected line break", columna, linea));
        }
        tokens.add(new Token(TipoToken.EOF, "EOF"));

        return tokens;
    }
}