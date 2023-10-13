package ast;

import environment.Environment;

/**
 * The Expression class represents an expression. Valid expressions include:
 *   - a number (as an int)
 *   - a variable (as a String)
 *   - binary operator (two Expressions and a String operator)
 * @author Nelson Gou
 * @version 10/17/23
 */
public abstract class Expression
{
    public abstract Object eval(Environment env);
}
