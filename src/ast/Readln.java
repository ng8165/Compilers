package ast;

import java.util.Scanner;
import environment.Environment;

/**
 * The Readln class represents a READLN statement.
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
}
