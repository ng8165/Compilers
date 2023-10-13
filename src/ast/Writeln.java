package ast;

import environment.Environment;

/**
 * The Writeln class represents a WRITELN(expr) statement.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Writeln extends Statement
{
    private final Expression exp;

    /**
     * This constructor takes an Expression and initializes the exp instance variable.
     * @param exp the Expression within the WRITELN statement
     */
    public Writeln(Expression exp)
    {
        this.exp = exp;
    }

    /**
     * Executes the WRITELN statement by printing the evaluation of the Expression.
     * @param env the Environment
     */
    @Override
    public void exec(Environment env)
    {
        Object obj = exp.eval(env);

        if (obj instanceof Bool bool) // print TRUE and FALSE instead of true and false
            System.out.println((Boolean) bool.eval(env) ? "TRUE" : "FALSE");
        else if (obj instanceof Boolean bool)
            System.out.println(bool ? "TRUE" : "FALSE");
        else
            System.out.println(obj);
    }
}
