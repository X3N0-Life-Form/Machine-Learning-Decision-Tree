package core;

import java.util.Map;
import java.util.TreeMap;

public class Node implements INode {

	private String attribute;
	
	/**
	 * Note: keys must be identical to those in sons map.
	 */
	private Map<String, Double> entropies = new TreeMap<String, Double>();
	/**
	 * Note: keys must be identical to those in entropies map.
	 */
	private Map<String, INode> sons = new TreeMap<String, INode>();
	
	/**
	 * Constructs an empty Node for the specified attribute.
	 * @param attribute
	 */
	public Node(String attribute) {
		this.setAttribute(attribute);
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
	public void addEntropy(String value, double ent) {
		entropies.put(value, ent);
	}
	
	public void addSon(String value, INode son) {
		sons.put(value, son);
	}
	
	public Map<String, Double> getEntropies() {
		return entropies;
	}
	
	public Map<String, INode> getSons() {
		return sons;
	}
}
