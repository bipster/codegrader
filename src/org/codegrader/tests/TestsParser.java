package org.codegrader.tests;

import java.io.File;
import java.io.IOException;

public class TestsParser {

	public static TestSuite parseTests(File dir) throws IOException {
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			throw new RuntimeException("That directory doesn't exist!");
		}
		return new TestSuite(dir);
	}

	/**
	 * Just for testing
	 */
	public static void main(String[] args) throws IOException {
		File file = new File("tests");
		TestSuite tests = parseTests(file);

		for (TestGroup group : tests) {
			for (TestCase test : group) {
				test.setActualOutput(Math.random() > 0.5d ? test.expectedOutput : "bad output");
			}
		}
		System.out.println(tests.prettyResults());
	}
}
