package ast;

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

    /**
     * This constructor initializes the str instance variable.
     * @param str the String
     */
    public Str(String str)
    {
        this.str = str;
    }

    @Override
    public Object eval(Environment env)
    {
        return str;
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
