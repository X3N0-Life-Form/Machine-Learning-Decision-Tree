package core;

public class Leaf implements INode {

	private boolean zorg;
	
	public Leaf(boolean zorg) {
		this.zorg = zorg;
	}
	
	public boolean isZorg() {
		return zorg;
	}
}
