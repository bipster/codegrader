package org.codegrader;

import java.io.*;
import java.util.Scanner;

/**
 * Created by jim on 2/21/15.
 */
public class LegacyAttempt {
    private static final File binary = new File(System.getProperty("user.dir")
            + "/bin/org/ewsis/apcompsci/calc/Calculator.class");

    private static BufferedReader errors;
    private static PrintWriter programInput;
    private static Scanner programOutput;
    private static Process process;

    public static void main(String[] args) {
        System.out.println(binary.getAbsolutePath());
        Runtime runtime = Runtime.getRuntime();

        try {
            process = runtime.exec("java -cp ./bin org.ewsis.apcompsci.calc.Calculator");

            errors = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            programInput = new PrintWriter(process.getOutputStream());
            programOutput = new Scanner(process.getInputStream());

            checkForErrors();
            verifyIntro();

//			testAdditionCalculations();
            testSubtractionCalculations();
            testMultiplicationCalculations();
            testDivisionCalculations();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private static void testAdditionCalculations() throws IOException {
        // Whole Numbers
        testCalculation("1 + 1", "2", "Simple Addition");
        testCalculation("-1 + 3", "2", "Negative numbers");
        testCalculation("-5 + 5", "0", "Inverted numbers");
        testCalculation("100 + 900", "1000", "Big numbers");
        // Proper Fractions
        testCalculation("1/2 + 1/2", "1", "Fractions summing to whole");
        testCalculation("-1/5 + 1/5", "0", "Inverted fractions");
        testCalculation("3/5 + 1/5", "4/5", "Adding to proper fraction");
        testCalculation("3/5 + 3/5", "1_1/5", "Adding to improper fraction");
        // Improper Fractions
        testCalculation("11/5 + 4/5", "3", "Improper fractions adding to whole");
        // Mixed Numbers
        testCalculation("10_5/10 + 10_5/10", "21", "Mixed adding to whole");
        testCalculation("1_1/2 + 1_3/4", "3_1/4", "Mixed summing to improper");
        testCalculation("-1_3/4 + 1_3/4", "0", "Inverted mixed");
        // Misc Types
        testCalculation("1 + 1/5", "1_1/5", "Whole and Fraction");
        testCalculation("-1 + 1/5", "-4/5", "Neg Whole and Fraction");
        testCalculation("5 + 7_3/4", "12_3/4", "Whole and Mixed");
        testCalculation("5 + -7_3/4", "-2_3/4", "Whole and Neg Mixed");
        testCalculation("1/5 + 1_4/5", "2", "Fraction and Mixed");
        testCalculation("60/15 + 5_2/5", "9_2/5", "Improper and Mixed");
    }

    private static void testSubtractionCalculations() throws IOException {
        // Whole Numbers
        testCalculation("3 - 1", "2", "Simple Subtraction");
        testCalculation("1 - 3", "-2", "Negative Subtraction");
        testCalculation("3 - -3", "6", "Inverted Numbers");
        testCalculation("-3 - -3", "0", "Same Numbers");
        testCalculation("3 - 3", "0", "Equaling 0");
    }

    private static void testMultiplicationCalculations() throws IOException {
        // Whole Numbers
        testCalculation("5 * 5", "25", "Simple Multiplication");
        testCalculation("5 * -5", "-25", "negative Multiplication");
    }

    private static void testDivisionCalculations() throws IOException {
        // Whole Numbers
        testCalculation("10 / 5", "2", "Clean Division");
        testCalculation("10 / 3", "3_1/3", "Division with remainder");
        // Mixed Numbers
        testCalculation("6_1/2 / 2", "3_1/4", "Mixed division");
        testCalculation("6_1/2 / -2", "-3_1/4", "Neg Mixed Division #1");
        testCalculation("-6_1/2 / 2", "-3_1/4", "Neg Mixed Division #2");
        testCalculation("-6_1/2 / -2", "3_1/4", "Double Neg Mixed Division");
    }
    private static void testCalculation(String expression, String correctAnswer, String desc)
            throws IOException {
        checkForErrors();
        programInput.println(expression);
        programInput.flush();
        checkForErrors();
        String theirAnswer = programOutput.nextLine();
        theirAnswer = theirAnswer.substring(theirAnswer.indexOf(" ") + 1);
        assertEquals(correctAnswer, theirAnswer, desc + ": " + expression + " (" + theirAnswer + ")");
        checkForErrors();
    }
    private static void checkForErrors() throws IOException {
        boolean hasErrors = errors.ready();
        while (errors.ready()) {
            System.err.println(errors.readLine());
        }
        if (hasErrors) {
            System.exit(1);
        }
    }

    private static void verifyIntro() throws IOException {
        String nextLine = programOutput.nextLine();
        assertEquals("Welcome to Calculator!", nextLine, nextLine);
        nextLine = programOutput.nextLine();
        assertEquals("Enter an expression and I will evaluate it for you.", nextLine, nextLine);
        nextLine = programOutput.nextLine();
        assertEquals("Or type \"quit\" to stop.", nextLine, nextLine);
        char nextChar = (char) process.getInputStream().read();
        assertEquals(">", "" + nextChar, "" + nextChar);
    }

    private static void assertEquals(String correct, String tested, String message) {
        assertCondition(correct.equals(tested), message);
    }
    private static void assertCondition(boolean expression, String message) {
        if (!expression) {
            System.err.println(message);
        }
    }
}
