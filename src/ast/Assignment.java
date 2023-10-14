package ast;

import environment.Environment;

/**
 * The Assignment class represents an assignment statement that consists
 * of a String variable and an Expression which is assigned to the variable.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Assignment extends Statement
{
    private final String var;
    private final Expression exp;

    /**
     * This constructor initializes the String variable name and
     * the Expression which it is equal to.
     * @param var the String name
     * @param exp the Expression
     */
    public Assignment(String var, Expression exp)
    {
        this.var = var;
        this.exp = exp;
    }

    /**
     * Executes the Assingment by setting the value of var to the evaluation of exp
     * in the env Environment.
     * @param env the Environment
     */
    @Override
    public void exec(Environment env)
    {
        env.setVariable(var, exp.eval(env));
    }
}