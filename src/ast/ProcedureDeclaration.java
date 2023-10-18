package ast;

import java.util.List;
import environment.Environment;

/**
 * The ProcedureDeclaration class represents the declaration of a procedure.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class ProcedureDeclaration extends Statement
{
    private final String id;
    private final List<String> params;
    private final Statement stmt;

    /**
     * Instantiates the instance variables (procedure ID and the statement).
     * @param id ID of the procedure
     * @param params the parameters of the procedure
     * @param stmt statement to execute when the procedure is called
     */
    public ProcedureDeclaration(String id, List<String> params, Statement stmt)
    {
        this.id = id;
        this.params = params;
        this.stmt = stmt;
    }

    /**
     * Gets the parameters of the procedure.
     * @return procedure parameters
     */
    public List<String> getParams()
    {
        return params;
    }

    /**
     * Gets the Statement of the procedure
     * @return procedure Statement
     */
    public Statement getStatement()
    {
        return stmt;
    }

    /**
     * Maps the Procedure's statement to its ID in the Environment so it can be executed later.
     * @param env the Environment
     */
    @Override
    public void exec(Environment env)
    {
        env.setProcedure(id, this);
    }
}
