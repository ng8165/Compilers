package environment;

import java.util.HashMap;
import java.util.Map;

import ast.ProcedureDeclaration;

/**
 * The Environment class stores the values of variables and procedures
 * based on a String identifier.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Environment
{
    private Environment parent;
    private final Map<String, Object> variables;
    private final Map<String, ProcedureDeclaration> procedures;

    /**
     * Creates a root Environment.
     * Instantiates the Maps as HashMaps and sets the parent to null.
     */
    public Environment()
    {
        variables = new HashMap<>();
        procedures = new HashMap<>();
        parent = null;
    }

    /**
     * Creates a child Environment.
     * Instantiates the Maps and sets the parent as the root Environment.
     * @param env another Environment (if root, sets env to this parent;
     *            if a child, sets env's parent to this parent)
     */
    public Environment(Environment env)
    {
        this();
        this.parent = (env.parent == null ? env : env.parent);
    }

    /**
     * Declares a variable in the current Environment.
     * @param variable the name of the variable
     * @param value the value of the variable
     */
    public void declareVariable(String variable, Object value)
    {
        variables.put(variable, value);
    }

    /**
     * Sets a variable to a value.
     * If this Environment is a child environment, does not have the variable set in itself,
     * but has the variable in the root, the root variable is directly modified.
     * In all other cases, the variable is set in the child environment.
     * @param variable the variable to set
     * @param value the Integer value
     */
    public void setVariable(String variable, Object value)
    {
        if (parent != null &&
                !variables.containsKey(variable) && parent.variables.containsKey(variable))
            parent.declareVariable(variable, value);
        else
            declareVariable(variable, value);
    }

    /**
     * Gets the value of a variable.
     * Checks the current Environment. If it is not present, checks the root Environment.
     * @param variable the name of the variable
     * @throws IllegalArgumentException if the variable is not defined
     * @return the value
     */
    public Object getVariable(String variable)
    {
        if (variables.containsKey(variable))
            return variables.get(variable);
        else if (parent != null)
            return parent.getVariable(variable);

        throw new IllegalArgumentException(variable + " is not defined");
    }

    /**
     * Sets a procedure to its corresponding Statement.
     * The procedure will be set in the root Environment.
     * @param id the name of the procedure
     * @param procedure the ProcedureDeclaration of the procedure
     */
    public void setProcedure(String id, ProcedureDeclaration procedure)
    {
        if (parent == null)
            procedures.put(id, procedure);
        else
            parent.setProcedure(id, procedure);
    }

    /**
     * Gets the ProcedureDeclaration of a procedure.
     * Looks for the procedure in the root Environment.
     * @precondition the procedure's ID must already be set in the Environment
     * @param id the name of the procedure
     * @return the ProcedureDeclaration
     */
    public ProcedureDeclaration getProcedure(String id)
    {
        if (parent == null)
            return procedures.get(id);
        else
            return parent.getProcedure(id);
    }
}
