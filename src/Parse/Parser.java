package Parse;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import core.Matrix;

public class Parser {


	public static Matrix parseFile(String fileURL) throws IOException{
		
		//// counting////////////////////////////////////////////////////////////
		
		FileInputStream fis = new FileInputStream(fileURL);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		String line = br.readLine().trim();
		
		int nbAttributes = 0;
		int nbExamples = 0;
		
		while(line != null){
			line = line.trim();
			
			if (optionalString("@attribute", line)){
				nbAttributes++;
			}
			else if (optionalString("@data", line)){
				line = br.readLine();
				while(line != null && line != "%"){
					nbExamples++;
					line = br.readLine();
				}
			}
			
			line = br.readLine();
		}
		
		///// parsing//////////////////////////////////////////////////////////////////////////////////	
		
		fis = new FileInputStream(fileURL);
		isr = new InputStreamReader(fis);
		br = new BufferedReader(isr);
		line = br.readLine().trim();
		
		int i = 0;
		String data[][] = new String[nbAttributes][nbExamples];
		String [] attributes = new String[nbAttributes];
		Map <String, List<String>> validValues = new TreeMap<String, List<String>>();
	
		while(line != null){
			line = line.trim();
			
			if (optionalString("@attribute", line)){
				List<String> tempList = new ArrayList<String>();
				String tempVal;
				String tempLine = line.substring(line.indexOf(' ')).trim();
				String tempAttribute = tempLine.substring(0, tempLine.indexOf('{')).trim().replace("'", "");
				tempLine = tempLine.substring(tempLine.indexOf('{') + 1, tempLine.indexOf('}') - 1).replace("'", "");
				while(tempLine.contains(",")){
					tempVal = tempLine.substring(0, tempLine.indexOf(",")-1);
					tempList.add(tempVal);
					tempLine = tempLine.replace(tempVal+",", "").trim();
				}
				tempVal = tempLine.trim();
				if (i < nbAttributes - 1){
					tempList.add("?");
				}
				attributes[i] = tempAttribute;
				validValues.put(tempAttribute, tempList);
				i++;
			}
			else if (optionalString("@data", line)){
				
			}
			
			line = br.readLine();
		}
		
		Matrix mat = new Matrix(data, attributes, validValues);
		return mat;
	}
	
	private static boolean optionalString(String string, String line) {
		if (!line.startsWith(string))
			return false;
		else
			return true;
	}
	
}
