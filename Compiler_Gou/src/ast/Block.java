package ast;

import emitter.Emitter;
import environment.Environment;

import java.util.List;

/**
 * The Block class represents a list of Statements.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Block extends Statement
{
    private final List<Statement> stmts;

    /**
     * This constructor initializes the stmts instance variable.
     * @param stmts the list of Statements
     */
    public Block(List<Statement> stmts)
    {
        this.stmts = stmts;
    }

    /**
     * Executes all Statements inside the Block.
     * @param env the Environment variable
     */
    @Override
    public void exec(Environment env)
    {
        for (Statement stmt: stmts)
            stmt.exec(env);
    }

    /**
     * Compiles the Block by compiling all Statements in the Block.
     * @param e the Emitter
     */
    @Override
    public void compile(Emitter e)
    {
        for (Statement stmt: stmts)
            stmt.compile(e);
    }
}
