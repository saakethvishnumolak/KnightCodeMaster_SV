package compiler;

/**
 * Description: 
 * @author Justin Mattix
 * 
 * @version 13
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
	
	public boolean isNull() {
		if(name.equals(null))
			return true;
		else
			return false;
	}
	
	public int getIndex() {
		return index;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	int index;
	String name;
	String type;
}
