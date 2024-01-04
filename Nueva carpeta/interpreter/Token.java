package interpreter;

public class Token {

    public final TipoToken tipo;
    public final String lexema;
    public final Object literal;
    public final Integer columnaE;
    public final Integer linea;

    public Token(TipoToken tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;
        this.columnaE = null;
        this.linea = null;
    }

    public Token(TipoToken tipo, String lexema, Object literal) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        this.columnaE = null;
        this.linea = null;
    }

    public Token(TipoToken tipo, String lexema, int columnaE, int linea) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;
        this.columnaE = columnaE;
        this.linea = linea;
    }

    public Token(TipoToken tipo, String lexema, Object literal, int columnaE, int linea) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        this.columnaE = columnaE;
        this.linea = linea;
    }

    public String toString() {
        return "<" + tipo + " " + lexema + " " + literal + " linea:" + linea + " column:" + columnaE + ">";
    }
}