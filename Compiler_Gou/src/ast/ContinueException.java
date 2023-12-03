package ast;

/**
 * The ContinueException is thrown whenever there is a CONTINUE statement in a loop.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class ContinueException extends RuntimeException
{
    /**
     * Constructor for the ContinueException.
     */
    public ContinueException()
    {
        super();
    }
}
