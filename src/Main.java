import java.util.List;
public class Main {
    public static void main(String[] args) {
        String code ="_var1 = 5; _tempVar = _var1 + 10;";;

        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
