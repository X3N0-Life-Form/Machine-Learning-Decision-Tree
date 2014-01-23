package main;

import java.util.ArrayList;
import java.util.List;

import compute.Compute;
import core.Matrix;
import core.Node;

/**
 * Main.
 * @author etudiant
 *
 */
public class Main {
	
	//TODO read that off the command line
	public static int maxDepth;
	public static String fileName;
	public static double impurity;

	public static void main(String[] args) {
		//TODO handle args
	}

	public static List<Node> createFirstNode(Matrix matrix) {
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
			Node node = new Node(currentAttribute);
			node.setTotalPositives(totalPositive);
			node.setTotalNegatives(totalNegative);
			node.setEntropy(Compute.calculateEntropy(totalPositive, totalNegative, total));
			
			for (String currentValue : matrix.getValidValues().get(currentAttribute)) {
				System.out.println(currentValue);
				node.addEntropy(currentValue, Compute.entropy(currentAttribute, currentValue, matrix));
				int totalValue = matrix.getNumberOfExamples(currentAttribute, currentValue);
				System.out.println("total " + totalValue);
				node.addProportion(currentValue, (double) totalValue / (double) total);
			}
			
			nodeList.add(node);
		}
		return nodeList;
	}
}
