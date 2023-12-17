package emitter;

import ast.*;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

/**
 * The Emitter class writes files line-by-line through emit calls.
 * @author Nelson Gou
 * @version 11/9/23
 */
public class Emitter
{
    private final PrintWriter out;
    private final Map<String, Type> globalVariableTypes;
    private final Map<String, ProcedureDeclaration> procedureDeclarations;
    private ProcedureDeclaration procedureContext;
    private int labelID;
    private int excessStackHeight;
    private final Stack<String> loopIDs;

    /**
     * Creates an emitter for writing to a new file with given name.
     * @param outputFileName the name of the output file
     * @param variables Map of VariableDeclarations from which to extract global variable Types
     * @param procedures Map of ProcedureDeclarations from which to extract procedure return Types
     */
    public Emitter(String outputFileName, List<VariableDeclaration> variables,
                   List<ProcedureDeclaration> procedures)
    {
        try
        {
            out = new PrintWriter(new FileWriter(outputFileName), true);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        labelID = 0;
        excessStackHeight = 0;
        globalVariableTypes = new HashMap<>();
        procedureDeclarations = new HashMap<>();
        for (VariableDeclaration variable: variables)
            globalVariableTypes.put(variable.getName(), variable.getType());
        for (ProcedureDeclaration procedure: procedures)
            procedureDeclarations.put(procedure.getName(), procedure);
        loopIDs = new Stack<>();
    }

    /**
     * Sets proc as the current Procedure context.
     * @param proc new Procedure context
     */
    public void setProcedureContext(ProcedureDeclaration proc)
    {
        procedureContext = proc;
        excessStackHeight = 0;
    }

    /**
     * Clears the current Procedure context by making the context null.
     */
    public void clearProcedureContext()
    {
        procedureContext = null;
    }

    /**
     * Gets the Type of a variable. If the variable is local, uses the
     * current Procedure context to find the type. Otherwise, uses the
     * Map of global variables to find the Type.
     * @param name the name of the variable
     * @return the Type of the variable
     * @throws IllegalArgumentException if the variable does not exist
     */
    public Type getVariableType(String name)
    {
        Type t = getLocalVariableType(name);
        if (t != null) return t;

        if (!globalVariableTypes.containsKey(name))
            throw new IllegalArgumentException(name + " was not declared as a variable");

        return globalVariableTypes.get(name);
    }

    /**
     * Returns true if varName is a local variable in the current Procedure and
     * false if varName is not in the Procedure. Returns false if there is no current Procedure.
     * @param varName variable name to check
     * @return true if a local variable; false if not
     */
    public boolean isLocalVariable(String varName)
    {
        return getLocalVariableType(varName) != null;
    }

    /**
     * Returns the variable Type if varName is a local variable or a parameter
     * in the current Procedure and null if varName is not in the Procedure.
     * Returns null if there is no current Procedure.
     * @param varName variable name to check
     * @return the Type of the local variable; if not a local variable, returns null
     */
    private Type getLocalVariableType(String varName)
    {
        if (procedureContext == null)
            return null;

        if (procedureContext.getReturnType() != null && procedureContext.getName().equals(varName))
            return procedureContext.getReturnType();

        // check local variables and parameters
        Type isLocalVar = getLocalVar(varName);
        if (isLocalVar != null) return isLocalVar;
        return getParam(varName);
    }

    /**
     * Gets the Type of a Procedure parameter.
     * Returns null if the parameter is not present.
     * @param paramName name of the parameter to check
     * @return the Type of the parameter; if no parameter, returns null
     */
    private Type getParam(String paramName)
    {
        for (VariableDeclaration param: procedureContext.getParams())
            if (param.getName().equals(paramName))
                return param.getType();

        return null;
    }

    /**
     * Gets the Type of a Procedure local variable.
     * Returns null if the local variable is not present.
     * @param varName name of the local variable to check
     * @return the Type of the local variable; if no local variable, returns null
     */
    private Type getLocalVar(String varName)
    {
        for (VariableDeclaration localVar: procedureContext.getLocalVars())
            if (localVar.getName().equals(varName))
                return localVar.getType();

        return null;
    }

    /**
     * Gets the return Type of the Procedure (integer, string, boolean, or null/void).
     * If the procedure's name is invalid, throws an error.
     * @param name name of the procedure to find the return Type
     * @return return Type of the specified procedure
     */
    public ProcedureDeclaration getProcedureDeclaration(String name)
    {
        if (!procedureDeclarations.containsKey(name))
            throw new IllegalArgumentException("procedure name " + name + " is invalid");

        return procedureDeclarations.get(name);
    }

    /**
     * Returns the offset from the current $sp to retrieve the variable in
     * the current Procedure context.
     * @param varName the name of the variable to find the offset
     * @return $sp offset to represent varName
     */
    public int getOffset(String varName)
    {
        List<VariableDeclaration> params = procedureContext.getParams(),
                localVars = procedureContext.getLocalVars();

        // first check local variables
        for (int i=0; i<localVars.size(); i++)
            if (localVars.get(i).getName().equals(varName))
                return 4 * (localVars.size() - i - 1) + excessStackHeight;

        // then check if is a return value
        if (procedureContext.getReturnType() != null && procedureContext.getName().equals(varName))
            return 4 * localVars.size() + excessStackHeight;

        // then check if is a parameter
        for (int i=0; i<params.size(); i++)
            if (params.get(i).getName().equals(varName))
                return 4 * (localVars.size() + params.size() - i) + excessStackHeight;

        throw new RuntimeException(varName + " is not a local variable or parameter");
    }

    /**
     * Gets the label ID of the most recent loop. Used for break/continue
     * statements to generate the correct label.
     * @return the most recent loop's label ID
     */
    public String getLoopID()
    {
        return loopIDs.peek();
    }

    /**
     * Pushes a loop's label ID onto the loopIDs stack.
     * Used whenever a loop is encountered so that break/continue Statements
     * within the loop know which label to jump to.
     * @param id the label ID to push
     */
    public void pushLoopID(String id)
    {
        loopIDs.push(id);
    }

    /**
     * Pops a loop's label ID from the loopIDs stack.
     * Used whenever a loop ends to maintain the loopIDs stack.
     * See pushLoopID or peekLoopID documentation for more details.
     */
    public void popLoopID()
    {
        loopIDs.pop();
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
        excessStackHeight += 4;
    }

    /**
     * Emits code that pops the stack and puts the popped value into reg.
     * @param reg the register to store the popped stack value
     */
    public void emitPop(String reg)
    {
        emit("lw " + reg + " ($sp)\t# pop stack into " + reg);
        emit("addu $sp $sp 4");
        excessStackHeight -= 4;
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