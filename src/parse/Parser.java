package parse;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import core.Matrix;

/**
 * Class that deals with the parsing of a .arff file.
 * @author Sara Tari
 *
 */
public class Parser {

	/**
	 * Self explanatory.
	 * @param fileURL
	 * @return A Matrix object representing the parsed file.
	 * @throws IOException
	 * @throws ParserException If an invalid value was encountered
	 */
	public static Matrix parseFile(String fileURL) throws IOException, ParserException {
		//////////////////////////
		///// Counting Phase /////
		//////////////////////////
		
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
		
		System.out.println("\nNumber of attributes: "+ nbAttributes);
		System.out.println("Number of examples: " + nbExamples);
		
		/////////////////////////
		///// Parsing Phase /////
		/////////////////////////
		
		fis.close();
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
			/*	if (i < nbAttributes - 1){
					tempList.add("?");
				}*/
				attributes[i] = tempAttribute;
				validValues.put(tempAttribute, tempList);
				i++;
			}
			else if (optionalString("@data", line)){
				int j = 0;
				String tempArg;
				while(line != null){
					line = br.readLine();
					if (line == null)
						break;
					line = removeComments(line);
					String tempLine = line.trim();
					
					i = 0;
					while(tempLine.length() != 0){						
						if (tempLine.contains(",")){
							tempArg = tempLine.substring(0, tempLine.indexOf(",")).trim();
							if (tempArg.equals("?"))
								tempArg = "\\" + tempArg;
							tempLine = tempLine.replaceFirst(tempArg+",", "");
						}
						else {
							tempArg = tempLine.trim();
							tempLine = tempLine.replace(tempArg, "");
						}
						if (!validValues.get(attributes[i]).contains(tempArg)) {
							br.close();
							throw new ParserException("Invalid value detected for attribute " + attributes[i] + "(value= " + tempArg + ")");
						}
						data[j][i] = tempArg;					
						i++;
					}
					j++;		
					
				}				
			}	
			
			line = br.readLine();
		}
		
		br.close();
		Matrix mat = new Matrix(data, attributes, validValues);
		return mat;
	}
	
	/**
	 * Removes comments from a line.
	 * @param line
	 * @return A line without comments.
	 */
	protected static String removeComments(String line) {
		String myLine = line;
		if (line.contains("%")) {
			int position = line.indexOf("%");
			myLine = line.substring(0, position);
		}
		return myLine;
	}

	/**
	 * Checks whether a non-essential String is present.
	 * @param string
	 * @param line
	 * @return True if the line starts with the specified String.
	 */
	public static boolean optionalString(String string, String line) {
		if (!line.startsWith(string))
			return false;
		else
			return true;
	}
	
}
