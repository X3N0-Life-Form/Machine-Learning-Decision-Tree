package compute;

import java.util.Map;

import core.Matrix;

/**
 * Class meant to compute requirements.
 * @author Adrien Droguet & Sara Tari
 *
 */
public class Requirements {
	
	/**
	 * Do not instantiate this class.
	 */
	private Requirements() {}

	/**
	 * Verifies that the specified row contains certain required values.
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

}
