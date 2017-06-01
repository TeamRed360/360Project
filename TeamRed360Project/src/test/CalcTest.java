package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.Calculator;

/**
 * JUnit tests
 * 
 * @author Amanda Aldrich
 *
 */
public class CalcTest {

	String tester;
	Calculator calc;

	@Before
	public void setUp() {
		tester = "";
		calc = new Calculator(tester);
	}

	@Test
	public void testEval() {
		tester = ".1+1.";
		String tester1 = "2/0";
		String tester2 = "1+3";
		String tester3 = "9-5";
		String tester4 = "6*4";
		String tester5 = "7/1";
		String tester6 = "8^2";
		String tester7 = "-10+1";
		String tester8 = "1+-11";
		String tester9 = "+12+1";
		String tester10 = "1++13";

		assertEquals(1.1, new Calculator(tester).getResult(), .0001);
		assertEquals(0.0, new Calculator(tester1).getResult(), .0001);
		assertEquals(4.0, new Calculator(tester2).getResult(), .0001);
		assertEquals(4.0, new Calculator(tester3).getResult(), .0001);
		assertEquals(24.0, new Calculator(tester4).getResult(), .0001);
		assertEquals(7.0, new Calculator(tester5).getResult(), .0001);
		assertEquals(64.0, new Calculator(tester6).getResult(), .0001);
		assertEquals(-9.0, new Calculator(tester7).getResult(), .0001);
		assertEquals(-10.0, new Calculator(tester8).getResult(), .0001);
		assertEquals(13.0, new Calculator(tester9).getResult(), .0001);
		assertEquals(14.0, new Calculator(tester10).getResult(), .0001);
	}

	@Test
	public void testBreakingTheUniverseTrue() {
		tester = "2/0";
		assertTrue(calc.breakingTheUniverse(tester));
	}

	@Test
	public void testBreakingTheUniverseFalse() {
		tester = "2/2";
		assertEquals(false, calc.breakingTheUniverse(tester));
	}

	@Test(expected = RuntimeException.class)
	public void testIfBadStrings() {
		tester = "11";
		String tester1 = "//99";
		String tester2 = "ab,c'd:";
		String tester3 = "11*";
		String tester4 = "-1 8";
		String tester5 = "1 1 1";

		assertEquals(0.0, new Calculator(tester).getResult(), .0001);
		assertEquals(0.0, new Calculator(tester1).getResult(), .0001);
		assertEquals(0.0, new Calculator(tester2).getResult(), .0001);
		assertEquals(0.0, new Calculator(tester3).getResult(), .0001);
		assertEquals(0.0, new Calculator(tester4).getResult(), .0001);
		assertEquals(0.0, new Calculator(tester5).getResult(), .0001);

	}

	@Test
	public void testIfNoString() {
		tester = "";

		assertEquals(0.0, new Calculator(tester).getResult(), .0001);

	}
}
