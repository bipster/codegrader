package org.codegrader.tests;

public class TestCase {
	public final String input, expectedOutput, description;
	public final double weight;
	public Boolean succeeded;

	/**
	 * @param values One line of a CSV with format input,output,description,weight
	 *               weight is optional
	 */
	public TestCase(Iterable<String> line) {
		String input = null, expectedOutput = null, description = null;
		Double weight = null;

		for (String value : line) {
			if (input == null) {
				input = value;
			} else if (expectedOutput == null) {
				expectedOutput = value;
			} else if (description == null) {
				description = value;
			} else if (weight == null) {
				weight = Double.parseDouble(value);
			} else {
				throw new RuntimeException("Too many columns provided!");
			}
		}

		this.input = input;
		this.expectedOutput = expectedOutput;
		this.description = description;
		this.weight = weight == null ? 1 : weight;
	}

	@Override
	public String toString() {
		return input + " :: " + expectedOutput + " :: " + description + " :: " + weight;
	}
}
