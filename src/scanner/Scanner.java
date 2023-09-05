package scanner;
import java.io.*;

/**
 * The Scanner is the first lab for the Compilers course. It will parse the input string, determine
 * lexemes, and produce a string of tokens.
 * @author Nelson Gou
 * @version 8/31/23
 */
public class Scanner
{
    private final BufferedReader in;
    private char currentChar;
    private boolean eof;

    /**
     * This constructor uses an InputStream object for input to the reader.
     * It sets the end-of-file flag to false and sets the first character in currentChar.
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }

    /**
     * This constructor uses a String for input to the reader.
     * It sets the end-of-file flag to false and sets the first character in currentChar.
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * The getNextChar function loads the next character from the stream. If the stream is
     * at the end of file, the eof instance variable is set to true. Otherwise, the currentChar
     * variable is set to the next character.
     * @postcondition replaces currentChar; if no more characters, sets eof
     */
    private void getNextChar()
    {
        try
        {
            int read = in.read();

            if (read == -1 || read == '.')
                eof = true;
            else
                currentChar = (char) read;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * The eat function verifies that currentChar is equal to the expected character.
     * If they are equal, the input stream is advanced by calling the getNextChar method.
     * If they are not equal, a ScanErrorException is thrown.
     * @param expected a char which has the expected value of currentChar
     * @postcondition calls the getNextChar method (replaces currentChar;
     *                if no more characters, sets eof)
     * @throws ScanErrorException when the expected character and currentChar are different
     */
    private void eat(char expected) throws ScanErrorException
    {
        if (expected == currentChar)
        {
            getNextChar();
        }
        else
        {
            String str = "Illegal character - expected " + expected + " and found " + currentChar;
            throw new ScanErrorException(str);
        }
    }

    /**
     * Determines if the input stream is at the end.
     * @return true if there are more characters in the stream (not end-of-file);
     *         false if there are no more characters in the stream (end-of-file)
     */
    public boolean hasNext()
    {
        return !eof;
    }

    /**
     * Determines if the character is a digit.
     * @param input the input character
     * @return true if input is a digit; false if not
     */
    public static boolean isDigit(char input)
    {
        return input >= '0' && input <= '9';
    }

    /**
     * Determines if the character is a letter (either lowercase or uppercase).
     * @param input the input character to be tested
     * @return true if the input is a letter; false if not
     */
    public static boolean isLetter(char input)
    {
        return (input >= 'A' && input <= 'Z') || (input >= 'a' && input <= 'z');
    }

    /**
     * Determines if the character is whitespace (space, carriage return, new line, tab).
     * @param input the input character to be tested
     * @return true if the input is whitespace; false if not
     */
    public static boolean isWhitespace(char input)
    {
        return " \r\n\t".contains("" + input);
    }

    /**
     * Determines if the character is an operand (equals, plus, minus, asterisk, forward slash,
     * percent, colon, angle brackets, ampersand, pipe, exclamation mark, tilde).
     * @param input the input character to be tested
     * @return true if the input is an operand; false if not
     */
    private static boolean isOperand(char input)
    {
        return "=+-*/%:<>&|!~".contains("" + input);
    }

    /**
     * Determines if the character is a separator (parentheses, square brackets,
     * braces, semicolon, comma).
     * @param input the input character to be tested
     * @return true if the input is a separator; false if not
     */
    private static boolean isSeparator(char input)
    {
        return "()[]{};,".contains("" + input);
    }

    /**
     * Scans a stream of digits into a number as a Token.
     * @return the number as a Token
     * @precondition currentChar is a digit when the method is called
     * @postcondition from the eat function, replaces currentChar; if no more characters, sets eof
     * @throws ScanErrorException from the eat function when the expected character
     *                            and currentChar are different
     */
    private Token scanNumber() throws ScanErrorException
    {
        StringBuilder number = new StringBuilder();

        do
        {
            number.append(currentChar);
            eat(currentChar);
        } while (isDigit(currentChar) && hasNext());

        return new Token(number.toString(), Token.Type.NUMBER);
    }

    /**
     * Scans a stream of letters/digits into an identifier as a Token.
     * @precondition currentChar is a letter when the method is called
     * @return the identifier as a Token
     * @precondition the currentChar must be a letter
     * @postcondition from the eat function, replaces currentChar; if no more characters, sets eof
     * @throws ScanErrorException from the eat function when the expected character
     *                            and currentChar are different
     */
    private Token scanIdentifier() throws ScanErrorException
    {
        StringBuilder identifier = new StringBuilder();

        do
        {
            identifier.append(currentChar);
            eat(currentChar);
        } while ((isDigit(currentChar) || isLetter(currentChar)) && hasNext());

        return new Token(identifier.toString(), Token.Type.IDENTIFIER);
    }

    /**
     * Scans currentChar as an operand. The method also handles comments,
     * because both comment types (single-line and multi-line) start with a forward slash.
     * Also handles operand that are two characters long (:=, <=, >=, <>, <<, >>).
     * @return the operand as a Token; if a comment, returns nextToken after the end of the comment
     * @precondition currentChar must be an operand (see isOperand for more details)
     * @postcondition from the eat function, replaces currentChar; if no more characters, sets eof
     * @throws ScanErrorException from the eat function when the expected character
     *                            and currentChar are different
     */
    private Token scanOperand() throws ScanErrorException
    {
        String operand = "" + currentChar;
        eat(currentChar);

        // handle comments (single-line and multi-line)
        if (operand.equals("/") && currentChar == '/')
            return scanSingleComment();
        else if (operand.equals("/") && currentChar == '*')
            return scanMultiComment();

        // handle operands with two characters
        if ((":<>".contains(operand) && currentChar == '=') || // :=, <=, >=
                (operand.equals("<") && (currentChar == '<' || currentChar == '>')) || // <>, <<
                (operand.equals(">") && currentChar == '>')) // >>
        {
            operand += currentChar;
            eat(currentChar);
        }

        return new Token(operand, Token.Type.OPERAND);
    }

    /**
     * Scans currentChar as a separator.
     * @return the separator as a Token
     * @precondition currentChar must be a separator (see isSeparator for details)
     * @postcondition from the eat function, replaces currentChar; if no more characters, sets eof
     * @throws ScanErrorException from the eat function when the expected character
     *                            and currentChar are different
     */
    private Token scanSeparator() throws ScanErrorException
    {
        Token token =  new Token("" + currentChar, Token.Type.SEPARATOR);
        eat(currentChar);
        return token;
    }

    /**
     * The scanSingleComment method eats characters until it reaches a new line.
     * @return returns the next token after the comment ends
     * @precondition // must be present before this method is called
     * @postcondition from the eat function, replaces currentChar; if no more characters, sets eof
     * @throws ScanErrorException from the eat function when the expected character
     *                            and currentChar are different
     */
    private Token scanSingleComment() throws ScanErrorException
    {
        do
            eat(currentChar);
        while (currentChar != '\n' && hasNext());

        return nextToken();
    }

    /**
     * The scanMultiComment method eats characters until it finds the end multiline symbol.
     * (I can't put it here because the documentation will break if I do.)
     * @return returns the text token after the comment ends
     * @precondition /* must be present before this method is called
     * @postcondition from the eat function, replaces currentChar; if no more characters, sets eof
     * @throws ScanErrorException from the eat function when the expected character
     *                            and currentChar are different
     */
    private Token scanMultiComment() throws ScanErrorException
    {
        eat(currentChar); // eat the * in /*
        char lastChar;

        do
        {
            lastChar = currentChar;
            eat(currentChar);
        } while (!(lastChar == '*' && currentChar == '/') && hasNext());

        eat(currentChar); // eat the last / in */

        return nextToken();
    }

    /**
     * The nextToken method skips all whitespace and returns the next lexeme as a Token.
     * @return the next token in the stream; "END" if at end-of-file
     * @postcondition parses the next token and eats characters in the stream
     * @throws ScanErrorException from the eat function when the expected character
     *                            and currentChar are different
     */
    public Token nextToken() throws ScanErrorException
    {
        while (isWhitespace(currentChar) && hasNext())
            eat(currentChar);

        if (!hasNext())
            return new Token("END", Token.Type.EOF);

        if (isDigit(currentChar))
            return scanNumber();
        else if (isLetter(currentChar))
            return scanIdentifier();
        else if (isOperand(currentChar))
            return scanOperand();
        else if (isSeparator(currentChar))
            return scanSeparator();
        else
        {
            String error = "Unrecognized character " + currentChar;
            eat(currentChar); // move on
            throw new ScanErrorException(error);
        }
    }
}