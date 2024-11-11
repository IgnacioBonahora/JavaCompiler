public class Token {
    public enum TokenType {
        COMMENT, DATA_TYPE, KEYWORD, IDENTIFIER,
        FLOAT_LITERAL, INTEGER_LITERAL, OPERATOR,
        PUNCTUATION, EOF,NEWLINE,
        INVALID_IDENTIFIER
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
