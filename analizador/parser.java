package analizador;
// import java.util.ArrayList;

// import java.util.List;

// public class parser {

// private int i = 0;
// private boolean hayErrores = false;
// private Token preanalisis;
// private final List<Token> tokens;

// public Parser(List<Token> tokens) {
// this.tokens = tokens;
// preanalisis = this.tokens.get(i);
// }

// public boolean parse() {
// if (preanalisis.tipo == TipoToken.EOF && !hayErrores) {
// System.out.println("Correcto");
// return true;
// } else {
// System.out.println("Se encontraron errores");
// }
// return false;
// }

// private Statement classDeclaration() {
// if (preanalisis.tipo == TipoToken.CLASS) {
// match(TipoToken.CLASS);
// match(TipoToken.IDENTIFIER);
// Token name = previous();
// ExprVariable superClass = classInher();
// match(TipoToken.LEFT_BRACE);
// List<StmtFunction> methods = new ArrayList<>();
// functions(methods);
// match(TipoToken.RIGHT_BRACE);
// }
// }
// }
public interface parser {
    boolean parse();
}