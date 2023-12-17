package ast;

import emitter.Emitter;
import environment.Environment;

import java.util.EmptyStackException;

/**
 * The Continue class represents a CONTINUE statement in a loop.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Continue extends Statement
{
    /**
     * Executes the Continue statement by throwing a ContinueException, which will be
     * caught in the loop and skip the next execution.
     * @param env the Environment
     * @throws ContinueException to signify a Continue statement
     */
    @Override
    public void exec(Environment env)
    {
        throw new ContinueException();
    }

    /**
     * Compiles the Continue Statement by finding the most recent loop ID and jumping
     * to the end of the loop based on its ID. If no loop is found, an Exception is thrown.
     * @param e the Emitter
     */
    @Override
    public void compile(Emitter e)
    {
        try
        {
            e.emit("j cont" + e.getLoopID());
        }
        catch (EmptyStackException err)
        {
            throw new IllegalArgumentException("cannot find loop to CONTINUE out of");
        }
    }
}
