package ast;

import emitter.Emitter;
import environment.Environment;

import java.util.List;

/**
 * The Writeln class represents a WRITELN(args) statement and prints.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Writeln extends Statement
{
    private final List<Expression> exprs;
    private final boolean newLine;

    /**
     * This constructor instantiates arguments (a List of Expressions).
     * @param exprs the Expression within the WRITELN statement
     */
    public Writeln(List<Expression> exprs)
    {
        this.exprs = exprs;
        newLine = true;
    }

    /**
     * This constructor instantiates arguments (a List of Expressions).
     * Also takes in a boolean determining if a new line is necessary
     * @param exprs the Expression within the WRITELN statement
     * @param newLine true if a new line is needed; false if not
     */
    public Writeln(List<Expression> exprs, boolean newLine)
    {
        this.exprs = exprs;
        this.newLine = newLine;
    }

    /**
     * Formats the object as a String. (used to print Bools as uppercase instead of lowercase).
     * @param obj the Object to format
     * @return a formatted String representing obj
     */
    protected static String printFormat(Object obj)
    {
        if (obj instanceof Boolean bool)
            return bool ? "TRUE" : "FALSE";
        else
            return obj.toString();
    }

    /**
     * Executes the WRITELN statement by concatenating all arguments
     * and printing it to user output.
     * @param env the Environment
     */
    @Override
    public void exec(Environment env)
    {
        StringBuilder str = new StringBuilder();
        for (Expression expr: exprs)
            str.append(printFormat(expr.eval(env)));
        System.out.println(str);
    }

    /**
     * Compiles the Writeln by compiling each Expression and choosing the right
     * syscall based off of its return type.
     * @param e the Emitter
     */
    @Override
    public void compile(Emitter e)
    {
        for (Expression expr: exprs)
        {
            Type type = expr.compile(e);

            switch (type) {
                case INTEGER -> {
                    e.emit("move $a0 $v0\t# print integer");
                    e.emit("li $v0 1");
                }
                case STRING -> {
                    e.emit("move $a0 $v0\t# print string");
                    e.emit("li $v0 4");
                }
                case BOOLEAN -> {
                    e.emit("move $a0 $v0\t# convert boolean to string");
                    e.emitPush("$ra");
                    e.emit("jal boolToStr");
                    e.emitPop("$ra");
                    e.emit("move $a0 $v0\t# print boolean");
                    e.emit("li $v0 4");
                }
            }

            e.emit("syscall");
        }

        if (newLine)
        {
            e.emit("li $v0 4");
            e.emit("la $a0 newline");
            e.emit("syscall");
        }
    }
}
