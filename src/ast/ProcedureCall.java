package ast;

import environment.Environment;
import java.util.List;

/**
 * The ProcedureCall class represents a call to a procedure.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class ProcedureCall extends Expression
{
    private final String id;
    private final List<Expression> args;

    /**
     * Instantiates the id of the procedure.
     * @param id procedure name
     * @param args arguments of the ProcedureCall
     */
    public ProcedureCall(String id, List<Expression> args)
    {
        this.id = id;
        this.args = args;
    }

    /**
     * Evaluates the ProcedureCall by executing the Statement
     * that corresponds with the procedure name.
     * @param env the Environment
     * @return the return value of the procedure
     */
    @Override
    public Object eval(Environment env)
    {
        ProcedureDeclaration procedure = env.getProcedure(id);
        List<String> params = procedure.getParams();
        Environment childEnv = new Environment(env);
        childEnv.declareVariable(id, 0); // default return value

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
