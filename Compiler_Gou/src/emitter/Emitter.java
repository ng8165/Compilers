package emitter;

import ast.Type;
import ast.VariableDeclaration;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * The Emitter class writes files line-by-line through emit calls.
 * @author Nelson Gou
 * @version 11/9/23
 */
public class Emitter
{
    private final PrintWriter out;
    private Map<String, Type> variableTypes;
    private int labelID;

    /**
     * Creates an emitter for writing to a new file with given name.
     * @param outputFileName the name of the output file
     */
    public Emitter(String outputFileName, List<VariableDeclaration> variables)
    {
        try
        {
            out = new PrintWriter(new FileWriter(outputFileName), true);
            labelID = 0;
            variableTypes = new HashMap<>();
            for (VariableDeclaration variable: variables)
                variableTypes.put(variable.getName(), variable.getType());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the Type of a global variable.
     * @param name the name of the variable
     * @return the Type of the variable
     * @throws IllegalArgumentException if the variable does not exist
     */
    public Type getVariableType(String name)
    {
        if (!variableTypes.containsKey(name))
            throw new IllegalArgumentException(name + " was not declared as a global variable");

        return variableTypes.get(name);
    }

    /**
     * Emits an empty line (just a new line character).
     */
    public void emit()
    {
        out.println();
    }

    /**
     * Emits one line of code to file (with non-labels indented).
     * @param code the line of code to add to the file
     */
    public void emit(String code)
    {
        if (code.matches("\s*#.*") || !code.matches(".*:\s*(\\..*)?"))
            code = "\t" + code;
        out.println(code);
    }

    /**
     * Emits one line of code to file. Tabs if tab is true.
     * @param code the line of code to add to the file
     * @param tab if true, places a tab; otherwise, there is no tab
     */
    public void emit(String code, boolean tab)
    {
        out.println((tab ? "\t" : "") + code);
    }

    /**
     * Emits code that pushes the value of reg into the stack.
     * @param reg the register to push into the stack
     */
    public void emitPush(String reg)
    {
        emit("subu $sp $sp 4\t# push " + reg + " into stack");
        emit("sw " + reg + " ($sp)");
    }

    /**
     * Emits code that pops the stack and puts the popped value into reg.
     * @param reg the register to store the popped stack value
     */
    public void emitPop(String reg)
    {
        emit("lw " + reg + " ($sp)\t# pop stack into " + reg);
        emit("addu $sp $sp 4");
    }

    /**
     * Returns the next available label ID to avoid conflicting label names.
     * Also updates the ID for the next time it is called.
     * @return the next label ID
     */
    public int nextLabelID()
    {
        return labelID++;
    }

    /**
     * Closes the file. Should be called after all calls to emit.
     */
    public void close()
    {
        out.close();
    }
}