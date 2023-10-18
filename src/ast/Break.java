package ast;

import environment.Environment;

/**
 * The Break class represents a BREAK statement in a loop.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Break extends Statement
{
    /**
     * Executes the Break Statement by throwing a BreakException, which will be caught
     * in the loop and stop the loop's execution.
     * @param env the Environment
     * @throws BreakException to signify a Break statement
     */
    @Override
    public void exec(Environment env)
    {
        throw new BreakException();
    }
}
