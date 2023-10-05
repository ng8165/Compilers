package parser;

import scanner.*;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * The Parser class executes Parser-like statements as it parses them.
 * It uses the Scanner to scan tokens and uses the Pascal grammar to execute it.
 * @author Nelson Gou
 * @version 10/4/23
 */
public class Parser
{
    private final Scanner sc;
    private Token currentToken;
    private final Map<String, Object> identifiers;

    /**
     * This constructor takes a Scanner. It also loads the first token into currentToken and
     * initializes an identifiers HashMap for the symbol table.
     * @param sc Scanner that scans the file into tokens
     * @throws ScanErrorException if there is an error in scanning
     */
    public Parser(Scanner sc) throws ScanErrorException
    {
        this.sc = sc;
        currentToken = sc.nextToken();
        identifiers = new HashMap<>();
    }

    /**
     * The eat function is overloaded for convenience. It creates a new Token from the
     * String content and the Token.Type.
     * @param content the String content of the Token
     * @param type the Type of the Token
     * @throws ScanErrorException if there is an error in scanning
     */
    private void eat(String content, Token.Type type) throws ScanErrorException
    {
        eat(new Token(content, type));
    }

    /**
     * The eat function checks that the expected token is equal to the currentToken.
     * If it is not, it throws an IllegalArgumentException.
     * @param expected the expected Token
     * @throws ScanErrorException if there is an error in scanning
     * @throws IllegalArgumentException if the expected token is not equal to currentToken
     */
    private void eat(Token expected) throws ScanErrorException, IllegalArgumentException
    {
        if (currentToken.equals(expected))
            currentToken = sc.nextToken();
        else
            throw new IllegalArgumentException("Illegal character - expected " +
                    expected.getContent() + " and found " + currentToken.getContent());
    }

    /**
     * Returns a String representation of variables in the identifiers symbol table.
     * Primarily used to print Booleans as TRUE and FALSE instead of true and false.
     * @param var the variable
     * @return the String representation of var
     */
    private String variableToStr(Object var)
    {
        if (var instanceof Boolean bool)
            // print TRUE and FALSE instead of true and false
            return (bool ? "TRUE" : "FALSE");
        else
            return var.toString();
    }

    /**
     * The parseNumber function parses currentToken and returns it in an Integer representation.
     * @precondition currentToken's Type should be NUMBER
     * @return Integer representation of currentToken
     * @throws ScanErrorException if there is an error in scanning
     */
    private Integer parseNumber() throws ScanErrorException
    {
        int num = Integer.parseInt(currentToken.getContent());
        eat(currentToken);
        return num;
    }

    /**
     * The parseStatement function parses a statement. Valid statements include:
     *   - WRITELN(exprList);
     *   - BEGIN stmts END;
     *   - id := exprList;
     *   - READLN(id);
     * NOTE: An exprList is a comma-separated list of expressions that is concatenated as Strings.
     * @return true if a statement has been parsed;
     *         false if no statement was parsed (acts like hasNext)
     * @throws ScanErrorException if there is an error in scanning
     * @throws IOException if there is an error in reading user input
     */
    public boolean parseStatement() throws ScanErrorException, IOException
    {
        if (currentToken.equals("WRITELN"))
        {
            eat(currentToken);
            eat("(", Token.Type.SEPARATOR);

            System.out.print(variableToStr(parseExpression()));
            while (currentToken.equals(","))
            {
                eat(currentToken); // eat the comma
                System.out.print(variableToStr(parseExpression()));
            }
            System.out.println(); // print new line

            eat(")", Token.Type.SEPARATOR);
            eat(";", Token.Type.SEPARATOR);
        }
        else if (currentToken.equals("BEGIN"))
        {
            eat(currentToken);
            boolean cont = true;

            while (cont)
            {
                if (currentToken.equals("END"))
                {
                    eat(currentToken);
                    eat(";", Token.Type.SEPARATOR);
                    cont = false;
                }
                else
                    cont = parseStatement();
            }
        }
        else if (currentToken.equals("READLN"))
        {
            eat(currentToken);
            eat("(", Token.Type.SEPARATOR);
            String id = currentToken.getContent();
            eat(currentToken);
            eat(")", Token.Type.SEPARATOR);
            eat(";", Token.Type.SEPARATOR);
            identifiers.put(id, sc.readUserInt());
        }
        else if (currentToken.getType() == Token.Type.IDENTIFIER)
        {
            String id = currentToken.getContent();
            eat(currentToken); // eat the identifier
            eat(":=", Token.Type.OPERAND);

            Object expr = parseExpression();
            if (!currentToken.equals(","))
                identifiers.put(id, expr);
            else
            {
                StringBuilder str = new StringBuilder(variableToStr(expr));

                while (currentToken.equals(",")) {
                    eat(currentToken); // eat the comma
                    str.append(variableToStr(parseExpression()));
                }

                identifiers.put(id, str.toString());
            }

            eat(";", Token.Type.SEPARATOR);
        }
        else
            return false;

        return true;
    }

    /**
     * The parseFactor function parses a factor. Valid factors include:
     *   - (expr)
     *   - -factor
     *   - a number (parsed by parseNumber)
     *   - an identifier (looked up in the identifiers HashMap)
     *   - a boolean (TRUE or FALSE)
     *   - a string
     *   - NOT expr
     * @return the numerical value of the factor
     * @throws ScanErrorException if there is an error in scanning
     */
    private Object parseFactor() throws ScanErrorException
    {
        Object ret = null;

        if (currentToken.equals("("))
        {
            eat(currentToken);
            ret = parseExpression();
            eat(")", Token.Type.SEPARATOR);
        }
        else if (currentToken.equals("-"))
        {
            eat(currentToken);
            ret = -(Integer) parseFactor();
        }
        else if (currentToken.equals("NOT"))
        {
            eat(currentToken);
            ret = !((Boolean) parseExpression());
        }
        else if (currentToken.equals("TRUE"))
        {
            eat(currentToken);
            ret = Boolean.TRUE;
        }
        else if (currentToken.equals("FALSE"))
        {
            eat(currentToken);
            ret = Boolean.FALSE;
        }
        else if (identifiers.containsKey(currentToken.getContent()))
        {
            ret = identifiers.get(currentToken.getContent());
            eat(currentToken);
        }
        else if (currentToken.getType() == Token.Type.NUMBER)
            ret = parseNumber();
        else if (currentToken.getType() == Token.Type.STRING)
        {
            ret = currentToken.getContent();
            eat(currentToken);
        }

        return ret;
    }

    /**
     * The parseTerm function parses a term. Valid terms include:
     *   - term * factor
     *   - term / factor
     *   - term mod factor
     *   - term AND factor
     *   - factor
     * @return the numerical value of the term
     * @throws ScanErrorException if there is an error in scanning
     */
    private Object parseTerm() throws ScanErrorException
    {
        boolean cont = true;
        Object factor = parseFactor();

        while (cont)
        {
            if (currentToken.equals("*"))
            {
                eat(currentToken);
                factor = (Integer) factor * (Integer) parseFactor();
            }
            else if (currentToken.equals("/"))
            {
                eat(currentToken);
                factor = (Integer) factor / (Integer) parseFactor();
            }
            else if (currentToken.equals("mod"))
            {
                eat(currentToken);
                factor = (Integer) factor % (Integer) parseFactor();
            }
            else if (currentToken.equals("AND"))
            {
                eat(currentToken);
                factor = (Boolean) factor && (Boolean) parseFactor();
            }
            else
                cont = false;
        }

        return factor;
    }

    /**
     * The parseSimpleExpression function parses a simple expression.
     * Valid simple expressions include:
     *   - simpleExpr + term
     *   - simpleExpr - term
     *   - simpleExpr OR term
     *   - term
     * @return the numerical value of the simple expression
     * @throws ScanErrorException if there is an error in scanning
     */
    private Object parseSimpleExpression() throws ScanErrorException
    {
        Object term = parseTerm();
        boolean cont = true;

        while (cont)
        {
            if (currentToken.equals("+"))
            {
                eat(currentToken);
                term = (Integer) term + (Integer) parseTerm();
            }
            else if (currentToken.equals("-"))
            {
                eat(currentToken);
                term = (Integer) term - (Integer) parseTerm();
            }
            else if (currentToken.equals("OR"))
            {
                eat(currentToken);
                term = (Boolean) term || (Boolean) parseTerm();
            }
            else
                cont = false;
        }

        return term;
    }

    /**
     * The parseExpression function parses an expression. Valid expressions include:
     *   - simpleExpr = simpleExpr
     *   - simpleExpr <> simpleExpr
     *   - simpleExpr < simpleExpr
     *   - simpleExpr <= simpleExpr
     *   - simpleExpr > simpleExpr
     *   - simpleExpr >= simpleExpr
     *   - simpleExpr
     * @return 0 if the boolean expression is false; 1 if it is true
     * @throws ScanErrorException if there is an error in scanning
     */
    private Object parseExpression() throws ScanErrorException
    {
        Object expr = parseSimpleExpression();

        if (currentToken.equals("="))
        {
            eat(currentToken);
            return expr.equals(parseSimpleExpression());
        }
        else if (currentToken.equals("<>"))
        {
            eat(currentToken);
            return !expr.equals(parseSimpleExpression());
        }
        else if (currentToken.equals("<"))
        {
            eat(currentToken);
            return (Integer) expr < (Integer) parseSimpleExpression();
        }
        else if (currentToken.equals("<="))
        {
            eat(currentToken);
            return (Integer) expr <= (Integer) parseSimpleExpression();
        }
        else if (currentToken.equals(">"))
        {
            eat(currentToken);
            return (Integer) expr > (Integer) parseSimpleExpression();
        }
        else if (currentToken.equals(">="))
        {
            eat(currentToken);
            return (Integer) expr >= (Integer) parseSimpleExpression();
        }
        else
            return expr;
    }
}