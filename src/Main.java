import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Código de prueba que queremos analizar
        String code = """
    if ( _hola > 0 && _hola == 0) then
        write _var1;
""";

        // Paso 1: Tokenizar el código
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();

        // Paso 2: Crear el parser con los tokens y analizar
        Parser parser = new Parser(tokens);
        System.out.println("Tokens generados por el Lexer:");
        for (Token token : tokens) {
            System.out.println(token);
        }

        System.out.println("\nInicio del análisis en el Parser:");
        parser.parse();  // Esto debería procesar y validar el código de prueba
        System.out.println("Análisis completo.");
    }
}

