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

        // Debug: Imprimir los primeros caracteres del código fuente
        System.out.println("Primeros 100 caracteres del código fuente:");
        System.out.println(sourceCode.substring(0, Math.min(100, sourceCode.length())));
        System.out.println("Longitud total del código: " + sourceCode.length());
    }

    public List<Token> tokenize() {
        while (position < sourceCode.length()) {
            if (match("//.*")) { // Comentarios de una línea
                tokens.add(new Token(Token.TokenType.COMMENT, lastMatch));
            } else if (matchMultiLineComment()) { // Comentarios multilínea
                tokens.add(new Token(Token.TokenType.COMMENT, lastMatch));
            } else if (match("\\b(long|double)\\b")) { // Tipos de datos
                tokens.add(new Token(Token.TokenType.DATA_TYPE, lastMatch));
            } else if (match("\\b(if|then|else|while|do|break|write)\\b")) { // Palabras reservadas
                tokens.add(new Token(Token.TokenType.KEYWORD, lastMatch));
            } else if (match("_[a-zA-Z0-9]*")) { // Identificadores válidos
                tokens.add(new Token(Token.TokenType.IDENTIFIER, lastMatch));
            } else if (match("[a-zA-Z][a-zA-Z0-9]*")) { // Identificadores inválidos
                tokens.add(new Token(Token.TokenType.INVALID_IDENTIFIER, lastMatch));
            } else if (match("\\d+\\.\\d+")) { // Literales de punto flotante
                tokens.add(new Token(Token.TokenType.FLOAT_LITERAL, lastMatch));
            } else if (match("\\d+")) { // Literales enteros
                tokens.add(new Token(Token.TokenType.INTEGER_LITERAL, lastMatch));
            } else if (match("\\+|\\-|\\*|/|>=|<=|==|!=|<>|>|<|&&|\\|\\||=")) { // Operadores
                tokens.add(new Token(Token.TokenType.OPERATOR, lastMatch));
            } else if (match("[\\(\\);]")) { // Paréntesis y punto y coma
                tokens.add(new Token(Token.TokenType.PUNCTUATION, lastMatch));
            } else if (match("\\s+")) { // Ignorar espacios en blanco
                continue;
            } else if (match("\n")) { // Saltos de línea
                tokens.add(new Token(Token.TokenType.NEWLINE, "\\n"));
            } else {
                throw new RuntimeException("Error léxico: carácter no reconocido en posición " + position +
                        ": " + sourceCode.charAt(position));
            }
        }

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

    private boolean matchMultiLineComment() {
        if (!sourceCode.substring(position).startsWith("/*")) {
            return false;
        }

        StringBuilder comment = new StringBuilder();
        int currentPos = position;

        currentPos += 2;
        comment.append("/*");

        while (currentPos < sourceCode.length() - 1) {
            if (sourceCode.charAt(currentPos) == '*' &&
                    sourceCode.charAt(currentPos + 1) == '/') {
                comment.append("*/");
                lastMatch = comment.toString();
                position = currentPos + 2;
                return true;
            }
            comment.append(sourceCode.charAt(currentPos));
            currentPos++;
        }

        throw new RuntimeException("Error: Comentario multilínea no cerrado");
    }
}