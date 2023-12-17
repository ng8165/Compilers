package ast;

import emitter.Emitter;
import environment.Environment;

import java.util.EmptyStackException;

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

    /**
     * Compiles the Break Statement by finding the most recent loop ID and jumping
     * to the end of the loop based on its ID. If no loop is found, an Exception is thrown.
     * @param e the Emitter
     */
    @Override
    public void compile(Emitter e)
    {
        try
        {
            e.emit("j end" + e.getLoopID());
        }
        catch (EmptyStackException err)
        {
            throw new IllegalArgumentException("cannot find loop to BREAK out of");
        }
    }
}
