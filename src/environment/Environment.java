package environment;

import java.util.HashMap;
import java.util.Map;

/**
 * The Environment class remembers the values of variables.
 * @author Nelson Gou
 * @version 10/17/23
 */
public class Environment {
    private Map<String, Object> variables;

    /**
     * Initializes the variables Map as a HashMap.
     */
    public Environment()
    {
        variables = new HashMap<>();
    }

    /**
     * Sets a variable to an Integer value.
     * @param variable the variable to set
     * @param value the Integer value
     */
    public void setVariable(String variable, Object value)
    {
        variables.put(variable, value);
    }

    /**
     * Gets the value of a variable.
     * @precondition the variable must already be set in the Environment
     * @param variable the name of the variable
     * @return the Integer value
     */
    public Object getVariable(String variable)
    {
        return variables.get(variable);
    }
}
