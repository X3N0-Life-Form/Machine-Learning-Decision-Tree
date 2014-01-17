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
	
	
}
