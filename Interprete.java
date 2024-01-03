import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Interprete {

    static boolean existenErrores = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("El uso correcto: interprete [archivo.txt]\n");

            for (String a : args) {
                System.out.println("\n\n\n//////////////////////////\n prueba:" + a + "\n");
                ejecutarArchivo(a);

            }
            System.exit(64);
        } else if (args.length == 1) {
            ejecutarArchivo(args[0]);

        } else {
            System.out.println("prompt");
            ejecutarPrompt();
        }
    }

    private static void ejecutarArchivo(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        ejecutar(new String(bytes, Charset.defaultCharset()));

        // Se indica que existe un error
        if (existenErrores)
            System.exit(65);
    }

    private static void ejecutarPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print(">>> ");
            String linea = reader.readLine();
            if (linea == null)
                break; // Presionar Ctrl + D
            ejecutar(linea);
            existenErrores = false;
        }
    }

    private static void ejecutar(String source) {
        try {
            Scanner scanner = new Scanner(source);
            List<Token> tokens = scanner.scan();

            int i = 0;

            for (Token token : tokens) {
                if (token.tipo == TipoToken.ERROR_LEXICAL) {
                    i++;
                    reportar(token.linea, token.columnaE, token.lexema);

                }
            }

            if (i != 0) {
                System.exit(0);
            }

            parser parser = new ASDR(tokens);
            parser.parse();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static void reportar(int linea, Integer posicion, Object mensaje) {
        System.err.println(
                "[linea " + linea + " posicion " + posicion + "] : " + mensaje);
        existenErrores = true;
    }

}