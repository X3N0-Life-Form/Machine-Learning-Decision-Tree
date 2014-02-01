package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public static final String TEST_FILE = "resources/data/weather.nominal.arff";
	
	private static int maxDepth = 5;
	private static String fileName = TEST_FILE;
	private static double impurity = 0.05;

	public static void main(String[] args) throws IOException {
		
		handleArgs(args);
		printRecap();
		
		Matrix matrix = Parser.parseFile(fileName);
		
		List<Node> firstNodes = createNodes(matrix, root);
		root = chooseBest(firstNodes, matrix);
		createSons(root, matrix, 0);
		
		int currentDepth = 1;
		Node currentNode = root;
		
		hjvo(matrix, currentNode, currentDepth);
		System.out.println(root.ourToString(0));
	}

	public static void printRecap() {
		System.out.println("File:\t" + fileName);
		System.out.println("Max depth:\t" + maxDepth);
		System.out.println("Impurity:\t" + impurity);
	}

	private static void handleArgs(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String currentArg = args[i];
			switch (currentArg) {
			case "-file":
				fileName = args[++i];
				break;
			case "-maxDepth":
				maxDepth = Integer.parseInt(args[++i]);
				if (maxDepth < 1) {
					maxDepth = 1;
				}
				break;
			case "-impurity":
				impurity = Integer.parseInt(args[++i]);
				if (impurity < 0) {
					impurity = 0;
				} else if (impurity > 1) {
					impurity = 1;
				}
			default:
				System.err.println("Unrecognised argument:" + currentArg);
			}
		}
	}

	public static void hjvo(Matrix matrix, Node currentNode, int currentDepth) {
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
				hjvo(matrix, (Node) son, currentDepth + 1);
			}
		}
	}

	public static void createSons(Node node, Matrix matrix, int currentDepth) {
		List<String> values = matrix.getValidValues().get(node.getAttribute());
		for (String currentValue : values) {
			if (node.getProportions().get(currentValue) != 0.0) {
				if (node.getEntropies().get(currentValue) == 0.0) {
					String classValue = matrix.getFirstClassValue(node.getAttribute(), currentValue, node.getRequired());
					node.addSon(currentValue, new Leaf(node, classValue, currentValue));
				} else if (currentDepth == maxDepth - 1 || node.isPureEnough(impurity)) {
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
