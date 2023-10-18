package ast;

import environment.Environment;

/**
 * The For class represents a FOR loop.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class For extends Statement
{
    private final String var;
    private final Expression start;
    private final Expression end;
    private final Statement stmt;

    /**
     * This constructor initializes the instance variables.
     * @param var the name of the FOR loop variable
     * @param start the Expression representing the initial value of var
     * @param end the Expression representing the ending value of var
     * @param stmt the Statement to run in the loop
     */
    public For(String var, Expression start, Expression end, Statement stmt)
    {
        this.var = var;
        this.start = start;
        this.end = end;
        this.stmt = stmt;
    }

    /**
     * Executes the FOR loop.
     * @param env the Environment
     */
    @Override
    public void exec(Environment env)
    {
        try
        {
            int startIndex = (Integer) start.eval(env);
            int endIndex = (Integer) end.eval(env);

            for (int i = startIndex; i <= endIndex; i++)
            {
                env.setVariable(var, i);

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
                    continue; // not necessary but there needs to be something here
                }
            }
        }
        catch (ClassCastException e)
        {
            throw new IllegalArgumentException("Could not execute FOR " + var + " := " +
                    start + " TO " + end + " DO ...");

        }
    }
}
