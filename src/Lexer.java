import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private String sourceCode;
    private List<Token> tokens;
    private int position;
    private String lastMatch;

    public Lexer(String sourceCode) {
        this.sourceCode = sourceCode;
        this.tokens = new ArrayList<>();
        this.position = 0;
    }

    public List<Token> tokenize() {
        while (position < sourceCode.length()) {
            if (match("//.*")) { // Comentarios de una línea
                tokens.add(new Token(Token.TokenType.COMMENT, lastMatch));
            } else if (match("/\\*([\\s\\S]*?)\\*/")) { // Comentarios de varias líneas
                tokens.add(new Token(Token.TokenType.COMMENT, lastMatch.replaceAll("\n", " ")));
            } else if (match("\\b(long|double|if|then|else|while|break|read|write)\\b")) { // Palabras reservadas
                tokens.add(new Token(Token.TokenType.KEYWORD, lastMatch));
            } else if (match("_\\w+")) { // Identificadores
                tokens.add(new Token(Token.TokenType.IDENTIFIER, lastMatch));
            } else if (match("\\d+\\.\\d+")) { // Literales de punto flotante
                tokens.add(new Token(Token.TokenType.FLOAT_LITERAL, lastMatch));
            } else if (match("\\d+")) { // Literales enteros
                tokens.add(new Token(Token.TokenType.INTEGER_LITERAL, lastMatch));
            } else if (match("\\+|\\-|\\*|/|>=|<=|==|!=|<>|>|<|&&|\\|\\||=")) { // Operadores
                tokens.add(new Token(Token.TokenType.OPERATOR, lastMatch));
            } else if (match("\\(") || match("\\)")) { // Paréntesis
                tokens.add(new Token(Token.TokenType.PUNCTUATION, lastMatch));
            } else if (match("\\s+")) { // Ignorar espacios en blanco
                continue;
            } else {
                position++; // Avanza si no se encuentra un token
            }
        }

        // Añade el token de fin de archivo
        tokens.add(new Token(Token.TokenType.EOF, "EOF"));
        return tokens;
    }

    private boolean match(String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sourceCode.substring(position));
        if (matcher.lookingAt()) {
            lastMatch = matcher.group();
            position += matcher.end();
            return true;
        }
        return false;
    }
}
