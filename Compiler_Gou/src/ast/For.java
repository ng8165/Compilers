package ast;

import emitter.Emitter;
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

    /**
     * Compiles a FOR loop. First complies the start Expression and assigns its
     * value into var. Then, within the loop, compiles the end Expression. If the start
     * is greater than the end, jumps out of the FOR loop. Executes the FOR Statement.
     * Increments var, then jumps back to the start of the loop.
     * @param e the Emitter
     * @throws IllegalArgumentException if the variable types are incompatible
     */
    @Override
    public void compile(Emitter e)
    {
        int id = e.nextLabelID();

        if (e.getVariableType(var) != Type.INTEGER)
            throw new IllegalArgumentException(var + " must be an integer");

        // compile start and save into $v0
        if (start.compile(e) != Type.INTEGER)
            throw new IllegalArgumentException("incompatible type: " +
                    start + " must be an integer");

        e.emit("sw $v0 var" + var + "\t# compile FOR start into " + var);
        e.emit("for" + id + ":");

        // if start ($t0) > end ($v0), exit out of FOR
        e.emit("lw $v0 var" + var);
        e.emitPush("$v0");
        if (end.compile(e) != Type.INTEGER)
            throw new IllegalArgumentException("incompatible type: " +
                    end + " must be an integer");

        e.emitPop("$t0");
        e.emit("bgt $t0 $v0 endfor" + id + "\t# jump to end if start > end");

        // FOR Statement
        stmt.compile(e);

        // increment var
        e.emit("lw $v0 var" + var + "\t# increment " + var);
        e.emit("addu $v0 $v0 1");
        e.emit("sw $v0 var" + var);
        // jump to front of FOR
        e.emit("j for" + id + "\t# continue looping");

        e.emit("endfor" + id + ":"); // end FOR
    }
}
