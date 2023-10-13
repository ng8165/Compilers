package ast;

import environment.Environment;

/**
 * The Variable class represents a variable in the symbol table.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Variable extends Expression
{
    private final String name;

    /**
     * This constructor takes a String and initializes the name instance variable with it.
     * @param name the name of the variable
     */
    public Variable(String name)
    {
        this.name = name;
    }

    /**
     * Returns the evaluation of the Variable, which is the integer that it represents.
     * @param env the Environment used to look up the value of the variable
     * @return the value of the Variable
     */
    @Override
    public Object eval(Environment env)
    {
        return env.getVariable(name);
    }
}
