package ast;

import java.util.List;

import emitter.Emitter;
import environment.Environment;

/**
 * The ProcedureDeclaration class represents the declaration of a procedure.
 * Contains the name of the procedure, a list of parameters, and the Statement of
 * the procedure.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class ProcedureDeclaration extends Statement
{
    private final String id;
    private final List<VariableDeclaration> params;
    private final List<VariableDeclaration> localVars;
    private final Statement stmt;
    private final Type returnType;

    /**
     * Instantiates the instance variables (procedure ID, list of parameters, and the statement).
     * @param id ID of the procedure
     * @param params the parameters of the procedure
     * @param local list of local variables in the procedure
     * @param stmt statement to execute when the procedure is called
     * @param retType String version of the return type, which is converted to a Type
     */
    public ProcedureDeclaration(String id, List<VariableDeclaration> params,
                                List<VariableDeclaration> local, Statement stmt, String retType)
    {
        this.id = id;
        this.params = params;
        localVars = local;
        this.stmt = stmt;

        if (retType == null)
            returnType = null;
        else
            switch (retType.toLowerCase()) {
                case "integer" -> returnType = Type.INTEGER;
                case "boolean" -> returnType = Type.BOOLEAN;
                case "string" -> returnType = Type.STRING;
                default -> throw new RuntimeException(retType + " is not a valid variable type");
            }
    }

    /**
     * Gets the parameters of the procedure.
     * @return procedure parameters
     */
    public List<VariableDeclaration> getParams()
    {
        return params;
    }

    /**
     * Gets the local variables of the procedure.
     * @return procedure's local variables
     */
    public List<VariableDeclaration> getLocalVars()
    {
        return localVars;
    }

    /**
     * Gets the name of the procedure.
     * @return procedure name
     */
    public String getName()
    {
        return id;
    }

    /**
     * Gets the statement of the procedure
     * @return procedure Statement
     */
    public Statement getStatement()
    {
        return stmt;
    }

    /**
     * Gets the return type of the procedure.
     * @return procedure return type
     */
    public Type getReturnType()
    {
        return returnType;
    }

    /**
     * Maps the Procedure's statement to its ID in the Environment so it can be executed later.
     * @param env the Environment
     */
    public void exec(Environment env)
    {
        env.setProcedure(id, this);
    }

    /**
     * Compiles the ProcedureDeclaration.
     * @param e the Emitter
     */
    public void compile(Emitter e)
    {
        e.setProcedureContext(this);

        e.emit("proc" + id + ":");
        stmt.compile(e);
        e.emit("jr $ra");

        e.clearProcedureContext();
    }
}
