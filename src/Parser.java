import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int currentTokenIndex;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    private Token getCurrentToken() {
        return currentTokenIndex < tokens.size() ? tokens.get(currentTokenIndex) : null;
    }

    private Token consumeToken() {
        return tokens.get(currentTokenIndex++);
    }

    public void parse() {
        while (getCurrentToken() != null && getCurrentToken().getType() != Token.TokenType.EOF) {
            parseStatement();
        }
    }

    private void parseStatement() {
        Token token = getCurrentToken();
        // Si el token es un comentario, lo ignoramos y consumimos
        while (token != null && token.getType() == Token.TokenType.COMMENT) {
            consumeToken();
            token = getCurrentToken();
        }

        // Si llegamos al EOF, terminamos el análisis
        if (token == null || token.getType() == Token.TokenType.EOF) {
            return;
        }

        if (token.getType() == Token.TokenType.KEYWORD) {
            switch (token.getValue()) {
                case "if":
                    parseIfStatement();
                    break;
                case "while":
                    parseWhileStatement();
                    break;
                case "write":
                    parseWriteStatement();
                    break;
                default:
                    throw new RuntimeException("Unexpected keyword: " + token.getValue());
            }
        } else if (token.getType() == Token.TokenType.DATA_TYPE) {
            parseDeclaration();
        } else if (token.getType() == Token.TokenType.IDENTIFIER) {
            parseAssignment();
        } else {
            throw new RuntimeException("Unexpected token: " + token);
        }
    }

    private void parseDeclaration() {
        Token dataTypeToken = consumeToken(); // Consume 'long' o 'double'
        String dataType = dataTypeToken.getValue();

        if (getCurrentToken().getType() != Token.TokenType.IDENTIFIER) {
            throw new RuntimeException("Expected identifier after data type");
        }
        consumeToken(); // Consume identifier

        expect("="); // Consume '='
        Token valueToken = getCurrentToken(); // Obtener el token de la expresión de la asignación
        if ((dataType.equals("long") && valueToken.getType() != Token.TokenType.INTEGER_LITERAL) ||
                (dataType.equals("double") && valueToken.getType() != Token.TokenType.FLOAT_LITERAL)) {
            throw new RuntimeException("Error de tipo: no se puede asignar un valor " + valueToken.getType() +
                    " a una variable de tipo " + dataType);
        }

        parseExpression(); // Parse expresión de asignación
    }

    private void parseIfStatement() {
        consumeToken(); // Consume 'if'
        expect("(");
        parseLogicalExpression(); // Evaluar expresión lógica completa
        expect(")");
        expect("then");
        parseStatement();
    }

    private void parseWhileStatement() {
        consumeToken(); // Consume 'while'
        expect("(");
        parseLogicalExpression(); // Evaluar expresión lógica completa
        expect(")");
        expect("do");
        parseStatement();
    }

    private void parseWriteStatement() {
        consumeToken(); // Consume 'write'
        if (getCurrentToken().getType() != Token.TokenType.IDENTIFIER) {
            throw new RuntimeException("Expected identifier after 'write'");
        }
        consumeToken(); // Consume identifier
    }

    private void parseAssignment() {
        consumeToken(); // Consume identifier
        expect("=");
        parseExpression();
    }

    private void parseExpression() {
        parseRelationalExpression();
    }

    private void parseLogicalExpression() {
        parseRelationalExpression();
        while (getCurrentToken() != null &&
                (getCurrentToken().getValue().equals("&&") || getCurrentToken().getValue().equals("||"))) {
            consumeToken(); // Consume operador lógico '&&' o '||'
            parseRelationalExpression();
        }
    }

    private void parseRelationalExpression() {
        parseTerm();
        while (getCurrentToken() != null &&
                (getCurrentToken().getValue().equals(">") ||
                        getCurrentToken().getValue().equals("<") ||
                        getCurrentToken().getValue().equals(">=") ||
                        getCurrentToken().getValue().equals("<=") ||
                        getCurrentToken().getValue().equals("==") ||
                        getCurrentToken().getValue().equals("!="))) {
            consumeToken(); // Consume operador relacional
            parseTerm();
        }
    }

    private void parseTerm() {
        parseFactor();
        while (getCurrentToken() != null &&
                (getCurrentToken().getValue().equals("+") || getCurrentToken().getValue().equals("-"))) {
            consumeToken(); // Consume '+' o '-'
            parseFactor();
        }
    }

    private void parseFactor() {
        Token token = getCurrentToken();
        if (token.getType() == Token.TokenType.INTEGER_LITERAL || token.getType() == Token.TokenType.FLOAT_LITERAL) {
            consumeToken();
        } else if (token.getType() == Token.TokenType.IDENTIFIER) {
            consumeToken();
        } else {
            throw new RuntimeException("Unexpected token in expression: " + token);
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
