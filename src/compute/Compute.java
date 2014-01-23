package compute;

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
	 * @return
	 */
	public static double entropy(String attribute, String value, Matrix matrix) {
		int attributeIndex = matrix.getAttributeIndex(attribute);
		
		int valueCount = 0;
		int positiveCount = 0;
		int negativeCount = 0;
		String o = matrix.getPositiveClass();
		for (String[] row : matrix.getData()) {
			if (row[attributeIndex].equals(value)) {
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
			return - pp * Math.log(pp) - pm * Math.log(pm);
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
		for (String currentValue : matrix.getValidValues().get(node.getAttribute())) {
			entropy -= node.getEntropies().get(currentValue) * node.getProportions().get(currentValue);
		}
		// Note: entropy is now equal to our gain.
		return entropy;
	}
}
