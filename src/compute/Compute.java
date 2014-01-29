package compute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.Matrix;
import core.Node;

public class Compute {

	/**
	 * Don't instantiate this class.
	 */
	private Compute() {}
	
	/**
	 * Calculates the entropy of the specified attribute and value.
	 * @param attribute
	 * @param value
	 * @param matrix
	 * @param requiredValues set to null if it isn't required.
	 * @return
	 */
	public static double entropy(String attribute, String value, Matrix matrix, Map<String, String> requiredValues) {
		int attributeIndex = matrix.getAttributeIndex(attribute);
		
		int valueCount = 0;
		int positiveCount = 0;
		int negativeCount = 0;
		String o = matrix.getPositiveClass();
		for (String[] row : matrix.getData()) {
			if (row[attributeIndex].equals(value) && hasRequiredValues(row, requiredValues, matrix)) {
				valueCount++;
				
				if (row[row.length - 1].equals(o)) {
					positiveCount++;
				} else {
					negativeCount++;
				}
			}
		}
		
		return calculateEntropy(positiveCount, negativeCount, valueCount);
	}
	
	/**
	 * 
	 * @param row
	 * @param requiredValues
	 * @param matrix
	 * @return boolean true if the row contains all the required values.
	 */
	public static boolean hasRequiredValues(String[] row, Map<String, String> requiredValues, Matrix matrix) {
		if (requiredValues == null) {
			return true;
		}
		for (String currentAttribute : requiredValues.keySet()) {
			int i = matrix.getAttributeIndex(currentAttribute);
			if (!row[i].equals(requiredValues.get(currentAttribute))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Generic entropy calculation method.
	 * @param positiveCount
	 * @param negativeCount
	 * @param valueCount
	 * @return
	 */
	public static double calculateEntropy(int positiveCount, int negativeCount, int valueCount) {
		if (positiveCount == negativeCount) {
			return 1.0;
		} else if (positiveCount == 0 || negativeCount == 0) {
			return 0.0;
		} else {
			double pp = (double) positiveCount / (double) valueCount;
			double pm = (double) negativeCount / (double) valueCount;
			return - pp * (Math.log(pp)/Math.log(2)) - pm * (Math.log(pm)/Math.log(2));
		}
	}
	
	/**
	 * Calculates the gain for the specified attributes.
	 * Note that entropies must have been calculated beforehand.
	 * @param attribute
	 * @param node
	 * @return
	 */
	public static double gain(Node node, Matrix matrix) {
		double entropy = node.getEntropy();
		System.out.println("Attribute "+ node.getAttribute() + " entropy " + node.getEntropy());
		
		for (String currentValue : matrix.getValidValues().get(node.getAttribute())) {
			System.out.println("currentValue  " + currentValue);
		//	System.out.println("old entropy " + entropy);
			System.out.println(" currententropy  " + node.getEntropies().get(currentValue));
		//	System.out.println("currentProportion " + node.getProportions().get(currentValue) );
			entropy -= node.getEntropies().get(currentValue) * node.getProportions().get(currentValue);
		//	System.out.println("new entropy  " + entropy);
		}
		// Note: entropy is now equal to our gain.
	
		return entropy;
	}
	
	/**
	 * 
	 * @param node
	 * @param matrix
	 * @param value
	 * @param requiredValues set to null if this isn't required
	 * @return
	 */
	public static double proportions(Node node, Matrix matrix, String value, Map<String, String> requiredValues) {
		int total = 0;
		int totalValue = 0;
		if (requiredValues == null) {
			total = matrix.getData().length;
			totalValue = matrix.getNumberOfExamples(node.getAttribute(), value);
		} else {
			for (String[] row : matrix.getData()) {
				if (hasRequiredValues(row, requiredValues, matrix)) {
					total++;
					if (row[matrix.getAttributeIndex(node.getAttribute())].equals(value)) {
						totalValue++;
					}
				}
			}
		}
		
		return (double) totalValue / (double) total;
	}
}
