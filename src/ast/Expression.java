package ast;

import environment.Environment;

/**
 * The Expression class represents an expression. Valid expressions include:
 *   - a variable (as a String)
 *   - binary operator (two Expressions and a String operator)
 *   - unary operator
 *   - Bool, Str, and Number literals
 *   - a Condition
 *   - a ProcedureCall
 * @author Nelson Gou
 * @version 10/17/23
 */
public abstract class Expression
{
    /**
     * Evaluates the Expression.
     * @param env the Environment
     * @return the result of the Expression evaluation
     */
    public abstract Object eval(Environment env);
}
