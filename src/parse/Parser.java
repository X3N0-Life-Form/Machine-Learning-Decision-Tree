package parse;

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
	
	public static void main(String[] args) throws IOException{
		Matrix mat = parseFile("resources/data/weather.nominal.arff");
		
		//att 5 ex 14
		
		for (int i = 0; i < 14 ; i++){
			//System.out.println(i);
			System.out.println(mat.getValidValues());
		for (int j = 0 ; j < 5 ; j++){
				System.out.println(mat.getAttributes()[j]);
				System.out.println(i + " - "+ j);
				System.out.println(mat.getData()[i][j]);
			}
		}
		
	}


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
		
		System.out.println("att "+ nbAttributes +" ex" + nbExamples);
		
		///// parsing//////////////////////////////////////////////////////////////////////////////////	
		
		fis = new FileInputStream(fileURL);
		isr = new InputStreamReader(fis);
		br = new BufferedReader(isr);
		line = br.readLine().trim();
		
		int i = 0;
		String data[][] = new String[nbExamples][nbAttributes];
		String [] attributes = new String[nbAttributes];
		Map <String, List<String>> validValues = new TreeMap<String, List<String>>();
	
		while(line != null){
			line = line.trim();
			
			if (optionalString("@attribute", line)){
				List<String> tempList = new ArrayList<String>();
				String tempVal;
				String tempLine = line.substring(line.indexOf(' ')).trim();
				String tempAttribute = tempLine.substring(0, tempLine.indexOf('{')).trim().replace("'", "");
				tempLine = tempLine.substring(tempLine.indexOf('{') + 1, tempLine.indexOf('}')).replace("'", "");
				while(tempLine.contains(",")){
					tempVal = tempLine.substring(0, tempLine.indexOf(","));
					tempList.add(tempVal);
					tempLine = tempLine.replace(tempVal+",", "").trim();
				}
				tempVal = tempLine.trim();
				tempList.add(tempVal);
				if (i < nbAttributes - 1){
					tempList.add("?");
				}
				attributes[i] = tempAttribute;
				validValues.put(tempAttribute, tempList);
				i++;
			}
			else if (optionalString("@data", line)){
				int j = 0;
				String tempArg;
				while(line != null && !line.contains("%")){
					line = br.readLine();
					if (line == null)
						break;
					String tempLine = line.trim();
					
					i = 0;
					while(tempLine.length() != 0){						
						if (tempLine.contains(",")){
							tempArg = tempLine.substring(0, tempLine.indexOf(",")).trim();
							tempLine = tempLine.replace(tempArg+",", "");
						}
						else {
							tempArg = tempLine.trim();
							tempLine = tempLine.replace(tempArg, "");	
						}
						data[j][i] = tempArg;					
						i++;
					}
					j++;		
					
				}				
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
