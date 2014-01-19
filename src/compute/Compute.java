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
		String[] attributes = matrix.getAttributes();
		int attributeIndex = -1;
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i] == attribute) {
				attributeIndex = i;
				break;
			}
		}
		
		int valueCount = 0;
		int positiveCount = 0;
		int negativeCount = 0;
		for (String[] row : matrix.getData()) {
			if (row[attributeIndex].equals(value)) {
				valueCount++;
				
				if (row[row.length - 1].equals("cool")) { //TODO: modify this
					positiveCount++;
				} else {
					negativeCount++;
				}
			}
		}
		
		if (positiveCount == negativeCount) {
			return 1;
		} else if (positiveCount == 0 || negativeCount == 0) {
			return 0;
		} else {
			double pp = positiveCount / valueCount;
			double pm = negativeCount / valueCount;
			
			return - pp * Math.log(pp) - pm * Math.log(pm);
		}
	}
	
	
	public static double gain(String attribute, Node node) {
		
		return -1;
	}
}
