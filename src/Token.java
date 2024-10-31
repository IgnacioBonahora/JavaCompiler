public class Token {
    public enum TokenType {
        IDENTIFIER, KEYWORD, OPERATOR, COMMENT, INTEGER_LITERAL, FLOAT_LITERAL,
        PUNCTUATION, WHITESPACE, EOF // EOF: Fin de archivo
    }

    private TokenType type;
    private String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + type + ", value='" + value + '\'' + '}';
    }
}

