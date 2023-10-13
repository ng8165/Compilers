package ast;

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
}
