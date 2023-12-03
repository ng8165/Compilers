package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * The Bool class represents a boolean literal.
 * This class is specifically not named Boolean to avoid confusion with java.lang.Boolean.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Bool extends Expression
{
    private final boolean value;

    /**
     * Instantiates the value of the Bool.
     * @param value the boolean value
     */
    public Bool(boolean value)
    {
        this.value = value;
    }

    /**
     * Evaluates the Bool by returning the internal boolean value.
     * @param env the Environment
     * @return the internal boolean value
     */
    @Override
    public Object eval(Environment env)
    {
        return value;
    }

    /**
     * Compiles the Bool by loading it into the accumulator.
     * @param e the Emitter
     * @return the Boolean Variable Type
     */
    @Override
    public Type compile(Emitter e)
    {
        e.emit("li $v0 " + (value ? "-1\t# load TRUE" : "0\t# load FALSE") + " into accumulator");
        return Type.BOOLEAN;
    }

    /**
     * Prints the Bool as a String (capital TRUE and FALSE instead of lowercase).
     * @return String representation of the Expression
     */
    @Override
    public String toString()
    {
        return value ? "TRUE" : "FALSE";
    }
}
