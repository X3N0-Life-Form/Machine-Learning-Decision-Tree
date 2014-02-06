package parser;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import main.Engine;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import parse.Parser;
import parse.ParserException;
import core.Matrix;

public class ParserTests {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * 5 attributes & 14 examples.
	 * @throws IOException
	 * @throws ParserException 
	 */
	@Test
	public void test() throws IOException, ParserException {
		Matrix mat = Parser.parseFile(Engine.DEFAULT_FILE_NAME);
		assertTrue(mat.getAttributes().length == 5);
		assertTrue(mat.getData().length == 14);
		//NOTE: attributes and data have been verified visually
	}

}
