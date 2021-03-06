package model;

/**
 * This is the back end class to the calculator method
 * 
 * @author Amanda Aldrich, with large help from Boann of StackOverflow
 * 
 */
public class Calculator {

	Double result;

	String stringResult;

	public Calculator(String equation) {
		// System.out.println(equation);
		// System.out.println(eval(equation));
		if (breakingTheUniverse(equation)) {
			//Added this to throw error on /0 -Jimmy
			throw new RuntimeException("Can't divide by zero");
			
		} else {
			result = eval(checkingDots(equation));

		}
	}

	/**
	 * Getter for the result
	 * 
	 * @author Amanda Aldrich
	 * @return result, the final answer
	 */
	public Double getResult() {
		return result;
	}

	/**
	 * Chekes for hanging dots
	 * 
	 * @author Amanda Aldrich
	 * @param eq,
	 *            the equation
	 * @return the string with zeros
	 */
	public static String checkingDots(String eq) {
		// if(eq.startsWith(".")){
		eq = "0" + eq;
		// }

		if (eq.endsWith(".")) {
			eq = eq + "0";
		}
		return eq;
	}

	public static Boolean breakingTheUniverse(String eq) {
		Boolean broken = false;
		if (eq.contains("/0")) {
			broken = true;
		} else {
			broken = false;
		}
		return broken;
	}

	/**
	 * This is the parser for the equations
	 * 
	 * @author Boann, with tweaks and modifications by Amanda Aldrich
	 * @param str,
	 *            the string
	 * @return returns the finished equation
	 */
	public static double eval(final String stringEq) {

		// System.out.println(stringEq);

		if (stringEq.length() > 3 && Character.isDigit(stringEq.charAt(0))
				&& Character.isDigit(stringEq.charAt(stringEq.length() - 1))) {

			return new Object() {
				int pos = -1, ch;

				void nextChar() {
					ch = (++pos < stringEq.length()) ? stringEq.charAt(pos) : -1;
				}

				boolean eat(int charToEat) {
					while (ch == ' ')
						nextChar();
					if (ch == charToEat) {
						nextChar();
						return true;
					}
					return false;
				}

				double parse() {
					nextChar();
					double x = parseExpression();
					if (pos < stringEq.length())
						throw new RuntimeException("Unexpected: " + (char) ch);
					return x;
				}
				// Grammar:
				// expression = term | expression `+` term | expression `-`
				// term
				// term = factor | term `*` factor | term `/` factor
				// factor = `+` factor | `-` factor | `(` expression `)`
				// | number | functionName factor | factor `^` factor

				double parseExpression() {
					double x = parseTerm();
					for (;;) {
						if (eat('+'))
							x += parseTerm(); // addition
						else if (eat('-'))
							x -= parseTerm(); // subtraction
						else
							return x;
					}
				}

				double parseTerm() {
					double x = parseFactor();
					for (;;) {
						if (eat('*'))
							x *= parseFactor(); // multiplication
						else if (eat('/'))
							x /= parseFactor(); // division
						else
							return x;
					}
				}

				double parseFactor() {
					if (eat('+'))
						return parseFactor(); // unary plus
					if (eat('-'))
						return -parseFactor(); // unary minus

					double x;
					int startPos = this.pos;

					if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
						while ((ch >= '0' && ch <= '9') || ch == '.')
							nextChar();
						x = Double.parseDouble(stringEq.substring(startPos, this.pos));
					} else {
						throw new RuntimeException("Unexpected: " + (char) ch);
					}
					if (eat('^'))
						x = Math.pow(x, parseFactor()); // exponentiation

					return x;
				}
			}.parse();

		} else {
			return 0.0;
		}
	}

}
