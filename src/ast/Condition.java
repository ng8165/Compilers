package ast;

import environment.Environment;

/**
 * The Condition class represents a boolean Condition. Consists of a relop
 * (relative operator) and two Expressions.
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
     * Evaluates the result of the Condition based on the relop (=, <>, <, >, <=, or >=).
     * @param env the Environment
     * @return true if the Condition is true; false if not.
     */
    @Override
    public Object eval(Environment env)
    {
        try
        {
            return switch (relop)
            {
                case "=" -> exp1.eval(env).equals(exp2.eval(env));
                case "<>" -> !exp1.eval(env).equals(exp2.eval(env));
                case "<" -> (Integer) exp1.eval(env) < (Integer) exp2.eval(env);
                case ">" -> (Integer) exp1.eval(env) > (Integer) exp2.eval(env);
                case "<=" -> (Integer) exp1.eval(env) <= (Integer) exp2.eval(env);
                default -> (Integer) exp1.eval(env) >= (Integer) exp2.eval(env);
            };
        }
        catch (ClassCastException e)
        {
            throw new IllegalArgumentException("Could not evaluate " + this);
        }
    }

    /**
     * Prints the Expression as a String.
     * @return String representation of the Expression
     */
    @Override
    public String toString()
    {
        return exp1.toString() + " " + relop + " " + exp2.toString();
    }
}
