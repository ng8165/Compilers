package ast;

import environment.Environment;

/**
 * The UnOp class represents a unary operator.
 * Examples of unary operators include NOT and unary minus.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class UnOp extends Expression
{
    private final Expression exp;
    private final String op;

    /**
     * Initializes the Expression and String operator instance variables.
     * @precondition op must be NOT or -
     * @param op the operator
     * @param exp the Expression
     */
    public UnOp(String op, Expression exp)
    {
        this.op = op;
        this.exp = exp;
    }

    /**
     * Evaluates the unary operator.
     * @param env the Environment
     * @return the result of the unary Expression
     * @throws IllegalArgumentException if the Expression type cannot apply to the operator
     */
    @Override
    public Object eval(Environment env)
    {
        try
        {
            if (op.equals("NOT"))
                return !(Boolean) exp.eval(env);
            else
                return -(Integer) exp.eval(env);
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
        return op + (op.equals("NOT") ? " " : "") + exp.toString();
    }
}
