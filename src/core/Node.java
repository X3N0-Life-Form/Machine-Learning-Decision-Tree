package core;

import java.util.Map;
import java.util.TreeMap;

public class Node implements INode {

	private String attribute;
	private double entropy;
	private int totalPositives;
	private int totalNegatives;
	/**
	 * Note: First node's father is null.
	 */
	private Node father = null;
	/**
	 * Key = attribute; value = value
	 * Note: First node's required is null.
	 */
	private Map<String, String> required = null;
	
	/**
	 * Note: keys must be identical to those in sons map.
	 */
	private Map<String, Double> entropies = new TreeMap<String, Double>();
	/**
	 * Note: keys must be identical to those in entropies map.
	 */
	private Map<String, INode> sons = new TreeMap<String, INode>();
	private Map<String, Double> proportions = new TreeMap<String, Double>();
	
	/**
	 * Constructs an empty Node for the specified attribute.
	 * @param attribute
	 */
	public Node(String attribute) {
		this.setAttribute(attribute);
	}
	
	public Node(String attribute, Node father) {
		this(attribute);
		this.setFather(father);
		this.required = new TreeMap<String, String>(father.required);
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
	
	public void addProportion(String value, double proportion) {
		proportions.put(value, proportion);
	}
	
	public Map<String, Double> getEntropies() {
		return entropies;
	}
	
	public Map<String, INode> getSons() {
		return sons;
	}

	public int getTotalPositives() {
		return totalPositives;
	}

	public void setTotalPositives(int totalPositives) {
		this.totalPositives = totalPositives;
	}

	public int getTotalNegatives() {
		return totalNegatives;
	}

	public void setTotalNegatives(int totalNegatives) {
		this.totalNegatives = totalNegatives;
	}

	public double getEntropy() {
		return entropy;
	}

	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	public Node getFather() {
		return father;
	}

	public void setFather(Node father) {
		this.father = father;
	}

	public Map<String, Double> getProportions() {
		return proportions;
	}

	public void setProportions(Map<String, Double> proportions) {
		this.proportions = proportions;
	}

	public Map<String, String> getRequired() {
		return required;
	}

	public void setRequired(Map<String, String> required) {
		this.required = required;
	}

	public void addRequired(String attribute, String currentValue) {
		required.put(attribute, currentValue);
	}
}
