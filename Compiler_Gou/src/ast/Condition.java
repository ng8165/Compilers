package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * The Condition class represents a boolean Condition. Consists of a relop
 * (relative operator) and two Expressions.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Condition extends Expression
{
    private final Expression exp1;
    private final String relop;
    private final Expression exp2;

    /**
     * Initializes the instance variables exp1, relop, and exp2.
     * @param exp1 first Expression
     * @param relop boolean operator
     * @param exp2 second Expression
     */
    public Condition(Expression exp1, String relop, Expression exp2)
    {
        this.exp1 = exp1;
        this.relop = relop;
        this.exp2 = exp2;
    }

    /**
     * Evaluates the result of the Condition based on the relop (=, <>, <, >, <=, or >=).
     * @param env the Environment
     * @return true if the Condition is true; false if not.
     */
    @Override
    public Object eval(Environment env)
    {
        try
        {
            return switch (relop)
            {
                case "=" -> exp1.eval(env).equals(exp2.eval(env));
                case "<>" -> !exp1.eval(env).equals(exp2.eval(env));
                case "<" -> (Integer) exp1.eval(env) < (Integer) exp2.eval(env);
                case ">" -> (Integer) exp1.eval(env) > (Integer) exp2.eval(env);
                case "<=" -> (Integer) exp1.eval(env) <= (Integer) exp2.eval(env);
                default -> (Integer) exp1.eval(env) >= (Integer) exp2.eval(env);
            };
        }
        catch (ClassCastException e)
        {
            throw new IllegalArgumentException("Could not evaluate " + this);
        }
    }

    /**
     * Compiles the Condition to return as a boolean. Uses the branch command
     * for the opposite of the relop (for <, uses bge or >=). Called whenever
     * a Condition is evaluated in an Assignment.
     * @param e the Emitter
     * @return the boolean Variable Type
     * @throws IllegalArgumentException if exp1 and exp2's Types do not match the relop
     */
    @Override
    public Type compile(Emitter e)
    {
        // $t0 = exp1, $v0 = exp2
        Type t1 = exp1.compile(e);
        e.emitPush("$v0");
        Type t2 = exp2.compile(e);
        e.emitPop("$t0");

        int id = e.nextLabelID();
        String falseLabel = "condfalse" + id;

        try
        {
            switch (relop)
            {
                case "=" -> {
                    if (t1 == Type.STRING && t2 == Type.STRING)
                    {
                        e.emit("move $a0 $t0");
                        e.emit("move $a1 $v0");
                        e.emitPush("$ra");
                        e.emit("jal strcmp");
                        e.emitPop("$ra");
                        return Type.BOOLEAN;
                    }

                    e.emit("bne $t0 $v0 " + falseLabel);
                }
                case "<>" -> {
                    if (t1 == Type.STRING && t2 == Type.STRING)
                    {
                        e.emit("move $a0 $t0");
                        e.emit("move $a1 $v0");
                        e.emitPush("$ra");
                        e.emit("jal strcmp");
                        e.emitPop("$ra");
                        e.emit("not $v0 $v0");
                        return Type.BOOLEAN;
                    }

                    e.emit("beq $t0 $v0 " + falseLabel);
                }
                case "<" -> {
                    if (t1 != Type.INTEGER || t2 != Type.INTEGER) throw new RuntimeException();
                    e.emit("bge $t0 $v0 " + falseLabel);
                }
                case ">" -> {
                    if (t1 != Type.INTEGER || t2 != Type.INTEGER) throw new RuntimeException();
                    e.emit("ble $t0 $v0 " + falseLabel);
                }
                case "<=" -> {
                    if (t1 != Type.INTEGER || t2 != Type.INTEGER) throw new RuntimeException();
                    e.emit("bgt $t0 $v0 " + falseLabel);
                }
                case ">=" -> {
                    if (t1 != Type.INTEGER || t2 != Type.INTEGER) throw new RuntimeException();
                    e.emit("blt $t0 $v0 " + falseLabel);
                }
                default -> super.compile(e);
            }

            e.emit("li $v0 -1\t# load TRUE if condition is TRUE");
            e.emit("j condend" + id);
            e.emit(falseLabel + ":");
            e.emit("li $v0 0\t# load FALSE if condition is FALSE");
            e.emit("condend" + id + ":");

            return Type.BOOLEAN;
        }
        catch (RuntimeException err)
        {
            // throw RuntimeException above for brevity and catch here for better error message
            throw new IllegalArgumentException("Incompatible operand types in " + this);
        }
    }

    /**
     * Compiles the Condition with a target label. Uses the branch command
     * for the opposite of the relop (for <, uses bge or >=). Used by
     * the If, While, and For Statements.
     * @param e the Emitter
     * @param targetLabel the label denoting the end of the block
     * @throws IllegalArgumentException if the variable types are incompatible
     */
    public void compile(Emitter e, String targetLabel)
    {
        // $t0 = exp1, $v0 = exp2
        Type t1 = exp1.compile(e);
        e.emitPush("$v0");
        Type t2 = exp2.compile(e);
        e.emitPop("$t0");
        String comment = "\t# jump to " + targetLabel + " if condition is not satisfied";

        try
        {
            switch (relop) {
                case "=" -> {
                    if (t1 == Type.STRING && t2 == Type.STRING)
                    {
                        e.emit("move $a0 $t0");
                        e.emit("move $a1 $v0");
                        e.emitPush("$ra");
                        e.emit("jal strcmp");
                        e.emitPop("$ra");
                        e.emit("beqz $v0 " + targetLabel + comment);
                    }
                    else
                        e.emit("bne $t0 $v0 " + targetLabel + comment);
                }
                case "<>" -> {
                    if (t1 == Type.STRING && t2 == Type.STRING)
                    {
                        e.emit("move $a0 $t0");
                        e.emit("move $a1 $v0");
                        e.emitPush("$ra");
                        e.emit("jal strcmp");
                        e.emitPop("$ra");
                        e.emit("bnez $v0 " + targetLabel + comment);
                    }
                    else
                        e.emit("beq $t0 $v0 " + targetLabel + comment);
                }
                case "<" -> {
                    if (t1 != Type.INTEGER || t2 != Type.INTEGER) throw new RuntimeException();
                    e.emit("bge $t0 $v0 " + targetLabel + comment);
                }
                case ">" -> {
                    if (t1 != Type.INTEGER || t2 != Type.INTEGER) throw new RuntimeException();
                    e.emit("ble $t0 $v0 " + targetLabel + comment);
                }
                case "<=" -> {
                    if (t1 != Type.INTEGER || t2 != Type.INTEGER) throw new RuntimeException();
                    e.emit("bgt $t0 $v0 " + targetLabel + comment);
                }
                case ">=" -> {
                    if (t1 != Type.INTEGER || t2 != Type.INTEGER) throw new RuntimeException();
                    e.emit("blt $t0 $v0 " + targetLabel + comment);
                }
                default -> super.compile(e);
            }
        }
        catch (RuntimeException err)
        {
            // throw RuntimeException above for brevity and catch here for better error message
            throw new IllegalArgumentException("Incompatible operand types in " + this);
        }
    }

    /**
     * Prints the Expression as a String.
     * @return String representation of the Expression
     */
    @Override
    public String toString()
    {
        return exp1.toString() + " " + relop + " " + exp2.toString();
    }
}
