package ast;

import environment.Environment;

/**
 * The Expression class represents an expression. Valid expressions include:
 *   - Variable
 *   - BinOp (binary operator)
 *   - UnOp (unary operator)
 *   - Bool, Str, and Number literals
 *   - Condition
 *   - ProcedureCall
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
