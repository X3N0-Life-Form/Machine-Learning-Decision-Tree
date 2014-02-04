package core;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class representing a decision tree node.
 * @author Adrien Droguet & Sara Tari
 *
 */
public class Node implements INode {
	
	////////////////////
	//// Attributes ////
	////////////////////

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
	
	//////////////////////
	//// Constructors ////
	//////////////////////
	
	/**
	 * Constructs an empty Node for the specified attribute.
	 * @param attribute
	 */
	public Node(String attribute) {
		this.setAttribute(attribute);
	}
	
	/**
	 * Constructs an empty Node with a father for the specified attribute.
	 * @param attribute
	 * @param father
	 */
	public Node(String attribute, Node father) {
		this(attribute);
		this.setFather(father);
		if (father.required != null) {
			this.required = new TreeMap<String, String>(father.required);
		} else {
			this.required = new TreeMap<String, String>();
		}
	}
	
	///////////////////////////
	//// Getters / Setters ////
	///////////////////////////

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
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
	
	////////////////
	//// Adders ////
	////////////////
	
	public void addEntropy(String value, double ent) {
		entropies.put(value, ent);
	}
	
	public void addSon(String value, INode son) {
		sons.put(value, son);
	}
	
	public void addProportion(String value, double proportion) {
		proportions.put(value, proportion);
	}

	public void addRequired(String attribute, String currentValue) {
		required.put(attribute, currentValue);
	}
	
	//////////////////
	///// Methods ////
	//////////////////
	
	@Override
	public String toString() {
		return ourToString(0);
	}
	
	@Override
	public String ourToString(int depth) {
		String tab = "";
		for (int i = 0; i < depth; i++) {
			tab += "\t";
		}
		String temp = tab + "Attribute = " + attribute + "; Entropy = " + entropy;
		for (String currentSon : sons.keySet()) {
			INode actualSon = sons.get(currentSon);
			temp += "\n" + actualSon.ourToString(depth + 1);
		}
		return temp;
	}
	
	/**
	 * Fusion dance. Copy another node's most important attributes.
	 * @param best
	 */
	public void fusion(Node best) {
		this.attribute = best.attribute;
		this.entropies = best.entropies;
		this.entropy = best.entropy;
		this.proportions = best.proportions;
		this.totalPositives = best.totalPositives;
		this.totalNegatives = best.totalNegatives;
	}

	@Override
	public boolean isComplete() {
		if (sons == null || sons.size() == 0) {
			return false;
		}
		for (String currentSon : sons.keySet()) {
			INode son = sons.get(currentSon);
			if (son.isComplete()) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * Determines whether this Node is pure enough to be accepted.
	 * @param impurity
	 * @return
	 */
	public boolean isPureEnough(double impurity) {
		for (String currentProportion : proportions.keySet()){
			if (1 - proportions.get(currentProportion) <= impurity){
				return true;
			}
		}		
		return false;
	}
}
