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
        if (token.getType() == Token.TokenType.KEYWORD) {
            switch (token.getValue()) {
                case "if":
                    parseIfStatement();
                    break;
                case "while":
                    parseWriteStatement();
                    break;
                default:
                    throw new RuntimeException("Unexpected keyword: " + token.getValue());
            }
        } else if (token.getType() == Token.TokenType.IDENTIFIER) {
            parseAssignment();
        } else {
            throw new RuntimeException("Unexpected token: " + token);
        }
    }

    private void parseIfStatement( ) {

        consumeToken(); // Consume 'if'

        // Verificar y consumir el paréntesis de apertura
        expect("(");

        // Parse conditional expression
        parseExpression(); // e.g., `_var1 > 0`

        Token token = getCurrentToken();
        // Continuar si se encuentran operadores lógicos
        while (token != null && (token.getValue().equals("||") || token.getValue().equals("&&"))) {
            consumeToken(); // Consume '||' o '&&'
            parseExpression(); // Parse de la siguiente expresión condicional
            token = getCurrentToken();
        }

        // Verificar y consumir el paréntesis de cierre
        expect(")");

        // Verifies "then"
        expect("then");

        // Parses the statement in the "then" block
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
        Token identifier = consumeToken(); // Consume identifier
        expect("=");
        parseExpression();
    }

    private void parseExpression() {
        parseRelationalExpression();
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
            consumeToken(); // Consume comparison operator
            parseTerm();
        }
    }

    private void parseTerm() {
        parseFactor();
        while (getCurrentToken() != null &&
                (getCurrentToken().getValue().equals("+") || getCurrentToken().getValue().equals("-"))) {
            consumeToken(); // Consume '+' or '-'
            parseFactor();
        }
    }

    private void parseFactor() {
        Token token = getCurrentToken();
        if (token.getType() == Token.TokenType.INTEGER_LITERAL) {
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
