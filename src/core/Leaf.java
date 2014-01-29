package core;

public class Leaf implements INode {

	private boolean classValue;
	private Node father;
	
	public Leaf(boolean ok) {
		this.classValue = ok;
	}
	

	public Leaf(Node father, boolean classValue) {
		this.father = father;
		this.classValue = classValue;
	}

	public boolean isOk() {
		return classValue;
	}
}
