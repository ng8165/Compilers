package ast;

import environment.Environment;

/**
 * The Condition class represents a boolean Condition.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Condition extends Expression
{
    private final Expression exp1;
    private final String relop;
    private final Expression exp2;

    /**
     * Initializes the instance variables exp1, relop, and exp2.
     * @param exp1 first Expression
     * @param relop boolean operator
     * @param exp2 second Expression
     */
    public Condition(Expression exp1, String relop, Expression exp2)
    {
        this.exp1 = exp1;
        this.relop = relop;
        this.exp2 = exp2;
    }

    /**
     * Evaluates the result of the Condition.
     * @param env the Environment
     * @return true if the Condition is true; false if not.
     */
    @Override
    public Object eval(Environment env)
    {
        try
        {
            if (relop.equals("="))
                return exp1.eval(env).equals(exp2.eval(env));
            else if (relop.equals("<>"))
                return !exp1.eval(env).equals(exp2.eval(env));
            else if (relop.equals("<"))
                return (Integer) exp1.eval(env) < (Integer) exp2.eval(env);
            else if (relop.equals(">"))
                return (Integer) exp1.eval(env) > (Integer) exp2.eval(env);
            else if (relop.equals("<="))
                return (Integer) exp1.eval(env) <= (Integer) exp2.eval(env);
            else
                return (Integer) exp1.eval(env) >= (Integer) exp2.eval(env);
        }
        catch (ClassCastException e)
        {
            throw new IllegalArgumentException("Could not evaluate the expression " +
                    exp1.eval(env) + " " + relop + " " + exp2.eval(env));
        }
    }
}
