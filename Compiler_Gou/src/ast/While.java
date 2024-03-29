package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * The While class represents a while loop. It checks the condition and executes the Statement
 * if the Condition evaluates to TRUE. The loop then checks the condition again and continues
 * looping until the condition evaluates to FALSE.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class While extends Statement
{
    private final Expression cond;
    private final Statement stmt;

    /**
     * Initializes the WHILE condition and the Statement.
     * @param cond condition to check
     * @param stmt Statement to run
     */
    public While(Expression cond, Statement stmt)
    {
        this.cond = cond;
        this.stmt = stmt;
    }

    /**
     * Executes the WHILE loop. Checks the Condition and executes the Statement as long as
     * the Condition evaluates to TRUE. As soon as the Condition executes to FALSE,
     * execution stops. If a BreakException is caught, the loop is exited. If a
     * ContinueException is caught, one execution of the loop is skipped.
     * @param env the Environment
     * @throws IllegalArgumentException if cond does not evaluate to a boolean
     */
    @Override
    public void exec(Environment env)
    {
        try
        {
            while ((Boolean) cond.eval(env))
            {
                try
                {
                    stmt.exec(env);
                }
                catch (BreakException e)
                {
                    break;
                }
                catch (ContinueException e)
                {
                    continue; // not really necessary but there needs to be something here
                }
            }
        }
        catch (ClassCastException e)
        {
            throw new IllegalArgumentException("Could not execute WHILE " + cond +
                    " DO ...");
        }
    }

    /**
     * Compiles the While loop by compiling the condition, then running the
     * internal statement, and going back to the top of the While loop again.
     * @param e the Emitter
     */
    @Override
    public void compile(Emitter e)
    {
        int id = e.nextLabelID();
        e.pushLoopID("while" + id);

        Condition c = (cond instanceof Condition) ?
                (Condition) cond :
                new Condition(cond, "=", new Bool(true));

        e.emit("while" + id + ":"); // WHILE start
        e.emit("contwhile" + id + ":"); // where continue jumps to

        // if cond is FALSE, jump past WHILE
        c.compile(e, "endwhile" + id);

        // WHILE Statement and jump back to WHILE start
        stmt.compile(e);
        e.emit("j while" + id + "\t# continue looping");

        e.emit("endwhile" + id + ":"); // after WHILE

        e.popLoopID();
    }
}