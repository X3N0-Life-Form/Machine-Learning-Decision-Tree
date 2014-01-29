package core;

import java.util.List;
import java.util.Map;

public class Matrix {

	private String data[][];
	private String attributes[];
	private Map<String, List<String>> validValues;
	
	public Matrix(String[][] data, String attributes[],
			Map<String, List<String>> validValues) {
		super();
		this.data = data;
		this.attributes = attributes;
		this.validValues = validValues;
	}

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
	
	public String getPositiveClass() {
		return getValidValues().get(attributes[attributes.length - 1]).get(0);
	}
	
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
	 * 
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
	 * 
	 * @param attribute
	 * @param currentValue
	 * @return boolean true if the class value was positive.
	 */
	public boolean getFirstClassValue(String attribute, String currentValue) {
		int index = getAttributeIndex(attribute);
		for (int i = 0; i < data.length; i++) {
			if (data[i][index].equals(currentValue)) {
				if (data[i][getClassAttributeIndex()].equals(getPositiveClass())) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
}
