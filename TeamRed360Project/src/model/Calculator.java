package model;

import java.util.ArrayList;
import java.util.Stack;

public class Calculator {
	
	
	Double val1;
	
	Double val2;
	
	String finalResult;
	
	public Calculator(ArrayList equation){
		ArrayList<String> myEquation = new ArrayList<String>(equation);

		
		
		
	}
	
	private String magicSwitchCalc(ArrayList<String> equation){
		
		String result = "0.0";
		int index = 1;
		
		while(equation.size() - 3 >= 0){
			
			if(equation.contains("*")){
				index = equation.indexOf("*");
			}
			else if(equation.contains("/")){
				index = equation.indexOf("/");
			}
			
			try{
			Double val1 = Double.parseDouble(equation.get(index - 1));
			}
			catch(Exception e){
				return result = "-1.0";
			}
			
			String modifier = equation.get(index); //odd things happening here

			try{
			Double val2 = Double.parseDouble(equation.get(index + 1));
			}
			catch (Exception e){
				return result = "-1.0";
			}
			
			switch (modifier){
				
				case "+": equation.set(index - 1, add(val1, val2));
				
				case "-": equation.set(index - 1, subtract(val1, val2));
				
				case "*": equation.set(index - 1, multiply(val1, val2));
				
				case "/": equation.set(index - 1, divide(val1, val2));
				
				case "^": equation.set(index - 1, power(val1, val2));
			
			}
			
			for(int i = 0; i < 2; i++){
				equation.remove(index);
			}
		}

		return result;
	}

	private String add(Double val1, Double val2){
		
		Double result = val1 + val2;
		String strRes = "" + result;
		return strRes;
		
	}
	
	private String subtract(Double val1, Double val2){
		
		Double result = val1 - val2;
		String strRes = "" + result;
		return strRes;
		
	}
	
	private String multiply(Double val1, Double val2){
		
		Double result = val1 * val2;
		String strRes = "" + result;
		return strRes;
		
	}

	private String divide(Double val1, Double val2){
		
		Double result = val1 / val2;
		String strRes = "" + result;
		return strRes;
		
	}
	
	private String power(Double val1, Double val2){
		
		Double result = Math.pow(val1, val2);
		String strRes = "" + result;
		return strRes;
		
	}
	
	public String getFinalResult(){
		return finalResult;
	}
}

