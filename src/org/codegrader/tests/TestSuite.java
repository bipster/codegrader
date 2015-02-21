package org.codegrader.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestSuite implements Iterable<TestGroup> {
	public final List<TestGroup> groups = new ArrayList<>();
	public final double totalPoints;

	TestSuite(File dir) throws IOException {
		double totalPoints = 0;
		for (File file : dir.listFiles()) {
			TestGroup group = new TestGroup(file);
			groups.add(group);
			totalPoints += group.totalPoints;
		}
		this.totalPoints = totalPoints;
	}

	public double getScore() {
		double points = 0;
		for (TestGroup group : groups) {
			points += group.getScore();
		}
		return points;
	}

	@Override
	public String toString() {
		return "totalPoints : " + totalPoints + "\n" + groups.toString();
	}

	@Override
	public Iterator<TestGroup> iterator() {
		return this.groups.iterator();
	}
}
