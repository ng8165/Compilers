package ast;

import emitter.Emitter;
import environment.Environment;

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
}
