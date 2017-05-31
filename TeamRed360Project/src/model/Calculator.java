package model;

import java.util.regex.*;

/**
 * This is the back end class to the calculator method
 * 
 * @author Amanda Aldrich, with large help from Boann of StackOverflow
 *
 */
public class Calculator {
	
	Double result;
	
	public Calculator(String equation){
		//System.out.println(equation);
		//System.out.println(eval(equation));
		result = eval(equation);
	}
	
	/**
	 * Getter for the result
	 * 
	 * @author Amanda Aldrich
	 * @return result, the final answer
	 */
	public Double getResult(){
		return result;
	}
	
	/**
	 * This is the parser for the equations
	 * 
	 * @author Boann, with tweaks and modifications my Amanda Aldrich
	 * @param str, the string
	 * @return returns the finished equation
	 */
	public static double eval(final String stringEq) {
	    return new Object() {
	        int pos = -1, ch;

	        void nextChar() {
	            ch = (++pos < stringEq.length()) ? stringEq.charAt(pos) : -1;
	        }

	        boolean eat(int charToEat) {
	            while (ch == ' ') nextChar();
	            if (ch == charToEat) {
	                nextChar();
	                return true;
	            }
	            return false;
	        }

	        double parse() {
	            nextChar();
	            double x = parseExpression();
	            if (pos < stringEq.length()) {
	            	throw new RuntimeException("Unexpected: " + (char)ch);
	            }
	            return x;
	        }
	        // Grammar:
	        // expression = term | expression `+` term | expression `-` term
	        // term = factor | term `*` factor | term `/` factor
	        // factor = `+` factor | `-` factor | `(` expression `)`
	        //        | number | functionName factor | factor `^` factor

	        double parseExpression() {
	            double x = parseTerm();
	            for (;;) {
	                if      (eat('+')) x += parseTerm(); // addition
	                else if (eat('-')) x -= parseTerm(); // subtraction
	                else return x;
	            }
	        }
	        
	        double parseTerm() {
	            double x = parseFactor();
	            for (;;) {
	                if      (eat('*')) x *= parseFactor(); // multiplication
	                else if (eat('/')) x /= parseFactor(); // division
	                else return x;
	            }
	        }

	        double parseFactor() {
	            if (eat('+')) return parseFactor(); // unary plus
	            if (eat('-')) return -parseFactor(); // unary minus

	            double x;
	            int startPos = this.pos;
	            if (eat('(')) { // parentheses
	                x = parseExpression();
	                eat(')');
	            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
	                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
	                x = Double.parseDouble(stringEq.substring(startPos, this.pos));
	            } else if (ch >= 'a' && ch <= 'z') { // functions
	                while (ch >= 'a' && ch <= 'z') nextChar();
	                String func = stringEq.substring(startPos, this.pos);
	                x = parseFactor();
	                if (func.equals("sqrt")) x = Math.sqrt(x);
	                else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
	                else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
	                else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
	                else throw new RuntimeException("Unknown function: " + func);
	            } else {
	                throw new RuntimeException("Unexpected: " + (char)ch);
	            }
	            if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

	            return x;
	        }
	    }.parse();
	}
	
}

