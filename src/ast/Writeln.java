package ast;

import environment.Environment;

import java.util.List;

/**
 * The Writeln class represents a WRITELN(args) statement and prints.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Writeln extends Statement
{
    private final List<Expression> exprs;

    /**
     * This constructor instantiates arguments (a List of Expressions).
     * @param exprs the Expression within the WRITELN statement
     */
    public Writeln(List<Expression> exprs)
    {
        this.exprs = exprs;
    }

    /**
     * Formats the object as a String. (used to print Bools as uppercase instead of lowercase).
     * @param obj the Object to format
     * @return a formatted String representing obj
     */
    protected static String printFormat(Object obj)
    {
        if (obj instanceof Boolean bool)
            return bool ? "TRUE" : "FALSE";
        else
            return obj.toString();
    }

    /**
     * Executes the WRITELN statement by concatenating all arguments
     * and printing it to user output.
     * @param env the Environment
     */
    @Override
    public void exec(Environment env)
    {
        StringBuilder str = new StringBuilder();
        for (Expression expr: exprs)
            str.append(printFormat(expr.eval(env)));
        System.out.println(str);
    }
}
