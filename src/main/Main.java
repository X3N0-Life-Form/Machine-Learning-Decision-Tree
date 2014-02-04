package main;

import java.io.IOException;
import java.util.List;

import parse.Parser;
import core.Matrix;
import core.Node;

/**
 * Main class. Contains the main method, which is the main entry point into the program.
 * @author Sara Tari & Adrien Droguet
 *
 */
public class Main {
	
	/**
	 * Main method, takes up to 3 arguments.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		handleArgs(args);
		printRecap();
		
		Matrix matrix = Parser.parseFile(Engine.getFileName());
		
		List<Node> firstNodes = Engine.createNodes(matrix, Engine.root);
		Engine.root = Engine.chooseBest(firstNodes, matrix);
		Engine.createSons(Engine.root, matrix, 0);
		
		int currentDepth = 1;
		Node currentNode = Engine.root;
		
		Engine.buildTree(matrix, currentNode, currentDepth);
		System.out.println(Engine.root.ourToString(0));
	}

	/**
	 * Prints a small recap of the current settings (selected file, max depth, etc.).
	 */
	public static void printRecap() {
		System.out.println("File:\t" + Engine.getFileName());
		System.out.println("Max depth:\t" + Engine.getMaxDepth());
		System.out.println("Impurity:\t" + Engine.getImpurity());
	}

	/**
	 * Handles main(String[] args)'s arguments.
	 * @param args
	 */
	protected static void handleArgs(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String currentArg = args[i];
			switch (currentArg) {
			case "-file":
				Engine.setFileName(args[++i]);
				break;
			case "-maxDepth":
				Engine.setMaxDepth(Integer.parseInt(args[++i]));
				break;
			case "-impurity":
				Engine.setImpurity(Integer.parseInt(args[++i]));
				break;
			default:
				System.err.println("Unrecognised argument:" + currentArg);
			}
		}
	}
	
}
