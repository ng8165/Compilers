package ast;

import environment.Environment;

/**
 * The If class represents an IF-THEN block and an IF-THEN-ELSE block.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class If extends Statement
{
    private final Expression cond;
    private final Statement thenStmt;
    private final Statement elseStmt;

    /**
     * Initializes the cond and thenStmt instance variables.
     * @param cond the Expression to check
     * @param thenStmt the Statement to run in the Condition is TRUE
     */
    public If(Expression cond, Statement thenStmt)
    {
        this.cond = cond;
        this.thenStmt = thenStmt;
        this.elseStmt = null;
    }

    /**
     * Initializes the cond, thenStmt, and elseStmt instance variables.
     * @param cond the Expression to check
     * @param thenStmt the Statement to run in the Condition is TRUE
     * @param elseStmt the Statement to run in the Condition is FALSE
     */
    public If(Expression cond, Statement thenStmt, Statement elseStmt)
    {
        this.cond = cond;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }

    /**
     * Executes the If block. If cond evaluates to TRUE, executes the thenStmt.
     * If cond evaluates to FALSE and there is an elseStmt, executes it.
     * @param env the Environment
     * @throws IllegalArgumentException if cond does not evaluate to a boolean
     */
    @Override
    public void exec(Environment env)
    {
        try
        {
            if ((Boolean) cond.eval(env))
                thenStmt.exec(env);
            else if (elseStmt != null)
                elseStmt.exec(env);
        }
        catch (ClassCastException e)
        {
            throw new IllegalArgumentException("Could not execute IF " + cond + " THEN ...");
        }
    }
}
