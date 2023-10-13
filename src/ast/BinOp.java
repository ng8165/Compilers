package ast;

import environment.Environment;

/**
 * The BinOp class represents a binary operator expression.
 * Some examples of binary operators include plus, minus, asterisk, AND, OR, etc.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class BinOp extends Expression
{
    private final String op;
    private final Expression exp1;
    private final Expression exp2;

    /**
     * This constructor takes in a String operator and two Expressions and initializes
     * the corresponding instance variables with them.
     * @precondition op is +, -, *, /, MOD, AND, or OR
     * @param op the operator
     * @param exp1 the first Expression
     * @param exp2 the second Expression
     */
    public BinOp(String op, Expression exp1, Expression exp2)
    {
        this.op = op;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    /**
     * Evaluates the value of the BinOp based on the operator (addition, subtraction,
     * multiplication, division).
     * @param env the Environment
     * @return the integer evaluation of the Expression
     * @throws IllegalArgumentException if the Expression types cannot apply to the operator
     */
    @Override
    public Object eval(Environment env) throws IllegalArgumentException
    {
        try
        {
            if (op.equals("+"))
                return (Integer) exp1.eval(env) + (Integer) exp2.eval(env);
            else if (op.equals("-"))
                return (Integer) exp1.eval(env) - (Integer) exp2.eval(env);
            else if (op.equals("*"))
                return (Integer) exp1.eval(env) * (Integer) exp2.eval(env);
            else if (op.equals("/"))
                return (Integer) exp1.eval(env) / (Integer) exp2.eval(env);
            else if (op.equals("mod"))
                return (Integer) exp1.eval(env) % (Integer) exp2.eval(env);
            else if (op.equals("AND"))
                return (Boolean) exp1.eval(env) && (Boolean) exp2.eval(env);
            else
                return (Boolean) exp1.eval(env) || (Boolean) exp2.eval(env);
        }
        catch (ClassCastException e)
        {
            throw new IllegalArgumentException("Could not evaluate the expression " +
                    exp1.eval(env) + " " + op + " " + exp2.eval(env));
        }
    }
}
