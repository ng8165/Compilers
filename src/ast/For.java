package ast;

import environment.Environment;

/**
 * The For class represents a for loop. For loops evaluate a start Expression and assign it
 * to a given variable. The variable is then incremented until it is greater than the end
 * Expression. The loop execution then stops.
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
     * Executes the FOR loop. Evaluates start and assigns it to the variable. Increments
     * the variable until it is greater than end. If a BreakException is caught, the loop
     * is exited. If a ContinueException is caught, one execution of the loop is skipped.
     * @param env the Environment
     * @throws IllegalArgumentException if start or end Expression does not evaluate to a number
     */
    @Override
    public void exec(Environment env) throws IllegalArgumentException
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
