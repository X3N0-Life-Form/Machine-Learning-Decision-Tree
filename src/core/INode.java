package core;

/**
 * Node interface, represents either another node or a leaf.
 * @author Adrien Droguet & Sara Tari
 *
 */
public interface INode {

	/**
	 * Customized toString() method.
	 * @param depth
	 * @return A correctly indented String representing the INode and its sons.
	 */
	public String ourToString(int depth);

	/**
	 * Method checking whether an INode is complete.
	 * @return True if every branch ends with a {@link Leaf}.
	 */
	public boolean isComplete();

}
