package ast;

import environment.Environment;

/**
 * The VariableDeclaration class represents a variable declaration.
 * This can occur in the global space, within the main block, or within procedures.
 * @author Nelson Gou
 * @version 11/15/23
 */
public class VariableDeclaration extends Statement
{
    private final String name;
    private final Type type;

    /**
     * Instantiates a VariableDeclaration by setting a name and a type.
     * Takes in the type as a String and matches it to a Type.
     * @param name variable name
     * @param type variable type, as String
     */
    public VariableDeclaration(String name, String type)
    {
        this.name = name;

        switch (type.toLowerCase()) {
            case "integer" -> this.type = Type.INTEGER;
            case "boolean" -> this.type = Type.BOOLEAN;
            case "string" -> this.type = Type.STRING;
            default -> throw new RuntimeException(type + " is not a valid variable type");
        }
    }

    /**
     * Getter for the variable name in the VariableDeclaration.
     * @return variable name as a String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Getter for the variable Type in the VariableDeclaration.
     * @return type of the variable
     */
    public Type getType()
    {
        return type;
    }

    /**
     * Executes the VariableDeclaration by setting it in the Environment.
     * @param env the Environment
     */
    @Override
    public void exec(Environment env)
    {
        env.setVariable(name, null);
    }
}
