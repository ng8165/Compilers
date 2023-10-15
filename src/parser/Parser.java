package parser;

import scanner.*;
import ast.*;
import ast.Number; // remove ambiguity with java.lang.Number

import java.util.ArrayList;
import java.util.List;

/**
 * The Parser class executes Parser-like statements as it parses them.
 * It uses the Scanner to scan tokens and uses the Pascal grammar to generate a parse tree.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Parser
{
    private final Scanner sc;
    private Token currentToken;

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
    }

    /**
     * Returns true if there is another statement and false if not.
     * @return true if there is another statement; otherwise false.
     */
    public boolean hasNext()
    {
        return sc.hasNext();
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
     * The parseNumber function parses a number.
     * @precondition currentToken's Type should be NUMBER
     * @return a parsed Number which contains the integer
     * @throws ScanErrorException if there is an error in scanning
     */
    private Number parseNumber() throws ScanErrorException
    {
        Number num = new Number(Integer.parseInt(currentToken.getContent()));
        eat(currentToken);
        return num;
    }

    /**
     * The parseStatement function parses a statement. Valid statements include:
     *   - WRITELN(cond);
     *   - READLN(id);
     *   - BEGIN stmts END;
     *   - id := cond;
     *   - IF cond THEN stmt
     *   - IF cond THEN stmt ELSE stmt
     *   - WHILE cond DO stmt
     *   - FOR var := exp TO exp DO stmt
     * @return a parsed Statement (Writeln, Block, or Assignment) that represents the tokens
     * @throws ScanErrorException if there is an error in scanning
     */
    public Statement parseStatement() throws ScanErrorException
    {
        if (currentToken.equals("WRITELN"))
        {
            eat(currentToken); // eat "WRITELN" token
            eat("(", Token.Type.SEPARATOR);
            Expression exp = parseCondition();
            eat(")", Token.Type.SEPARATOR);
            eat(";", Token.Type.SEPARATOR);
            return new Writeln(exp);
        }
        else if (currentToken.equals("READLN"))
        {
            eat(currentToken); // eat "READLN" token
            eat("(", Token.Type.SEPARATOR);

            String var = currentToken.getContent();
            if (currentToken.getType() != Token.Type.IDENTIFIER)
                throw new ScanErrorException(var + " is not a valid identifier");
            eat(currentToken);

            eat(")", Token.Type.SEPARATOR);
            eat(";", Token.Type.SEPARATOR);

            return new Readln(var);
        }
        else if (currentToken.equals("BEGIN"))
        {
            eat(currentToken); // eat "BEGIN" token
            List<Statement> stmts = new ArrayList<>();

            while (!currentToken.equals("END"))
                stmts.add(parseStatement());

            eat(currentToken); // eat "END" token
            eat(";", Token.Type.SEPARATOR);

            return new Block(stmts);
        }
        else if (currentToken.equals("IF"))
        {
            eat(currentToken); // eat "IF"
            Expression cond = parseCondition();
            eat("THEN", Token.Type.KEYWORD);
            Statement thenStmt = parseStatement();

            if (currentToken.equals("ELSE"))
            {
                eat(currentToken); // eat "ELSE"
                return new If(cond, thenStmt, parseStatement());
            }
            return new If(cond, thenStmt);
        }
        else if (currentToken.equals("WHILE"))
        {
            eat(currentToken); // eat "WHILE"
            Condition cond = (Condition) parseCondition();
            eat("DO", Token.Type.KEYWORD);
            return new While(cond, parseStatement());
        }
        else if (currentToken.equals("FOR"))
        {
            eat(currentToken); // eat "FOR"

            String var = currentToken.getContent();
            if (currentToken.getType() != Token.Type.IDENTIFIER)
                throw new ScanErrorException(var + " is not a valid identifier");
            eat(currentToken);

            eat(":=", Token.Type.OPERAND);
            Expression start = parseExpression();
            eat("TO", Token.Type.KEYWORD);
            Expression end = parseExpression();
            eat("DO", Token.Type.KEYWORD);

            return new For(var, start, end, parseStatement());
        }
        else if (currentToken.getType() == Token.Type.IDENTIFIER)
        {
            String var = currentToken.getContent();
            eat(currentToken); // eat the variable name
            eat(":=", Token.Type.OPERAND);
            Expression exp = parseCondition();
            eat(";", Token.Type.SEPARATOR);
            return new Assignment(var, exp);
        }

        throw new ScanErrorException(currentToken.getContent() +
                " did not match a valid statement");
    }

    /**
     * The parseFactor function parses a factor. Valid factors include:
     *   - (cond)
     *   - -factor
     *   - NOT cond
     *   - an integer (parsed by parseNumber)
     *   - TRUE and FALSE
     *   - an identifier (looked up in the identifiers HashMap)
     *   - a String
     * @return a parsed Expression which represents the factor
     * @throws ScanErrorException if there is an error in scanning
     */
    private Expression parseFactor() throws ScanErrorException
    {
        if (currentToken.equals("("))
        {
            eat(currentToken);
            Expression exp = parseCondition();
            eat(")", Token.Type.SEPARATOR);
            return exp;
        }
        else if (currentToken.equals("-"))
        {
            eat(currentToken);
            return new UnOp("-", parseFactor());
        }
        else if (currentToken.equals("TRUE"))
        {
            eat(currentToken);
            return new Bool(true);
        }
        else if (currentToken.equals("FALSE"))
        {
            eat(currentToken);
            return new Bool(false);
        }
        else if (currentToken.equals("NOT"))
        {
            eat(currentToken);
            return new UnOp("NOT", parseCondition());
        }
        else if (currentToken.getType() == Token.Type.STRING)
        {
            String str = currentToken.getContent();
            eat(currentToken);
            return new Str(str);
        }
        else if (currentToken.getType() == Token.Type.IDENTIFIER)
        {
            Variable var = new Variable(currentToken.getContent());
            eat(currentToken);
            return var;
        }
        else if (currentToken.getType() == Token.Type.NUMBER)
            return parseNumber();

        throw new ScanErrorException(currentToken.getContent() + " did not match a valid factor");
    }

    /**
     * The parseTerm function parses a term. Valid terms include:
     *   - term * factor
     *   - term / factor
     *   - term mod factor
     *   - term AND factor
     *   - factor
     * @return a parsed Expression which represents the term
     * @throws ScanErrorException if there is an error in scanning
     */
    private Expression parseTerm() throws ScanErrorException
    {
        boolean cont = true;
        Expression factor = parseFactor();

        while (cont)
        {
            if (currentToken.equals("*"))
            {
                eat(currentToken); // eat "*" token
                factor = new BinOp("*", factor, parseFactor());
            }
            else if (currentToken.equals("/"))
            {
                eat(currentToken); // eat "/" token
                factor = new BinOp("/", factor, parseFactor());
            }
            else if (currentToken.equals("mod"))
            {
                eat(currentToken); // eat "mod" token
                factor = new BinOp("mod", factor, parseFactor());
            }
            else if (currentToken.equals("AND"))
            {
                eat(currentToken); // eat "AND" token
                factor = new BinOp("AND", factor, parseFactor());
            }
            else
                cont = false;
        }

        return factor;
    }

    /**
     * The parseExpression function parses an expression.
     * Valid expressions include:
     *   - expr + term
     *   - expr - term
     *   - expr OR term
     *   - term
     * @return a parsed Expression which represents the expression
     * @throws ScanErrorException if there is an error in scanning
     */
    private Expression parseExpression() throws ScanErrorException
    {
        boolean cont = true;
        Expression term = parseTerm();

        while (cont)
        {
            if (currentToken.equals("+"))
            {
                eat(currentToken); // eat "+" token
                term = new BinOp("+", term, parseTerm());
            }
            else if (currentToken.equals("-"))
            {
                eat(currentToken); // eat "-" token
                term = new BinOp("-", term, parseTerm());
            }
            else if (currentToken.equals("OR"))
            {
                eat(currentToken); // eat "OR" token
                term = new BinOp("OR", term, parseTerm());
            }
            else
                cont = false;
        }

        return term;
    }

    /**
     * The parseCondition function parses a condition. Valid conditions include:
     *   - expr = expr
     *   - expr <> expr
     *   - expr < expr
     *   - expr > expr
     *   - expr <= expr
     *   - expr >= expr
     *   - expr
     * @return a parsed Expression which represents the condition
     * @throws ScanErrorException if there is an error in scanning
     */
    private Expression parseCondition() throws ScanErrorException
    {
        Expression exp1 = parseExpression();
        if (currentToken.getType() != Token.Type.RELOPS)
            return exp1;

        String relop = currentToken.getContent();
        eat(currentToken); // eat the relop
        Expression exp2 = parseExpression();

        return new Condition(exp1, relop, exp2);
    }
}