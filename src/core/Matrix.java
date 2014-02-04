package core;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import parse.Parser;

import compute.Requirements;

/**
 * Class representing the information contained in a .arff file.
 * @author Sara Tari & Adrien Droguet
 * @see Parser
 */
public class Matrix {

	private String data[][];
	private String attributes[];
	private Map<String, List<String>> validValues;
	
	/**
	 * Constructs a Matrix object.
	 * @param data
	 * @param attributes
	 * @param validValues
	 */
	public Matrix(String[][] data, String attributes[],
			Map<String, List<String>> validValues) {
		super();
		this.data = data;
		this.attributes = attributes;
		this.validValues = validValues;
	}

	///////////////////////////
	//// Getters / Setters ////
	///////////////////////////
	
	/**
	 * Returns a data row as read from a .arff file.
	 * @return String[row][attribute value]
	 */
	public String[][] getData() {
		return data;
	}

	public void setData(String[][] data) {
		this.data = data;
	}

	public String[] getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes[]) {
		this.attributes = attributes;
	}

	public Map<String, List<String>> getValidValues() {
		return validValues;
	}

	public void setValidValues(Map<String, List<String>> validValues) {
		this.validValues = validValues;
	}
	
	/////////////////////////
	//// Special Getters ////
	/////////////////////////
	
	/**
	 * We don't do that anymore.
	 * @return Was supposed to be the 'positive' value of the class attribute.
	 */
	@Deprecated
	public String getPositiveClass() {
		return getValidValues().get(attributes[attributes.length - 1]).get(0);
	}
	
	/**
	 * Counts the number of times the value of an attribute appears.
	 * @param attribute
	 * @param value
	 * @return Number of row containing the specified value.
	 */
	public int getNumberOfExamples(String attribute, String value) {
		int total = 0;
		int attributeIndex = getAttributeIndex(attribute);
		for (String[] row : data) {
			if (row[attributeIndex].equals(value)) {
				total++;
			}
		}
		return total;
	}

	/**
	 * Gets the index of an attribute in a row of the data matrix.
	 * @param attribute
	 * @return -1 if the attribute could not be found.
	 */
	public int getAttributeIndex(String attribute) {
		int attributeIndex = -1;
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i].equals(attribute)) {
				attributeIndex = i;
				break;
			}
		}
		return attributeIndex;
	}

	/**
	 * 
	 * @return String value of the class attribute.
	 */
	public String getClassAttribute() {
		return attributes[attributes.length -1];
	}

	/**
	 * 
	 * @return int index of the class attribute.
	 */
	public int getClassAttributeIndex() {
		return attributes.length - 1;
	}
	
	/**
	 * Returns the class value of the first row matching the value of the specified attribute.
	 * A Map of required attribute/value can be specified.
	 * @param attribute
	 * @param currentValue
	 * @param required Can be null.
	 * @return First class value found.
	 */
	public String getFirstClassValue(String attribute, String currentValue, Map<String, String> required) {
		int index = getAttributeIndex(attribute);
		for (int i = 0; i < data.length; i++) {
			if (Requirements.hasRequiredValues(data[i], required, this) && data[i][index].equals(currentValue)) {
				return data[i][getClassAttributeIndex()];
			}
		}
		return "";
	}

	/**
	 * Calculates the dominant class value among rows matching the specified attribute/value.
	 * A Map of required attribute/value can be specified.
	 * @param attribute
	 * @param currentValue
	 * @param required Can be null.
	 * @return The dominant class value.
	 */
	public String getDominantClassValue(String attribute, String currentValue, Map<String, String> required) {
		int index = getAttributeIndex(attribute);
		Map<String, Integer> counters = new TreeMap<String, Integer>();
		List<String> validClassValues = validValues.get(getClassAttribute());
		for (String currentClassValue : validClassValues) {
			counters.put(currentClassValue, 0);
		}
		
		for (int i = 0; i < data.length; i++) {
			if (Requirements.hasRequiredValues(data[i], required, this) && data[i][index].equals(currentValue)) {
				String classValue = data[i][getClassAttributeIndex()];
				Integer value = counters.get(classValue);
				counters.put(classValue, value + 1);
			}
		}
		
		String dominant = "DOMINATING";
		counters.put(dominant, 0);
		for (String current : counters.keySet()) {
			if (counters.get(dominant) < counters.get(current)) {
				dominant = current;
			}
		}
		return dominant;
	}
}
