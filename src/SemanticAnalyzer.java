import java.util.*;

public class SemanticAnalyzer {
    private Map<String, VariableInfo> symbolTable;
    private List<String> errors;

    public SemanticAnalyzer() {
        this.symbolTable = new HashMap<>();
        this.errors = new ArrayList<>();
    }

    public void analyze(List<Token> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (token.getType() == Token.TokenType.INVALID_IDENTIFIER) {
                throw new RuntimeException("Error semántico: identificador inválido '" +
                        token.getValue() + "'. Los identificadores deben comenzar con _");
            }

            try {
                // Verificar identificadores
                if (token.getType() == Token.TokenType.IDENTIFIER) {
                    validateIdentifier(token.getValue());
                }

                // Verificar declaraciones y asignaciones
                if (token.getType() == Token.TokenType.KEYWORD &&
                        (token.getValue().equals("long") || token.getValue().equals("double"))) {
                    if (i + 1 < tokens.size() && tokens.get(i + 1).getType() == Token.TokenType.IDENTIFIER) {
                        handleVariableDeclaration(tokens.get(i).getValue(), tokens.get(i + 1).getValue());
                    }
                }

                // Verificar asignaciones
                if (token.getType() == Token.TokenType.IDENTIFIER &&
                        i + 1 < tokens.size() && tokens.get(i + 1).getValue().equals("=")) {
                    validateAssignment(token.getValue(), tokens.get(i + 2));
                }
            } catch (SemanticException e) {
                errors.add(e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            StringBuilder errorReport = new StringBuilder("Errores semánticos encontrados:\n");
            for (String error : errors) {
                errorReport.append("- ").append(error).append("\n");
            }
            throw new SemanticException(errorReport.toString());
        }
    }

    private void validateIdentifier(String identifier) {
        if (!identifier.startsWith("_")) {
            throw new SemanticException("Error: El identificador '" + identifier +
                    "' debe comenzar con '_'");
        }

        if (identifier.length() < 2) {
            throw new SemanticException("Error: El identificador '" + identifier +
                    "' debe tener al menos una letra después del '_'");
        }

        for (int i = 1; i < identifier.length(); i++) {
            if (!Character.isLetter(identifier.charAt(i))) {
                throw new SemanticException("Error: El identificador '" + identifier +
                        "' solo debe contener letras después del '_'");
            }
        }
    }

    private void handleVariableDeclaration(String type, String identifier) {
        if (symbolTable.containsKey(identifier)) {
            throw new SemanticException("Error: Variable '" + identifier +
                    "' ya ha sido declarada");
        }
        symbolTable.put(identifier, new VariableInfo(type));
    }

    private void validateAssignment(String identifier, Token valueToken) {
        if (!symbolTable.containsKey(identifier)) {
            throw new SemanticException("Error: Variable '" + identifier +
                    "' no ha sido declarada");
        }

        VariableInfo varInfo = symbolTable.get(identifier);

        // Validar tipos
        if (varInfo.getType().equals("long")) {
            if (valueToken.getType() != Token.TokenType.INTEGER_LITERAL) {
                throw new SemanticException("Error: Se esperaba un valor entero para la variable '" +
                        identifier + "'");
            }
        } else if (varInfo.getType().equals("double")) {
            if (valueToken.getType() != Token.TokenType.FLOAT_LITERAL &&
                    valueToken.getType() != Token.TokenType.INTEGER_LITERAL) {
                throw new SemanticException("Error: Se esperaba un valor numérico para la variable '" +
                        identifier + "'");
            }
        }
    }

    private static class VariableInfo {
        private String type;

        public VariableInfo(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}

class SemanticException extends RuntimeException {
    public SemanticException(String message) {
        super(message);
    }
}
