import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Código de prueba que queremos analizar
        String code = """
    /*hola tincho
    */
    while (_numero > 0) do
        if (_numero == 50) then
            _numero = _numero + 1;
        else
            _numero = _numero - 1;
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


//todo hacer parser de declaracion de variables (long, double)
