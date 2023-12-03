package parser;

import scanner.*;
import ast.*;
import ast.Number; // remove ambiguity with java.lang.Number

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * The Parser class executes Pascal-like statements as it parses them.
 * It uses the Scanner to scan tokens and uses the Pascal grammar to generate an AST.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Parser
{
    private final Scanner sc;
    private Token currentToken;

    // varDecs and strings are Lists of all VariableDeclarations and string
    // literals that are parsed. They are submitted to Program for compilation.
    private List<VariableDeclaration> variableDeclarations;
    private Map<String, Integer> strings;

    /**
     * This constructor takes a Scanner. It also loads the first token into currentToken and
     * initializes an identifiers HashMap for the symbol table.
     * @param sc Scanner that scans the file into tokens
     */
    public Parser(Scanner sc)
    {
        this.sc = sc;
        currentToken = sc.nextToken();
        variableDeclarations = new ArrayList<>();
        strings = new HashMap<>();
    }

    /**
     * The eat function is overloaded for convenience. It creates a new Token from the
     * String content and the Token.Type.
     * @param content the String content of the Token
     * @param type the Type of the Token
     */
    private void eat(String content, Token.Type type)
    {
        eat(new Token(content, type));
    }

    /**
     * The eat function checks that the expected token is equal to the currentToken.
     * If it is not, it throws an IllegalArgumentException.
     * @param expected the expected Token
     * @throws IllegalArgumentException if the expected token is not equal to currentToken
     */
    private void eat(Token expected) throws IllegalArgumentException
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
     */
    private Number parseNumber()
    {
        Number num = new Number(Integer.parseInt(currentToken.getContent()));
        eat(currentToken);
        return num;
    }

    /**
     * The parseParams function parses a list of parameters. A list of parameters can
     * have any number of comma-separated identifiers (including zero identifiers).
     * @return List of parameters as Strings
     * @throws ScanErrorException if a non-identifier is found in the parameter list
     */
    private List<String> parseParams() throws ScanErrorException
    {
        List<String> params = new ArrayList<>();

        while (currentToken.getType() == Token.Type.IDENTIFIER)
        {
            String param = currentToken.getContent();
            if (currentToken.getType() != Token.Type.IDENTIFIER)
                throw new ScanErrorException(param + " is not a valid identifier");
            eat(currentToken);

            params.add(param);

            if (currentToken.equals(","))
                eat(currentToken);
        }

        return params;
    }

    /**
     * The parseArgs function parses a list of arguments. A list of arguments can be
     * a set of empty parentheses or have any number of comma-separated Expressions
     * within the parentheses.
     * @return List of arguments as Expressions
     */
    private List<Expression> parseArgs()
    {
        eat("(", Token.Type.SEPARATOR);
        List<Expression> args = new ArrayList<>();

        while (!currentToken.equals(")"))
        {
            Expression arg = parseCondition();
            args.add(arg);

            if (currentToken.equals(","))
                eat(currentToken);
        }

        eat(")", Token.Type.SEPARATOR);
        return args;
    }

    /**
     * The parseVarDec function parses a VariableDeclaration statement and adds
     * it to the varDecs instance variable List.
     * A valid VariableDeclaration includes the keyword "VAR", a comma separated
     * list of identifiers, a colon, and a type (integer, boolean, or string).
     */
    private void parseVariableDeclarations()
    {
        eat("VAR", Token.Type.KEYWORD); // eat "VAR" keyword
        List<String> names = parseParams();

        String type = "integer";
        if (currentToken.equals(":"))
        {
            eat(":", Token.Type.OPERAND);
            type = currentToken.getContent();
            eat(currentToken);
        }
        eat(";", Token.Type.SEPARATOR);

        for (String name: names)
            variableDeclarations.add(new VariableDeclaration(name, type));
    }

    /**
     * The parseProgram function parses the entire Pascal program. Valid programs include:
     *   - PROCEDURE id(params); stmt program
     *   - stmt .
     * @return the parsed Program
     * @throws ScanErrorException if there is an error in parsing
     */
    public Program parseProgram() throws ScanErrorException
    {
        while (currentToken.equals("VAR"))
            parseVariableDeclarations();

        List<ProcedureDeclaration> procedures = new ArrayList<>();

        while (currentToken.equals("PROCEDURE"))
        {
            eat(currentToken); // eat "PROCEDURE" keyword

            String id = currentToken.getContent();
            if (currentToken.getType() != Token.Type.IDENTIFIER)
                throw new ScanErrorException(id + " is not a valid identifier");
            eat(currentToken);
            eat("(", Token.Type.SEPARATOR);
            List<String> params = parseParams();
            eat(")", Token.Type.SEPARATOR);
            eat(";", Token.Type.SEPARATOR);

            procedures.add(new ProcedureDeclaration(id, params, parseStatement()));
        }

        // parse Statement here so varDecs and strings are correct
        Statement stmt = parseStatement();

        List<String> stringList = new ArrayList<>();
        for (int i=0; i<strings.size(); i++) stringList.add(""); // set size equal to strings size
        for (Map.Entry<String, Integer> e: strings.entrySet())
            stringList.set(e.getValue(), e.getKey());

        return new Program(variableDeclarations, stringList, procedures, stmt);
    }

    /**
     * The parseStatement function parses a statement. Valid statements include:
     *   - WRITELN(cond);
     *   - READLN(id);
     *   - BEGIN stmts END;
     *   - IF cond THEN stmt
     *   - IF cond THEN stmt ELSE stmt
     *   - WHILE cond DO stmt
     *   - FOR var := exp TO exp DO stmt
     *   - BREAK;
     *   - CONTINUE;
     *   - id := cond;
     *   - id := procedure(args);
     * @return a parsed Statement that represents the tokens
     * @throws ScanErrorException if there is an error in parsing
     */
    public Statement parseStatement() throws ScanErrorException
    {
        if (currentToken.equals("WRITELN"))
        {
            eat(currentToken); // eat "WRITELN" token
            List<Expression> args = parseArgs();
            eat(";", Token.Type.SEPARATOR);
            return new Writeln(args);
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
            Expression cond = parseCondition();
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
        else if (currentToken.equals("BREAK"))
        {
            eat(currentToken);
            eat(";", Token.Type.SEPARATOR);
            return new Break();
        }
        else if (currentToken.equals("CONTINUE"))
        {
            eat(currentToken);
            eat(";", Token.Type.SEPARATOR);
            return new Continue();
        }
        else if (currentToken.equals("VAR"))
        {
            parseVariableDeclarations();

            // return the next Statement, since VariableDeclarations are ignored in the AST
            return parseStatement();
        }
        else if (currentToken.getType() == Token.Type.IDENTIFIER)
        {
            String var = currentToken.getContent();
            eat(currentToken); // eat the variable name

            if (currentToken.equals("(")) // ProcedureCall as Statement
            {
                List<Expression> args = parseArgs();
                eat(";", Token.Type.SEPARATOR);
                return new Assignment(null, new ProcedureCall(var, args));
            }
            else // traditional Assignment
            {
                eat(":=", Token.Type.OPERAND);
                Expression exp = parseCondition();
                eat(";", Token.Type.SEPARATOR);
                return new Assignment(var, exp);
            }
        }

        throw new ScanErrorException(currentToken.getContent() +
                " did not match a valid statement");
    }

    /**
     * The parseFactor function parses a factor. Valid factors include:
     *   - (cond)
     *   - -factor
     *   - TRUE
     *   - FALSE
     *   - NOT cond
     *   - a String
     *   - id
     *   - procedure(args)
     *   - an integer
     * @return a parsed Expression which represents the factor
     * @throws ScanErrorException if there is an error in parsing
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

            if (!strings.containsKey(str))
                strings.put(str, strings.size());

            return new Str(str, strings.get(str));
        }
        else if (currentToken.getType() == Token.Type.IDENTIFIER)
        {
            String id = currentToken.getContent();
            eat(currentToken);

            if (!currentToken.equals("("))
                return new Variable(id);

            List<Expression> args = parseArgs();
            return new ProcedureCall(id, args);
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
     */
    private Expression parseTerm()
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
     */
    private Expression parseExpression()
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
     */
    private Expression parseCondition()
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