package core;

public class Leaf implements INode {

	private boolean ok;
	
	public Leaf(boolean ok) {
		this.ok = ok;
	}
	
	public boolean isOk() {
		return ok;
	}
}
