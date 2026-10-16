public class Token {
    
    final TokenType type;
    final String lexeme;
    final int line;

    public Token (TokenType type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = 1; 
    }

    @Override 
    public String toString() {
        return "<"+ type +">" + lexeme + "</"+ type + ">";
    }
}