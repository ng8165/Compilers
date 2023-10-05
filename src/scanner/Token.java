package scanner;

import java.util.Arrays;
import java.util.HashSet;

/**
 * The Token class represents a Token. It contains the token's contents and a Type.
 * @author Nelson Gou
 * @version 8/31/23
 */
public class Token
{
    /**
     * The KEYWORDS Set contains all Pascal keywords. This list was scraped off of
     * https://wiki.freepascal.org/Reserved_words. I added "WRITELN" to the list.
     */
    private static final HashSet<String> KEYWORDS = new HashSet<>(Arrays.asList("AND", "ARRAY",
            "ASM", "BEGIN", "BREAK", "CASE", "CONST", "CONSTRUCTOR", "CONTINUE", "DESTRUCTOR",
            "DIV", "DO", "DOWNTO", "ELSE", "END", "FALSE", "FILE", "FOR", "FUNCTION", "GOTO", "IF",
            "IMPLEMENTATION", "IN", "INLINE", "INTERFACE", "LABEL", "MOD", "NIL", "NOT", "OBJECT",
            "OF", "ON", "OPERATOR", "OR", "PACKED", "PROCEDURE", "PROGRAM", "RECORD", "REPEAT",
            "SET", "SHL", "SHR", "STRING", "THEN", "TO", "TRUE", "TYPE", "UNIT", "UNTIL", "USES",
            "VAR", "WHILE", "WITH", "WRITELN", "XOR"));

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
        EOF,
        /**
         * The KEYWORD Type denotes a keyword in Pascal. See the KEYWORDS HashSet above.
         */
        KEYWORD,
        /**
         * The STRING type denotes a string in Pascal.
         */
        STRING,
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
        this.type = KEYWORDS.contains(content) ? Type.KEYWORD : type;
    }

    /**
     * Getter for the content of the Token.
     * @return the String content of the Token
     */
    public String getContent()
    {
        return content;
    }

    /**
     * Getter for the Type of the Token.
     * @return the Type of the Token
     */
    public Type getType()
    {
        return type;
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

    /**
     * Overrides the equals function to compare two Tokens. If the object to compare to is
     * not a Token, returns false. If it is a Token, compares both type and content.
     * @param obj the object to compare to
     * @return true if equal; false if not equal or not a Token
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Token other)
            return type == other.type && content.equals(other.content);
        else if (obj instanceof String other)
            return content.equals(other);
        return false;
    }
}
