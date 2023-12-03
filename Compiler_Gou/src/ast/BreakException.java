package ast;

/**
 * The BreakException is thrown whenever there is a BREAK statement in a loop.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class BreakException extends RuntimeException
{
    /**
     * Constructor for the BreakException.
     */
    public BreakException()
    {
        super();
    }
}
