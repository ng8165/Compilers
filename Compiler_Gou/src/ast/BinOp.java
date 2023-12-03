package ast;

import emitter.Emitter;
import environment.Environment;

import static ast.Writeln.printFormat;

/**
 * The BinOp class represents an operator expression that has two Expressions.
 * Some examples of operators include plus, minus, asterisk, mod, AND, OR, etc.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class BinOp extends Expression
{
    private final String op;
    private final Expression exp1;
    private final Expression exp2;

    /**
     * This constructor takes in a String operator and two Expressions and initializes
     * the corresponding instance variables with them.
     * @precondition op is +, -, *, /, mod, AND, or OR
     * @param op the operator
     * @param exp1 the first Expression
     * @param exp2 the second Expression
     */
    public BinOp(String op, Expression exp1, Expression exp2)
    {
        this.op = op;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    /**
     * Evaluates the value of the BinOp based on the operator (+, -, *, /, mod, AND, OR).
     * @param env the Environment
     * @return the integer evaluation of the Expression
     * @throws IllegalArgumentException if the Expression types cannot apply to the operator
     */
    @Override
    public Object eval(Environment env) throws IllegalArgumentException
    {
        try
        {
            return switch (op)
            {
                case "+" -> {
                    Object eval1 = exp1.eval(env), eval2 = exp2.eval(env);
                    if (eval1 instanceof String || eval2 instanceof String)
                        yield printFormat(eval1) + printFormat(eval2); // string concatenation
                    yield (Integer) eval1 + (Integer) eval2; // addition
                }
                case "-" -> (Integer) exp1.eval(env) - (Integer) exp2.eval(env);
                case "*" -> (Integer) exp1.eval(env) * (Integer) exp2.eval(env);
                case "/" -> (Integer) exp1.eval(env) / (Integer) exp2.eval(env);
                case "mod" -> (Integer) exp1.eval(env) % (Integer) exp2.eval(env);
                case "AND" -> (Boolean) exp1.eval(env) && (Boolean) exp2.eval(env);
                default -> (Boolean) exp1.eval(env) || (Boolean) exp2.eval(env);
            };
        }
        catch (ClassCastException e)
        {
            throw new IllegalArgumentException("Could not evaluate " + this);
        }
    }

    /**
     * Compiles the BinOp by first compiling exp1 and exp2 and then emitting
     * the appropriate commands based on the operator.
     * @param e the Emitter
     * @return the return Variable Type
     * @throws IllegalArgumentException if variable types are incompatible
     */
    @Override
    public Type compile(Emitter e)
    {
        // $t0 = exp1 and $v0 = exp2
        Type t1 = exp1.compile(e);
        e.emitPush("$v0");
        Type t2 = exp2.compile(e);
        e.emitPop("$t0");

        try {
            return switch (op.toLowerCase()) {
                case "+" -> {
                    if (t1 == Type.INTEGER && t2 == Type.INTEGER)
                    {
                        e.emit("addu $v0 $t0 $v0\t# add $t0 and $v0");
                        yield Type.INTEGER;
                    }
                    else if (t1 != Type.STRING && t2 != Type.STRING) throw new RuntimeException();

                    // convert t1 to STRING if necessary
                    if (t1 != Type.STRING)
                    {
                        e.emitPush("$v0");
                        e.emit("move $a0 $t0");
                        if (t1 == Type.BOOLEAN) e.emit("jal boolToStr\t# convert bool to string");
                        else e.emit("jal intToStr\t# convert integer to string");
                        e.emit("move $t0 $v0");
                        e.emitPop("$v0");
                    }

                    // convert t2 to STRING if necessary
                    if (t2 != Type.STRING)
                    {
                        e.emitPush("$t0");
                        e.emit("move $a0 $v0");
                        if (t2 == Type.BOOLEAN) e.emit("jal boolToStr\t# convert bool to string");
                        else e.emit("jal intToStr\t# convert integer to string");
                        e.emitPop("$t0");
                    }

                    // concatenate strings
                    e.emit("move $a0 $t0");
                    e.emit("move $a1 $v0");
                    e.emit("jal strcat");

                    yield Type.STRING;
                }
                case "-" -> {
                    if (t1 != Type.INTEGER || t2 != Type.INTEGER) throw new RuntimeException();
                    e.emit("subu $v0 $t0 $v0\t# subtract $t0 and $v0");
                    yield Type.INTEGER;
                }
                case "*" -> {
                    if (t1 != Type.INTEGER || t2 != Type.INTEGER) throw new RuntimeException();
                    e.emit("mult $t0 $v0");
                    e.emit("mflo $v0\t# multiply $t0 and $v0");
                    yield Type.INTEGER;
                }
                case "/" -> {
                    if (t1 != Type.INTEGER || t2 != Type.INTEGER) throw new RuntimeException();
                    e.emit("div $t0 $v0");
                    e.emit("mflo $v0\t#divide $t0 and $v0");
                    yield Type.INTEGER;
                }
                case "mod" -> {
                    if (t1 != Type.INTEGER || t2 != Type.INTEGER) throw new RuntimeException();
                    e.emit("div $t0 $v0");
                    e.emit("mfhi $v0\t# find remainder of $t0 and $v0");
                    yield Type.INTEGER;
                }
                case "and" -> {
                    if (t1 != Type.BOOLEAN || t2 != Type.BOOLEAN) throw new RuntimeException();
                    e.emit("and $v0 $t0 $v0");
                    yield Type.BOOLEAN;
                }
                case "or" -> {
                    if (t1 != Type.BOOLEAN || t2 != Type.BOOLEAN) throw new RuntimeException();
                    e.emit("or $v0 $t0 $v0");
                    yield Type.BOOLEAN;
                }
                default -> super.compile(e);
            };
        }
        catch (RuntimeException err)
        {
            // throw RuntimeException above for brevity and catch here for better error message
            throw new IllegalArgumentException("Incompatible operand types for " +
                    op + " in " + this);
        }
    }

    /**
     * Prints the Expression as a String.
     * @return String representation of the Expression
     */
    @Override
    public String toString()
    {
        return exp1.toString() + " " + op + " " + exp2.toString();
    }
}
