package ast;

import environment.Environment;
import java.util.List;

/**
 * The ProcedureCall class represents a call to a procedure.
 * Contains the name of the procedure and a list of arguments for the procedure.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class ProcedureCall extends Expression
{
    private final String id;
    private final List<Expression> args;

    /**
     * Instantiates the id of the procedure and arguments (a list of Expressions).
     * @param id procedure name
     * @param args arguments of the ProcedureCall
     */
    public ProcedureCall(String id, List<Expression> args)
    {
        this.id = id;
        this.args = args;
    }

    /**
     * Evaluates the ProcedureCall by executing the Statement that corresponds with the
     * procedure name. First, creates a child Environment for the procedure and declares all the
     * arguments with the parameter names. Instantiates the return value as the name of
     * the procedure. Then, executes the Statement of the procedure.
     * @param env the Environment
     * @return the return value of the procedure
     * @throws IllegalArgumentException if the number of args doesn't match the number of params
     */
    @Override
    public Object eval(Environment env) throws IllegalArgumentException
    {
        ProcedureDeclaration procedure = env.getProcedure(id);
        List<String> params = procedure.getParams();
        Environment childEnv = new Environment(env);
        childEnv.declareVariable(id, 0); // default return value

        if (args.size() != params.size())
            throw new IllegalArgumentException("Number of args does not match number of params");

        for (int i=0; i<args.size(); i++)
            childEnv.declareVariable(params.get(i), args.get(i).eval(env));

        procedure.getStatement().exec(childEnv);

        return childEnv.getVariable(id);
    }

    /**
     * Returns the ProcedureCall as a String.
     * @return the String version of the ProcedureCall
     */
    @Override
    public String toString()
    {
        String argStr = args.toString();
        return id + "(" + argStr.substring(1, argStr.length()-1) + ")";
    }
}
