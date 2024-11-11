import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int currentTokenIndex;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
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
        consumeToken(); // Consume el identificador

        if (getCurrentToken().getValue().equals("=")) {
            consumeToken(); // Consume el '='
            parseExpression();
        }

        expectSemicolon();
    }

    private void parseAssignment() {
        consumeToken(); // Consume el identificador
        expect("=");
        parseExpression();
        expectSemicolon();
    }

    private void parseExpression() {
        Token token = getCurrentToken();
        if (token == null) {
            throw new RuntimeException("Se esperaba una expresión pero se encontró EOF");
        }

        if (token.getType() == Token.TokenType.INTEGER_LITERAL ||
                token.getType() == Token.TokenType.FLOAT_LITERAL ||
                token.getType() == Token.TokenType.IDENTIFIER) {
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

    private void expectSemicolon() {
        Token token = getCurrentToken();
        if (token.getType() != Token.TokenType.PUNCTUATION || !token.getValue().equals(";")) {
            throw new RuntimeException("Se esperaba ';' pero se encontró: " + token);
        }
        consumeToken();
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
        Token identifier = getCurrentToken();
        if (identifier.getType() != Token.TokenType.IDENTIFIER) {
            throw new RuntimeException("Se esperaba un identificador después de 'write'");
        }
        consumeToken();
        expectSemicolon();
    }

    private void parseBreakStatement() {
        consumeToken(); // Consume 'break'
        expectSemicolon();
    }

    private Token getCurrentToken() {
        if (currentTokenIndex >= tokens.size()) {
            return null;
        }
        return tokens.get(currentTokenIndex);
    }

    private Token consumeToken() {
        Token current = getCurrentToken();
        currentTokenIndex++;
        return current;
    }

    private void parseCondition() {
        parseExpression();
        Token token = getCurrentToken();
        while (token != null && token.getType() == Token.TokenType.OPERATOR &&
                (token.getValue().equals("||") || token.getValue().equals("&&") ||
                        token.getValue().equals(">") || token.getValue().equals("<") ||
                        token.getValue().equals(">=") || token.getValue().equals("<=") ||
                        token.getValue().equals("=="))) {
            consumeToken(); // Consume el operador
            parseExpression();
            token = getCurrentToken();
        }
    }

    private void expect(String value) {
        Token token = getCurrentToken();
        if (token != null && token.getValue().equals(value)) {
            consumeToken();
        } else {
            throw new RuntimeException("Error de sintaxis: se esperaba el token '" + value + "' pero se encontró: "
                    + (token != null ? token : "EOF")
                    + " en posición " + currentTokenIndex);
        }
    }
}
