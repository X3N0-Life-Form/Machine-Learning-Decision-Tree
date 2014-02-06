package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import compute.Compute;
import compute.Requirements;
import core.INode;
import core.Leaf;
import core.Matrix;
import core.Node;

/**
 * This is the class that is going to generate our decision tree.
 * @author Sara Tari & Adrien Droguet
 *
 */
public class Engine {
	///////////////////
	//// Constants ////
	///////////////////
	
	public static final String DEFAULT_FILE_NAME = "resources/data/weather.nominal.arff";
	public static final int DEFAULT_MAX_DEPTH = 100;
	public static final double DEFAULT_IMPURITY = 0.05;
	
	///////////////////
	//// Variables ////
	///////////////////
	
	private static int maxDepth = DEFAULT_MAX_DEPTH;
	private static double impurity = DEFAULT_IMPURITY;
	private static String fileName = "resources/data/prime.arff";
	
	/**
	 * Root node, used by Engine and Main.
	 */
	protected static Node root = null;
	
	///////////////////////////
	//// Getters / Setters ////
	///////////////////////////
	
	public static int getMaxDepth() {
		return maxDepth;
	}
	
	/**
	 * Sets the max depth and makes sure its >= 1
	 * @param maxDepth
	 */
	public static void setMaxDepth(int maxDepth) {
		Engine.maxDepth = maxDepth;
		if (Engine.getMaxDepth() < 1) {
			Engine.maxDepth = 1;
		}
	}
	
	public static String getFileName() {
		return fileName;
	}
	
	public static void setFileName(String fileName) {
		Engine.fileName = fileName;
	}
	
	public static double getImpurity() {
		return impurity;
	}
	
	/**
	 * Sets the impurity value and makes sure its >= 0 & <= 1.
	 * @param impurity
	 */
	public static void setImpurity(double impurity) {
		Engine.impurity = impurity;
		if (Engine.impurity < 0) {
			Engine.impurity = 0;
		} else if (Engine.impurity > 1) {
			Engine.impurity = 1;
		}
	}
	
	/////////////////
	//// Methods ////
	/////////////////

	/**
	 * Creates either {@link Node} stubs of {@link Leaf} objects linked to the specified Node,
	 * based on depth, purity and entropy.
	 * @param node
	 * @param matrix
	 * @param currentDepth
	 */
	public static void createSons(Node node, Matrix matrix, int currentDepth) {
		List<String> values = matrix.getValidValues().get(node.getAttribute());
		for (String currentValue : values) {
			if (node.getProportions().get(currentValue) != 0.0) {
				
				if (node.getEntropies().get(currentValue) == 0.0) {
					
					String classValue = matrix.getFirstClassValue(node.getAttribute(), currentValue, node.getRequired());
					node.addSon(currentValue, new Leaf(node, classValue, currentValue));
					
				} else if (currentDepth == Engine.maxDepth - 1 || node.isPureEnough(Engine.impurity)) {
					
					String classValue = matrix.getDominantClassValue(node.getAttribute(), currentValue, node.getRequired());
					node.addSon(currentValue, new Leaf(node, classValue, currentValue));
					
				} else {
					Node son = new Node("TBD", node);
					son.addRequired(node.getAttribute(), currentValue);
					node.addSon(currentValue, son);
				}
			}
		}
	}

	/**
	 * Creates {@link Node}s and prepare them properly (calculate entropy, etc.) in order to be
	 * able to choose the best.
	 * @param matrix
	 * @param skeletonWarriorNode set to null for first node
	 * @return List of created Nodes.
	 */
	public static List<Node> createNodes(Matrix matrix, Node skeletonWarriorNode) {
		ArrayList<Node> nodeList = null;
		int totalPositive = 0;
		int total = 0;
		if (skeletonWarriorNode == null) {
			nodeList = new ArrayList<Node>(matrix.getAttributes().length);
			total = matrix.getData().length;
			for (String[] row : matrix.getData()) {
				if (row[row.length - 1].equals(matrix.getPositiveClass())) {
					totalPositive++;
				}
			}
			
		} else {
			nodeList = new ArrayList<Node>(matrix.getAttributes().length - skeletonWarriorNode.getRequired().size());
			for (String[] row : matrix.getData()) { // partially copy pasted from Compute.proportions()
				if (Requirements.hasRequiredValues(row, skeletonWarriorNode.getRequired(), matrix)) {
					total++;
					if (row[row.length - 1].equals(matrix.getPositiveClass())) {
						totalPositive++;
					}
				}
			}
		}
		
		int totalNegative = total - totalPositive;
		
		Map<String, String> requiredValues = null;
		if (skeletonWarriorNode != null) {
			requiredValues = skeletonWarriorNode.getRequired();
		}
		for (String currentAttribute : matrix.getAttributes()) {
			if (currentAttribute.equals(matrix.getClassAttribute())){
				break;
			} else if (requiredValues != null && requiredValues.containsKey(currentAttribute)) {
				continue;
			}
			Node node = new Node(currentAttribute);
			node.setTotalPositives(totalPositive);
			node.setTotalNegatives(totalNegative);
			node.setEntropy(Compute.calculateEntropy(totalPositive, totalNegative, total));
			
			for (String currentValue : matrix.getValidValues().get(currentAttribute)) {
				node.addEntropy(currentValue, Compute.entropy(currentAttribute, currentValue, matrix, requiredValues));
				node.addProportion(currentValue, Compute.proportions(node, matrix, currentValue, requiredValues));
			}
			
			nodeList.add(node);
		}
		return nodeList;
	}

	/**
	 * Choose the best node amongst a list of nodes, according to their individual gains.
	 * @param nodeList
	 * @param matrix
	 * @return Best Node within the provided List.
	 */
	public static Node chooseBest(List<Node> nodeList, Matrix matrix){
		Node tempNode = null;
		double tempGain = 0;
		for (Node currentNode : nodeList){
			double currentGain = Compute.gain(currentNode, matrix);
			if (tempNode == null){
				tempNode = currentNode;
				tempGain = currentGain ;
			}
			else if (currentGain > tempGain){
				tempNode = currentNode;
				tempGain = currentGain ;
			}
		}		
		return tempNode;
	}

	/**
	 * Recursive method that builds our decision tree. Note that the root must have been initialized,
	 * ie. node create, best node chosen and sons created.
	 * @param matrix
	 * @param currentNode
	 * @param currentDepth
	 */
	public static void buildTree(Matrix matrix, Node currentNode, int currentDepth) {
		for (String key : currentNode.getSons().keySet()) {
			INode son = currentNode.getSons().get(key);
			if (!(son instanceof Leaf)) {
				List<Node> nodes = createNodes(matrix, (Node) son);
				Node best = chooseBest(nodes, matrix);
				((Node) son).fusion(best);
			}
		}
		
		for (String key : currentNode.getSons().keySet()) {
			INode son = currentNode.getSons().get(key);
			if (!(son instanceof Leaf)) {
				createSons((Node) son, matrix, currentDepth);
				buildTree(matrix, (Node) son, currentDepth + 1);
			}
		}
	}
	
}
