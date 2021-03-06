package main;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import parse.Parser;
import parse.ParserException;
import core.Matrix;
import core.Node;

public class NodeCreationTest {
	
	public static final String testFile = "resources/data/weather.nominal.arff";
	private Matrix matrix;
	
	@Before
	public void setup() throws IOException, ParserException {
		matrix = Parser.parseFile(testFile);
	}

	@Test
	public void testCreateFirstNode() {
		List<Node> nodeList = Engine.createNodes(matrix, null);
		assertTrue(nodeList.size() == matrix.getAttributes().length);
		//NOTE: individual nodes verified visually
	}

}
