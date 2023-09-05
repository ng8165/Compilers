package scanner;

/**
 * The Token class represents a Token. It contains the token's contents and a Type.
 * @author Nelson Gou
 * @version 8/31/23
 */
public class Token
{
    public enum Type
    {
        /**
         * The NUMBER Type denotes a Token that is a sequence of digits.
         */
        NUMBER,
        /**
         * The IDENTIFIER Type denotes a Token that starts as a letter
         * and contains any sequence of digits or letters after.
         */
        IDENTIFIER,
        /**
         * The OPERAND Type denotes a Token that is an operand (equals, plus, minus, asterisk,
         * forward slash, percent, colon, angle brackets, caret, ampersand, and pipe);
         */
        OPERAND,
        /**
         * The SEPARATOR Type denotes a Token that is a separator (parentheses, square brackets,
         * braces, semicolon, comma, single quote, and double quote).
         */
        SEPARATOR,
        /**
         * The EOF Type denotes a Token indicating that the end-of-file is reached.
         */
        EOF
    }

    private final String content;
    private final Type type;

    /**
     * The Token constructor takes the Token content and its Type.
     * @param content the contents of the Token
     * @param type the Type of the Token
     */
    public Token(String content, Type type)
    {
        this.content = content;
        this.type = type;
    }

    /**
     * Overrides the toString function to include both the Token content and the Token Type.
     * @return the content of the Token and its Type
     */
    @Override
    public String toString()
    {
        return content + " (" + type + ")";
    }
}
