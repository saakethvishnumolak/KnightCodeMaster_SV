package compiler;

public class Variable {
	public Variable(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	public int getIndex() {
		return index;
	}
	public String getName() {
		return name;
	}
	
	int index;
	String name;
}
