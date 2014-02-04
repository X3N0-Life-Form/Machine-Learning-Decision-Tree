package compute;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import main.Main;

import org.junit.Before;
import org.junit.Test;

import parse.Parser;
import core.Matrix;
import core.Node;

public class ComputeTest {
	
	public static final String testFile = "resources/data/weather.nominal.arff";
	private Matrix matrix;
	
	@Before
	public void setup() throws IOException {
		matrix = Parser.parseFile(testFile);
	}

	//@Test
	public void testEntropy() {
		double result = Compute.entropy("outlook", "sunny", matrix, null);
		assertTrue(result > 0.0);
		assertTrue(result < 1.0);
	}

	@Test
	public void testGain() {
		List<Node> nodeList = Main.createNodes(matrix, null);
		for (Node currentNode : nodeList) {
			@SuppressWarnings("unused")
			double gain = Compute.gain(currentNode, matrix);
		//	System.err.println(currentNode.getAttribute() + " : " +gain);
		//	System.err.println(currentNode.getEntropy());
			for (String key : currentNode.getEntropies().keySet()) {
				System.err.println("\t" +key + " : " + currentNode.getEntropies().get(key));
				
			}
		}
	}
}
