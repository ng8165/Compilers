package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * The Number class represents an integer number.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Number extends Expression
{
    private final int value;

    /**
     * This constructor takes an integer value and initializes the value instance variable.
     * @param value the integer number
     */
    public Number(int value)
    {
        this.value = value;
    }

    /**
     * Returns the evaluation of the Number, which is just the value itself.
     * @param env the Environment
     * @return the Number's value
     */
    @Override
    public Object eval(Environment env)
    {
        return value;
    }

    /**
     * Compiles the Number by loading it in the $v0 register.
     * @param e the Emitter
     * @return the integer Variable Type
     */
    @Override
    public Type compile(Emitter e)
    {
        e.emit("li $v0 " + value + "\t# load integer " + value + " into accumulator");
        return Type.INTEGER;
    }

    /**
     * Prints the Expression as a String.
     * @return String representation of the Expression
     */
    @Override
    public String toString()
    {
        return String.valueOf(value);
    }
}
