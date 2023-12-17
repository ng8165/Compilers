package ast;

import java.util.Scanner;

import emitter.Emitter;
import environment.Environment;

/**
 * The Readln class represents a READLN statement, which reads an integer from user input.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Readln extends Statement
{
    private final String var;
    private final Scanner sc;

    /**
     * Initializes the var instance variable and creates a Scanner for reading user input.
     * @param var the variable name to store user input in
     */
    public Readln(String var)
    {
        this.var = var;
        sc = new Scanner(System.in);
    }

    /**
     * Reads the user input and sets it in a variable in the Environment.
     * @param env the Environment
     */
    @Override
    public void exec(Environment env)
    {
        env.setVariable(var, sc.nextInt());
    }

    /**
     * Compiles a Readln Statement by emitting the syscall to read integer from user input,
     * then saving the input into var.
     * @param e the Emitter
     * @throws IllegalArgumentException if var is not an integer variable
     */
    @Override
    public void compile(Emitter e)
    {
        if (var == null) // for empty READLN, read string and discard
        {
            e.emit("li $v0 8\t# read string from user input");
            e.emit("la $a0 readBuffer");
            e.emit("li $a1 128\t# max length of inputted string");
            e.emit("syscall");
            return;
        }

        switch (e.getVariableType(var))
        {
            case INTEGER -> {
                e.emit("li $v0 5\t# get user integer input and store in " + var);
                e.emit("syscall");
            }
            case STRING -> {
                e.emitPush("$ra");
                e.emit("jal readstr\t# get user string input in $v0");
                e.emitPop("$ra");
            }
            default -> throw new IllegalArgumentException("READLN only takes integers and strings");
        }

        if (e.isLocalVariable(var))
            e.emit("sw $v0 " + e.getOffset(var) + "($sp)\t# save $v0 into local variable");
        else
            e.emit("sw $v0 var" + var + "\t# load accumulator into " + var);
    }
}
