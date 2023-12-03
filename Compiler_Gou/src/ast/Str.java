package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * The Str class represents a String literal.
 * This class is specifically not named String to avoid confusion with java.lang.String.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Str extends Expression
{
    private final String str;
    private final int label;

    /**
     * This constructor initializes the str instance variable.
     * The label is used to determine the name of the string address.
     * @param str the String
     * @param label the label
     */
    public Str(String str, int label)
    {
        this.str = str;
        this.label = label;
    }

    /**
     * Evaluates the String by returning its internal String.
     * @param env the Environment
     * @return the String
     */
    @Override
    public Object eval(Environment env)
    {
        return str;
    }

    /**
     * Compiles the Str by setting the label string into the accumulator.
     * @param e the Emitter
     * @return the string Variable Type
     */
    @Override
    public Type compile(Emitter e)
    {
        e.emit("la $v0 str" + label + "\t# load string into accumulator");
        return Type.STRING;
    }

    /**
     * Prints the Expression as a String.
     * @return String representation of the Expression
     */
    @Override
    public String toString()
    {
        return "'" + str + "'";
    }
}
