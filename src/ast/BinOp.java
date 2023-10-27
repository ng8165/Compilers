package ast;

import environment.Environment;

import static ast.Writeln.printFormat;

/**
 * The BinOp class represents an operator expression that has two Expressions.
 * Some examples of operators include plus, minus, asterisk, mod, AND, OR, etc.
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
     * @precondition op is +, -, *, /, mod, AND, or OR
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
     * Evaluates the value of the BinOp based on the operator (+, -, *, /, mod, AND, OR).
     * @param env the Environment
     * @return the integer evaluation of the Expression
     * @throws IllegalArgumentException if the Expression types cannot apply to the operator
     */
    @Override
    public Object eval(Environment env) throws IllegalArgumentException
    {
        try
        {
            return switch (op)
            {
                case "+" -> {
                    Object eval1 = exp1.eval(env), eval2 = exp2.eval(env);
                    if (eval1 instanceof String || eval2 instanceof String)
                        yield printFormat(eval1) + printFormat(eval2); // string concatenation
                    yield (Integer) eval1 + (Integer) eval2; // addition
                }
                case "-" -> (Integer) exp1.eval(env) - (Integer) exp2.eval(env);
                case "*" -> (Integer) exp1.eval(env) * (Integer) exp2.eval(env);
                case "/" -> (Integer) exp1.eval(env) / (Integer) exp2.eval(env);
                case "mod" -> (Integer) exp1.eval(env) % (Integer) exp2.eval(env);
                case "AND" -> (Boolean) exp1.eval(env) && (Boolean) exp2.eval(env);
                default -> (Boolean) exp1.eval(env) || (Boolean) exp2.eval(env);
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
        return exp1.toString() + " " + op + " " + exp2.toString();
    }
}
