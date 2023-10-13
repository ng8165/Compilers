package ast;

import environment.Environment;

/**
 * The While class represents a WHILE-DO block.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class While extends Statement
{
    private final Condition cond;
    private final Statement stmt;

    /**
     * Initializes the WHILE Condition and the Statement.
     * @param cond Condition to check
     * @param stmt Statement to run
     */
    public While(Condition cond, Statement stmt)
    {
        this.cond = cond;
        this.stmt = stmt;
    }

    /**
     * Executes the WHILE-DO loop.
     * @param env the Environment
     */
    @Override
    public void exec(Environment env)
    {
        while ((Boolean) cond.eval(env))
            stmt.exec(env);
    }
}