package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import parse.Parser;

import compute.Compute;
import core.INode;
import core.Leaf;
import core.Matrix;
import core.Node;

/**
 * Main.
 * @author etudiant
 *
 */
public class Main {
	
	public static Node root = null;
	public static final String testFile = "resources/data/weather.nominal.arff";
	//TODO read that off the command line
	public static int maxDepth;
	public static String fileName;
	public static double impurity;

	public static void main(String[] args) throws IOException {
		//TODO handle args
		
		//Map <String, String> req = new TreeMap<String, String>();
		Matrix matrix = Parser.parseFile(testFile);
		List<Node> firstNodes = createNodes(matrix, root);
		root = chooseBest(firstNodes, matrix);
		System.out.println(" the chosen root is : " + root.getAttribute());
		
		createSons(root, matrix);
		for (String key : root.getSons().keySet()) {
			INode son = root.getSons().get(key);
			if (!(son instanceof Leaf)) {
				List<Node> nodes = createNodes(matrix, (Node) son);
				Node best = chooseBest(nodes, matrix);
				System.out.println("print de batard de la mort qui tue -->" + best.getAttribute());
			}
		}
	}

	public static void createSons(Node node, Matrix matrix) {
		List<String> values = matrix.getValidValues().get(node.getAttribute());
		for (String currentValue : values) {
			if (node.getProportions().get(currentValue) != 0.0) {
				if (node.getEntropies().get(currentValue) == 0.0) {
					boolean classValue = matrix.getFirstClassValue(node.getAttribute(), currentValue);
					node.addSon(currentValue, new Leaf(node, classValue));
				} else {
					Node son = new Node("TBD", node);
					son.addRequired(node.getAttribute(), currentValue);
					node.addSon(currentValue, son);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param matrix
	 * @param skeletonWarriorNode set to null for first node
	 * @return
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
				if (Compute.hasRequiredValues(row, skeletonWarriorNode.getRequired(), matrix)) {
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
	 * Choose the best node amongst a list of nodes.
	 * @param nodeList
	 * @param matrix
	 * @return
	 */
	public static Node chooseBest(List<Node> nodeList, Matrix matrix){
		Node tempNode = null;
		double tempGain = 0;
		for (Node currentNode : nodeList){
			double currentGain = Compute.gain(currentNode, matrix);
			System.err.println("" + currentNode.getAttribute());
			System.err.println(" ----- " + currentGain);
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
	
}
