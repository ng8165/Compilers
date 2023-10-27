package ast;

import environment.Environment;

/**
 * The Assignment class represents an assignment statement that consists
 * of a String variable and an Expression which is assigned to the variable.
 * Also used to execute procedures.
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
     * Executes the Assignment by setting the value of var to the evaluation of exp
     * in the env Environment. var may be null when ProcedureCall is used as a Statement.
     * In this case, the procedure is evaluated, and its return value (if any) is ignored.
     * @param env the Environment
     */
    @Override
    public void exec(Environment env)
    {
        if (var == null) // when ProcedureCall is used as a Statement
            exp.eval(env);
        else
            env.setVariable(var, exp.eval(env));
    }
}
