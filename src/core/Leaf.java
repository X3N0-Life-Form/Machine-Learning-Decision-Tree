package core;

public class Leaf implements INode {

	private boolean ok;
	
	public Leaf(boolean ok) {
		this.ok = ok;
	}
	

	public Leaf(Node father) {
		// TODO Auto-generated constructor stub
	}

	public boolean isOk() {
		return ok;
	}
}
