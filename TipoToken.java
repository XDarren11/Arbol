package interpreter;

public enum TipoToken {
    // Tokens de un sólo caracter
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // Tokens de uno o dos caracteres
    BANG, EQUAL, GREATER, LESS,
    BANG_EQUAL, EQUAL_EQUAL,
    GREATER_EQUAL, LESS_EQUAL,

    // Literales
    IDENTIFIER, STRING, NUMBER, COMMENT,

    // Palabras clave
    AND, ELSE, FALSE, FUN, FOR, IF, NULL, OR,
    PRINT, RETURN, TRUE, VAR, WHILE, ERROR_LEXICAL,

    CLASS,
    error, EOF
}
