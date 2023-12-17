package ast;

import emitter.Emitter;
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

    /**
     * Compiles the Variable by loading its value into $v0.
     * @param e the Emitter
     */
    @Override
    public Type compile(Emitter e)
    {
        if (e.isLocalVariable(name))
            e.emit("lw $v0 " + e.getOffset(name) + "($sp)\t# load local variable into $v0");
        else
            e.emit("lw $v0 var" + name + "\t# loads variable " + name + " into $v0");

        return e.getVariableType(name);
    }

    /**
     * Prints the Expression as a String.
     * @return String representation of the Expression
     */
    @Override
    public String toString()
    {
        return name;
    }
}
