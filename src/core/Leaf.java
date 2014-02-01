package core;

public class Leaf implements INode {

	private String classValue;
	private Node father;
	private String attributeValue;
	

	public Leaf(Node father, String classValue, String attributeValue) {
		this.father = father;
		this.classValue = classValue;
		this.attributeValue = attributeValue;
	}


	@Override
	public String ourToString(int depth) {
		String tab = "";
		for (int i = 0; i < depth; i++) {
			tab += "\t";
		}
		return tab + "Class Value = " + classValue + " for attribute " + father.getAttribute() + " with value " + attributeValue;
	}


	@Override
	public boolean isComplete() {
		return true;
	}
}
