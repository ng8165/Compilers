package ast;

import environment.Environment;

import java.util.List;

/**
 * The Program class represents a Pascal program, which is a list
 * of procedures and a Statement.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Program extends Statement
{
    private final List<ProcedureDeclaration> procedures;
    private final Statement stmt;

    /**
     * Instantiates the Program with a List of procedures and a Statement.
     * @param procedures list of procedures
     * @param stmt the Statement of the program
     */
    public Program(List<ProcedureDeclaration> procedures, Statement stmt)
    {
        this.procedures = procedures;
        this.stmt = stmt;
    }

    /**
     * Executes all ProcedureDeclarations (setting them in the Environment), then executes
     * the Statement of the program.
     * @param env the Environment
     */
    @Override
    public void exec(Environment env)
    {
        for (ProcedureDeclaration procedure: procedures)
            procedure.exec(env);

        stmt.exec(env);
    }
}
