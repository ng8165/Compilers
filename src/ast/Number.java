package ast;

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
     * Prints the Expression as a String.
     * @return String representation of the Expression
     */
    @Override
    public String toString()
    {
        return String.valueOf(value);
    }
}
