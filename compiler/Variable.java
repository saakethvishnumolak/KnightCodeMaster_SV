package compiler;

/**
 * Holds the variable for the symbol table
 * 
 * @author Saaki Vishnumolakala
 * @version 1.0
 * Compiler Assignment 4
 * CS322 Compiler Construction
 * Spring 2023
 */
public class Variable {
    public String name;
    public String type;
    public int value;

    public Variable()
    {
        name = "";
        type = "";
        value = 0;
    }

    public Variable(String name, String type, int value)
    {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    /**
     * Setters
     */
    public void setName(String name)
    {
        this.name = name;
    }

    public void setType(String type)
    {
        this.type = type; 
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    /**
     * Getters
     */
    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public int getValue()
    {
        return value;
    }

}
