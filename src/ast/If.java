package ast;

import environment.Environment;

/**
 * The If class represents an IF-THEN block and an IF-THEN-ELSE block.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class If extends Statement
{
    private final Condition cond;
    private final Statement thenStmt;
    private final Statement elseStmt;

    /**
     * Initializes the cond and thenStmt instance variables.
     * @param cond the Condition to check
     * @param thenStmt the Statement to run in the Condition is TRUE
     */
    public If(Condition cond, Statement thenStmt)
    {
        this.cond = cond;
        this.thenStmt = thenStmt;
        this.elseStmt = null;
    }

    /**
     * Initializes the cond, thenStmt, and elseStmt instance variables.
     * @param cond the Condition to check
     * @param thenStmt the Statement to run in the Condition is TRUE
     * @param elseStmt the Statement to run in the Condition is FALSE
     */
    public If(Condition cond, Statement thenStmt, Statement elseStmt)
    {
        this.cond = cond;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }

    /**
     * Executes the If block. If the Condition is TRUE, executes the thenStmt.
     * If the Condition is FALSE and there is an elseStmt, executes it.
     * @param env the Environment
     */
    @Override
    public void exec(Environment env)
    {
        if ((Boolean) cond.eval(env))
            thenStmt.exec(env);
        else if (elseStmt != null)
            elseStmt.exec(env);
    }
}
