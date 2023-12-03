package ast;

import emitter.Emitter;
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
     * Compiles the UnOp by compiling exp and emitting the appropriate code based on the operator.
     * @param e the Emitter
     * @return the variable Type of the UnOp
     */
    @Override
    public Type compile(Emitter e)
    {
        // $v0 = exp
        Type type = exp.compile(e);

        try {
            return switch (op.toLowerCase()) {
                case "-" -> {
                    if (type != Type.INTEGER) throw new RuntimeException();
                    e.emit("subu $v0 $zero $v0\t# negate accumulator contents");
                    yield Type.INTEGER;
                }
                case "not" -> {
                    if (type != Type.BOOLEAN) throw new RuntimeException();
                    e.emit("not $v0 $v0");
                    yield Type.BOOLEAN;
                }
                default -> super.compile(e);
            };
        }
        catch (RuntimeException err)
        {
            throw new IllegalArgumentException("Incompatible operand type for " +
                    op + " in " + this);
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
