import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JackTokenizerTest {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Uso: java JackTokenizerTest <caminho-para-arquivo.jack>");
            return;
        }

        Path inputPath = Path.of(args[0]);
        byte[] inputBytes = Files.readAllBytes(inputPath);

        Scanner scanner = new Scanner(inputBytes);
        
        String outputFileName = inputPath.getFileName().toString().replace(".jack", "T.xml");
        Path outputPath = inputPath.getParent().resolve(outputFileName);

        try (FileWriter writer = new FileWriter(outputPath.toFile())) {
            writer.write("<tokens>\n");

            Token token = scanner.nextToken();
            while (token.type != TokenType.EOF) {
                String tag = getXmlTag(token.type);
                
                // ➔ AQUI entra a conversão dos símbolos especiais XML!
                String value = escapeXml(token.lexeme);

                writer.write("  <" + tag + "> " + value + " </" + tag + ">\n");

                token = scanner.nextToken();
            }

            writer.write("</tokens>\n");
        }

        System.out.println("Arquivo XML gerado com sucesso em: " + outputPath);
    }

    private static String getXmlTag(TokenType type) {
        return switch (type) {
            case CLASS, CONSTRUCTOR, FUNCTION, METHOD, FIELD, STATIC, VAR,
                 INT, CHAR, BOOLEAN, VOID, TRUE, FALSE, NULL, THIS, LET,
                 DO, IF, ELSE, WHILE, RETURN -> "keyword";
            
            case PLUS, MINUS, STAR, SLASH, EQ, SEMICOLON, DOT, AND, OR,
                 NOT, GT, LT, LPAREN, RPAREN, LBRACE, RBRACE, LBRACKET,
                 RBRACKET, COMMA -> "symbol";
                 
            case NUMBER -> "integerConstant";
            case STRING -> "stringConstant";
            case IDENT -> "identifier";
            default -> throw new IllegalArgumentException("Tipo não suportado no XML: " + type);
        };
    }

    private static String escapeXml(String lexeme) {
        return switch (lexeme) {
            case "<" -> "&lt;";
            case ">" -> "&gt;";
            case "&" -> "&amp;";
            case "\"" -> "&quot;";
            default -> lexeme;
        };
    }
}