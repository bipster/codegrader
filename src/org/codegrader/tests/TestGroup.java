package org.codegrader.tests;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class TestGroup implements Iterable<TestCase> {
	final List<TestCase> tests = new ArrayList<>();
	final String name;
	final double totalPoints, totalWeight;

	public TestGroup(File file) throws IOException {
		String fileName = file.getName();
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		String[] split = fileName.split("_");
		name = split[0];
		double points = 1;
		if (split.length > 1) {
			try {
				points = Double.parseDouble(split[1]);
			}
			catch (NumberFormatException e) {
				points = 1;
			}
		}
		this.totalPoints = points;

		double totalWeight = 0;
		CSVParser parser = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.DEFAULT);
		for (CSVRecord record : parser) {
			TestCase test = new TestCase(record);
			tests.add(test);
			totalWeight += test.weight;
		}
		this.totalWeight = totalWeight;
	}

	public double getScore() {
		double successfulWeight = 0;
		try {
			for (TestCase test : tests) {
				if (test.succeeded) {
					successfulWeight += test.weight;
				}
			}
		}
		catch (NullPointerException e) {
			throw new RuntimeException("Not all tests have been graded yet!", e);
		}
		return (successfulWeight / totalWeight) * totalPoints;
	}

	@Override
	public String toString() {
		return "\n" + name + " : " + totalPoints + "\n" + tests.toString();
	}

	@Override
	public Iterator<TestCase> iterator() {
		return tests.iterator();
	}
}
