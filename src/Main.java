import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: Debe proporcionar el nombre del archivo a analizar");
            System.out.println("Uso: java -jar compilador.jar archivo.txt");
            System.exit(1);
        }

        String fileName = args[0];
        try {
            String sourceCode = new String(Files.readAllBytes(Paths.get(fileName)));

            System.out.println("Analizando archivo: " + fileName);
            System.out.println("Contenido del archivo:");
            System.out.println("--------------------");
            System.out.println(sourceCode);
            System.out.println("--------------------\n");

            // Proceso de análisis
            Lexer lexer = new Lexer(sourceCode);
            List<Token> tokens = lexer.tokenize();

            System.out.println("Tokens generados por el Lexer:");
            for (Token token : tokens) {
                System.out.println(token);
            }

            Parser parser = new Parser(tokens);
            System.out.println("\nInicio del análisis en el Parser:");
            parser.parse();
            System.out.println("Análisis completo.");

            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
            semanticAnalyzer.analyze(tokens);
            System.out.println("Análisis semántico completado.");

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + fileName);
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        } catch (RuntimeException e) {
            System.err.println("Error durante el análisis:");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
