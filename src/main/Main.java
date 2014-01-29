package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import parse.Parser;

import compute.Compute;
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
		List<Node> firstNodes = createFirstNodes(matrix);
		root = chooseBest(firstNodes, matrix);
		System.out.println(" the chosen root is : " + root.getAttribute());
		/*for (String currentAttribute : root.getEntropies().keySet()){
			if (root.getEntropies().get(currentAttribute) > 0){
				
			}
		}*/
		
		createSons(root, matrix);
		
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

	public static List<Node> createFirstNodes(Matrix matrix) {
		ArrayList<Node> nodeList = new ArrayList<Node>(matrix.getAttributes().length);
		int total = matrix.getData().length;
		int totalPositive = 0;
		for (String[] row : matrix.getData()) {
			if (row[row.length - 1].equals(matrix.getPositiveClass())) {
				totalPositive++;
			}
		}
		int totalNegative = total - totalPositive;
		
		for (String currentAttribute : matrix.getAttributes()) {
			if (currentAttribute.equals(matrix.getClassAttribute())){
				break;
			}
			Node node = new Node(currentAttribute);
			node.setTotalPositives(totalPositive);
			node.setTotalNegatives(totalNegative);
			node.setEntropy(Compute.calculateEntropy(totalPositive, totalNegative, total));
			
			for (String currentValue : matrix.getValidValues().get(currentAttribute)) {
				node.addEntropy(currentValue, Compute.entropy(currentAttribute, currentValue, matrix, null));
				node.addProportion(currentValue, Compute.proportions(node, matrix, currentValue, null));
			}
			
			nodeList.add(node);
		}
		return nodeList;
	}
	
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
