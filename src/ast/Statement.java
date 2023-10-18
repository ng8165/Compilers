package ast;

import environment.Environment;

/**
 * The Statement class represents a statement. Valid statements include:
 *   - WRITELN statement (which includes an Expression)
 *   - READLN statement
 *   - Assignment statement (includes a String variable and an Expression)
 *   - Block statement (a List of Statements)
 *   - Break and Continue statements
 *   - If statements
 *   - For and While loop statements
 *   - ProcedureDeclaration statements
 *   - Program statements
 * @author Nelson Gou
 * @version 10/17/23
 */
public abstract class Statement
{
    /**
     * Executes the Statement.
     * @param env the Environment
     */
    public abstract void exec(Environment env);
}
