package compiler;

public class Variable {
	public Variable(int index, String name, String type) {
		this.index = index;
		this.name = name;
		this.type = type;
	}
	
	public Variable() {
		
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
