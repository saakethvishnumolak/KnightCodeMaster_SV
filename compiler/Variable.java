package compiler;

/**
 * 
 * Description: Uses a hashmap to holds where the variable is in the symbol table.
 * 
 * @author Justin Mattix
 * @author David Jones
 * @author Taden Duerod
 * @version 13.0
 * Programming Project 4
 * CS322 - Compiler Construction
 * Fall 2021
 *
 */

public class Variable {
	public Variable(int index, String name, String type) {
		this.index = index;
		this.name = name;
		this.type = type;
	}
	
	public Variable() {
		this.index = 100;
		this.name = null;
		this.type = null;
	}
	/**
	 * Checks if the name of a variable is null
	 * @return returns true or false based on whether the variable is named null
	 */
	public boolean isNull() {
		if(name.equals(null))
			return true;
		else
			return false;
	}
	/**
	 * Returns the index
	 * @return Returns the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * Returns the name of the variable 
	 * @return name of the variable
	 */
	public String getName() {
		return name;
	}
	/**
	 * Returns the type of the variable
	 * @return Returns the type of the variable
	 */
	public String getType() {
		return type;
	}
	/**
	 * Sets the type of the variable
	 * @param type type of the variable
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	int index;
	String name;
	String type;
}
