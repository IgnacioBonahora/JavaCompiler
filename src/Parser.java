import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Parser {
    private List<Token> tokens;
    private int currentTokenIndex;
    private Map<String, Token> symbolTable; // Tabla de símbolos para almacenar variables

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
        this.symbolTable = new HashMap<>(); // Inicializa la tabla de símbolos
    }

    public void parse() {
        while (currentTokenIndex < tokens.size() && getCurrentToken().getType() != Token.TokenType.EOF) {
            parseStatement();
        }
    }

    private void parseStatement() {
        Token token = getCurrentToken();

        // Ignorar comentarios
        if (token.getType() == Token.TokenType.COMMENT) {
            consumeToken();
            return;
        }

        // Declaración de variables
        if (token.getType() == Token.TokenType.DATA_TYPE) {
            parseDeclaration();
            return;
        }

        // If statement
        if (token.getValue().equals("if")) {
            parseIfStatement();
            return;
        }

        // While statement
        if (token.getValue().equals("while")) {
            parseWhileStatement();
            return;
        }

        // Write statement
        if (token.getValue().equals("write")) {
            parseWriteStatement();
            return;
        }

        // Break statement
        if (token.getValue().equals("break")) {
            parseBreakStatement();
            return;
        }

        // Asignación
        if (token.getType() == Token.TokenType.IDENTIFIER) {
            parseAssignment();
            return;
        }

        throw new RuntimeException("Token inesperado: " + token);
    }

    private void parseDeclaration() {
        consumeToken(); // Consume el tipo de dato

        Token identifierToken = getCurrentToken();
        if (identifierToken.getType() != Token.TokenType.IDENTIFIER &&
                identifierToken.getType() != Token.TokenType.INVALID_IDENTIFIER) {
            throw new RuntimeException("Se esperaba un identificador");
        }

        // Añadir la variable a la tabla de símbolos con un valor inicial nulo
        symbolTable.put(identifierToken.getValue(), identifierToken);
        consumeToken(); // Consume el identificador

        if (getCurrentToken().getValue().equals("=")) {
            consumeToken(); // Consume el '='
            parseExpression();
        }

        expectEndOfStatement();
    }

    private void parseAssignment() {
        Token identifierToken = getCurrentToken();

        // Verificar si la variable ha sido declarada
        if (!symbolTable.containsKey(identifierToken.getValue())) {
            throw new RuntimeException("La variable '" + identifierToken.getValue() + "' no ha sido declarada. Asegúrate de declararla antes de usarla.");
        }

        consumeToken(); // Consume el identificador
        expect("=");

        // Actualizar el valor de la variable en la tabla de símbolos después de la expresión
        parseExpression();
        symbolTable.put(identifierToken.getValue(), identifierToken); // Actualiza el valor en la tabla

        expectEndOfStatement();
    }

    private void parseExpression() {
        Token token = getCurrentToken();
        if (token == null) {
            throw new RuntimeException("Se esperaba una expresión pero se encontró EOF");
        }

        if (token.getType() == Token.TokenType.INTEGER_LITERAL ||
                token.getType() == Token.TokenType.FLOAT_LITERAL ||
                token.getType() == Token.TokenType.IDENTIFIER) {

            // Verificar si el identificador usado en la expresión ha sido declarado
            if (token.getType() == Token.TokenType.IDENTIFIER && !symbolTable.containsKey(token.getValue())) {
                throw new RuntimeException("La variable '" + token.getValue() + "' no ha sido declarada");
            }

            consumeToken();

            token = getCurrentToken();
            if (token != null && token.getType() == Token.TokenType.OPERATOR &&
                    (token.getValue().equals("+") || token.getValue().equals("-") ||
                            token.getValue().equals("*") || token.getValue().equals("/"))) {
                consumeToken(); // Consume el operador
                parseExpression(); // Recursivamente parsea el resto de la expresión
            }
        } else {
            throw new RuntimeException("Se esperaba un valor o identificador pero se encontró: " + token);
        }
    }

    private void expectEndOfStatement() {
        Token token = getCurrentToken();
        if (token == null || token.getType() == Token.TokenType.EOF) {
            return; // Fin del archivo se acepta como el fin de una declaración
        }

        // Verificar si el token actual es un salto de línea o un punto y coma
        if (token.getType() == Token.TokenType.PUNCTUATION && token.getValue().equals(";")) {
            consumeToken();
        } else if (token.getType() == Token.TokenType.NEWLINE) {
            consumeToken();
        } else {
            throw new RuntimeException("Se esperaba un delimitador de declaración (punto y coma o salto de línea) pero se encontró: " + token);
        }
    }

    private void parseIfStatement() {
        consumeToken(); // Consume 'if'
        expect("(");
        parseCondition();
        expect(")");
        expect("then");
        parseStatement();

        if (getCurrentToken().getValue().equals("else")) {
            consumeToken(); // Consume 'else'
            parseStatement();
        }
    }

    private void parseWhileStatement() {
        consumeToken(); // Consume 'while'
        expect("(");
        parseCondition();
        expect(")");
        expect("do");
        parseStatement();
    }

    private void parseWriteStatement() {
        consumeToken(); // Consume 'write'
        parseExpression();
        expectEndOfStatement();
    }

    private void parseBreakStatement() {
        consumeToken(); // Consume 'break'
        expectEndOfStatement();
    }
    private void parseCondition() {
        parseExpression(); // Parsea la primera expresión

        Token token = getCurrentToken();

        // Manejar operadores de comparación y parsear otra expresión
        if (token != null && token.getType() == Token.TokenType.OPERATOR &&
                (token.getValue().equals("==") || token.getValue().equals("!=") ||
                        token.getValue().equals("<") || token.getValue().equals(">") ||
                        token.getValue().equals("<=") || token.getValue().equals(">="))) {

            consumeToken(); // Consume el operador de comparación
            parseExpression(); // Parsea el segundo término de la comparación
            token = getCurrentToken();
        }

        // Manejar operadores lógicos (&&, ||) y parsear condiciones adicionales
        while (token != null && token.getType() == Token.TokenType.OPERATOR &&
                (token.getValue().equals("&&") || token.getValue().equals("||"))) {

            consumeToken(); // Consume el operador lógico
            parseCondition(); // Llamada recursiva para parsear la siguiente condición
            token = getCurrentToken();
        }

        // Validar que termine en un paréntesis de cierre o en otro operador lógico
        if (token != null && !token.getValue().equals(")") &&
                !token.getValue().equals("&&") && !token.getValue().equals("||")) {
            throw new RuntimeException("Se esperaba un operador de condición o ')' pero se encontró: " + token);
        }
    }



    private void expect(String expectedValue) {
        Token token = getCurrentToken();
        if (token == null || !token.getValue().equals(expectedValue)) {
            throw new RuntimeException("Se esperaba '" + expectedValue + "' pero se encontró: " + token);
        }
        consumeToken();
    }

    private Token getCurrentToken() {
        return currentTokenIndex < tokens.size() ? tokens.get(currentTokenIndex) : null;
    }

    private void consumeToken() {
        currentTokenIndex++;
    }
}
