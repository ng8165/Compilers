package ast;

import environment.Environment;

/**
 * The Statement class represents a statement. Valid statements include:
 *   - WRITELN statement (which includes an Expression)
 *   - assignment statement (includes a String variable and an Expression)
 *   - block statement (a List of Statements)
 * @author Nelson Gou
 * @version 10/17/23
 */
public abstract class Statement
{
    public abstract void exec(Environment env);
}
