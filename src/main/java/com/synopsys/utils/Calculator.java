package main.java.com.synopsys.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

/**
 * 
 * @author brianr
 * 
 * Takes the first argument of main input, computes the result, and prints it to the console
 * 
 * Assuming Length of variable name populated within "let" operation would always be 1.
 *
 */
public class Calculator {
	
	private final static long MAX_INT_IN_LONG = (long) Integer.MAX_VALUE;
	private final static long MIN_INT_IN_LONG = (long) Integer.MIN_VALUE;

	private static final int OPERATION_ADD = 1;
	private static final int OPERATION_SUB = 2;
	private static final int OPERATION_MULT = 3;
	private static final int OPERATION_DIV = 4;
	private static final int OPERATION_LET = 5;
	
	private static final String ERROR_MSG_EXPRESSION = "The expression is invalid.";
	private static final String ERROR_MSG_VARIABLE_NOT_SPEC = "No value been specified for the variable being used.";
	private static final String ERROR_MSG_BEYOND_MIN = "Result beyond the integer min value.";
	private static final String ERROR_MSG_BEYOUND_MAX = "Result beyond the integer max value.";
	private static final String ERROR_MSG_DIVIDED_BY_ZERO = "An operation of divided by zero.";

	private final static Logger LOGGER = Logger.getLogger(Calculator.class.getName());
	
	/**
	 * String value of the main method argument will be treated as expression and pass in here for validation then pass down to operationDispatch. 
	 * exception will be thrown if the expression is invalid or bad operation happens, like divided by zero.
	 * 
	 * @param exp
	 * @return final value of the whole expression if success.
	 * @throws Exception 
	 */
	public int mainOperation(String exp) throws Exception {
		//convert to lower case and trim it.
		exp = exp.toLowerCase().trim();
		
		//remove all the white spaces
		exp = exp.replaceAll("\\s","");
		
		String errMsg = validateExpression(exp);
		if(!StringUtils.isEmpty(errMsg)) {
			LOGGER.error(errMsg);
			throw new Exception(errMsg);
		}
		
		int[] arrRtn = operationDispatch(exp, new HashMap<String, Integer>());
		
		if(arrRtn[0]!=100) {
			switch (arrRtn[0]) {
				case -100:
					throw new Exception(ERROR_MSG_EXPRESSION);
				case -200:
					throw new Exception(ERROR_MSG_VARIABLE_NOT_SPEC);
				case -300:
					throw new Exception(ERROR_MSG_BEYOND_MIN);
				case -400:
					throw new Exception(ERROR_MSG_BEYOUND_MAX);
				case -500:
					throw new Exception(ERROR_MSG_DIVIDED_BY_ZERO);
				default:
					//Nothing need to do here.
			}
		}
		
		return arrRtn[1];
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 * 	status code and the result value if the process is successfully.
	 *
	 * Return status code
	 * 		1)  100: Process successfully.
	 * 		2) -300: Result beyond the integer min value.
	 * 		3) -400: Result beyond the integer max value.
	 */
	private int[] add(int a, int b) {
		int[] arrRtn = new int[2];
		long x = (long) a;
		long y = (long) b;
		
		if((x+y)>(MAX_INT_IN_LONG)) {
			String errMsg = "Sum of " + a + " and  " + b + " beyond the maximum value of integer.";
			LOGGER.error(errMsg);
			arrRtn[0] = -400;
			return arrRtn;
		}
		
		if((x+y)<(MIN_INT_IN_LONG)) {
			String errMsg = "Sum of " + a + " and  " + b + " beyond the minimum value of integer.";
			LOGGER.error(errMsg);
			arrRtn[0] = -300;
			return arrRtn;
		}
		
		int result = a + b;
		arrRtn[0] = 100;
		arrRtn[1] = result;
		
		return arrRtn;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 * 	status code and the result value if the process is successfully.
	 *
	 * Return status code
	 * 		1)  100: Process successfully.
	 * 		2) -300: Result beyond the integer min value.
	 * 		3) -400: Result beyond the integer max value.
	 */
	private int[] sub(int a, int b) {
		int[] arrRtn = new int[2];
		long x = (long) a;
		long y = (long) b;
		
		if((x-y)>(MAX_INT_IN_LONG)) {
			String errMsg = "Result of subtract " + b + " from  " + a + " beyond the maximum value of integer.";
			LOGGER.error(errMsg);
			arrRtn[0] = -400;
			return arrRtn;
		}
		
		if((x-y)<(MIN_INT_IN_LONG)) {
			String errMsg = "Result of subtract " + b + " from  " + a + " beyond the minimum value of integer.";
			LOGGER.error(errMsg);
			arrRtn[0] = -300;
			return arrRtn;
		}

		int result = a - b;
		arrRtn[0] = 100;
		arrRtn[1] = result;
		
		return arrRtn;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 * 	status code and the result value if the process is successfully.
	 *
	 * Return status code
	 * 		1)  100: Process successfully.
	 * 		2) -300: Result beyond the integer min value.
	 * 		3) -400: Result beyond the integer max value.
	 */
	private int[] mult(int a, int b) {
		int[] arrRtn = new int[2];
		long x = (long) a;
		long y = (long) b;
		
		if((x*y)>(MAX_INT_IN_LONG)) {
			String errMsg = "Result of multiply " + a + " and  " + b + " beyond the maximum value of integer.";
			LOGGER.error(errMsg);
			arrRtn[0] = -400;
			return arrRtn;
		}
		
		if((x*y)<(MIN_INT_IN_LONG)) {
			String errMsg = "Result of multiply " + a + " and  " + b + "  beyond the minimum value of integer.";
			LOGGER.error(errMsg);
			arrRtn[0] = -300;
			return arrRtn;
		}
		
		int result = a*b;
		arrRtn[0] = 100;
		arrRtn[1] = result;
		
		return arrRtn;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 * 	status code and the result value if the process is successfully.
	 *
	 * Return status code
	 * 		1)  100: Process successfully.
	 * 		2) -500: Can not divided by zero. 
	 */
	private int[] div(int a, int b) {
		int[] arrRtn = new int[2];
		if(b==0) {
			String errMsg = "Can not divided by zero.";
			LOGGER.error(errMsg);
			arrRtn[0] = -500;
			return arrRtn;
		}
		
		int result = a/b;
		arrRtn[0] = 100;
		arrRtn[1] = result;
		
		return arrRtn;
	}
	
	/**
	 * Expression of possible any kind of operation will be dispatched to the corresponding operation, it will be called repeatedly 
	 * when there's multiple level of operation in the exp
	 * 
	 * @param exp
	 * @return
	 * 	status code and the result value if the process is successfully.
	 *
	 * Return status code
	 * 		1)  100: Process successfully.
	 * 		2) -100: Invalid expression; 
	 * 		3) -200: Using variable with value not been specified yet. 
	 * 		4) -300: Result beyond the integer min value.
	 * 		5) -400: Result beyond the integer max value.
	 * 		6) -500: Can not divided by zero.
	 * 
	 */
	private int[] operationDispatch(String exp, Map<String, Integer> map) {
		int[] arrRtn = new int[2];
		int operationType = 0;
		char ch = exp.charAt(0);
		
		switch (ch) {
			case 'a':
				if(exp.indexOf("add(")!=0) { // would be an invalid expression
					LOGGER.error(exp + " is not a valid expression.");
					arrRtn[0] = -100;
					return arrRtn;
				}
				
				operationType = OPERATION_ADD;
				break;
			case 's':
				if(exp.indexOf("sub(")!=0) { // would be an invalid expression
					LOGGER.error(exp + " is not a valid expression.");
					arrRtn[0] = -100;
					return arrRtn;
				}
				
				operationType = OPERATION_SUB;
				break;
			case 'm':
				if(exp.indexOf("mult(")!=0) { // would be an invalid expression
					LOGGER.error(exp + " is not a valid expression.");
					arrRtn[0] = -100;
					return arrRtn;
				}
				
				operationType = OPERATION_MULT;
				break;
			case 'd':
				if(exp.indexOf("div(")!=0) { // would be an invalid expression
					LOGGER.error(exp + " is not a valid expression.");
					arrRtn[0] = -100;
					return arrRtn;
				}
				
				operationType = OPERATION_DIV;
				break;
			case 'l':
				if(exp.indexOf("let(")!=0) { // would be an invalid expression
					LOGGER.error(exp + " is not a valid expression.");
					arrRtn[0] = -100;
					return arrRtn;
				}
				
				operationType = OPERATION_LET;
				break;
			default:
				
		}
		
		if(operationType == OPERATION_LET)
			return processLetOperation(exp, map);
		
		return processOperation(exp, operationType, map);
	}
	
	/**
	 * Process operations including add, sub, mult and div.
	 * 
	 * @return 
	 * 	status code and the result value if the process is successfully.
	 *
	 * Return status code
	 * 		1)  100: Process successfully.
	 * 		2) -100: Invalid expression; 
	 * 		3) -200: Using variable with value not been specified yet. 
	 * 		4) -300: Result beyond the integer min value.
	 * 		5) -400: Result beyond the integer max value.
	 * 		6) -500: Can not divided by zero.
	 * 
	 */
	@SuppressWarnings("deprecation")
	private int[] processOperation(String exp, int operationType, Map<String, Integer> map) {
		int[] arrRtn = new int[2];
		int argA = 0;
		int argB = 0;
		
		if(exp.length()<8) { // would be an invalid expression
			LOGGER.error(exp + " is not a valid expression.");
			arrRtn[0] = -100;
			return arrRtn;
		}
		
		int argAstartPos = operationType==OPERATION_MULT?5:4;
		int commasPos = exp.indexOf(",");
		int argBstartPos = 0;
		int argBendPos = exp.length() - 1;
		
		// Populate argA
		char ch = exp.charAt(argAstartPos);
		if(Character.isDigit(ch)||ch=='-') { // argument is numeric
			String numStr = exp.substring(argAstartPos, commasPos);
			if(!NumberUtils.isNumber(numStr)) { // Would be invalid exp.
				LOGGER.error(exp + " is not a valid expression.");
				arrRtn[0] = -100;
				return arrRtn;
			}
			
			argA = Integer.valueOf(numStr);
			argBstartPos = commasPos + 1;
		}else { // argument value will be obtained from map or expression.
			if(commasPos == argAstartPos + 1) { // Obtaining argument value from the map (assuming variable name always single character).
				String argAkey = String.valueOf(exp.charAt(argAstartPos));
				if(!map.containsKey(argAkey)) { // throw exception for using variable with value not been specified yet.
					LOGGER.error("There's no value been specified for variable '" + argAkey + "'.");
					arrRtn[0] = -200;
					return arrRtn;
				}
				
				argA = map.get(argAkey);
				argBstartPos = commasPos + 1;
			}else { // Obtaining argument value from the exp.
				int pEnd = getCurrentOperationClosePosition(exp, argAstartPos);
				if(pEnd==-100) { // exp not valid
					arrRtn[0] = -100;
					return arrRtn;
				}
				
				String expSub = exp.substring(argAstartPos, pEnd);
				
				arrRtn = operationDispatch(expSub, map);
				
				if(arrRtn[0]!=100) // Called operationDispatch and exception to be thrown.
					return arrRtn;
				
				argA = arrRtn[1];
				argBstartPos = pEnd + 1;
			}
		}
		
		// Populate argB
		ch = exp.charAt(argBstartPos);
		if(Character.isDigit(ch)||ch=='-') { // argument is numeric
			String numStr = exp.substring(argBstartPos, argBendPos);
			if(!NumberUtils.isNumber(numStr)) { // Would be invalid exp.
				LOGGER.error(exp + " is not a valid expression.");
				arrRtn[0] = -100;
				return arrRtn;
			}
			
			argB = Integer.valueOf(numStr);
		}else { // argument value will be obtained from map or expression.
			if(argBendPos == argBstartPos + 1) { // Obtaining argument value from the map (assuming variable name always be a single character).
				String argBkey = String.valueOf(exp.charAt(argBstartPos));
				if(!map.containsKey(argBkey)) { // throw exception for using variable with value not been specified yet.
					LOGGER.error("There's no value been specified for variable '" + argBkey + "'.");
					arrRtn[0] = -200;
					return arrRtn;
				}
				
				argB = map.get(argBkey);
			}else { // Obtaining argument value from the exp.
				int pEnd = getCurrentOperationClosePosition(exp, argBstartPos);
				if(pEnd==-100) { // exp not valid
					arrRtn[0] = -100;
					return arrRtn;
				}
				
				String expSub = exp.substring(argBstartPos, pEnd);
				
				arrRtn = operationDispatch(expSub, map);
				
				if(arrRtn[0]!=100) // Called operationDispatch and exception to be thrown.
					return arrRtn;
				
				argB = arrRtn[1];
			}
		}
		
		switch (operationType) {
			case OPERATION_ADD:
				arrRtn = add(argA, argB);
				break;
			case OPERATION_SUB:
				arrRtn = sub(argA, argB);
				break;
			case OPERATION_MULT:
				arrRtn = mult(argA, argB);
				break;
			case OPERATION_DIV:
				arrRtn = div(argA, argB);
				break;
			default:
					//Nothing to do here.
		}
		
		return arrRtn;
	}
	
	/**
	 * Process operation of let.
	 * 
	 * @param exp
	 * @param map
	 * @return
	 * 	status code and the result value if the process is successfully.
	 * 
	 * Return status code
	 * 		1)  100: Process successfully.
	 * 		2) -100: Invalid expression; 
	 * 		3) -200: Using variable with value not been specified yet. 
	 * 		4) -300: Result beyond the integer min value.
	 * 		5) -400: Result beyond the integer max value.
	 * 		6) -500: Can not divided by zero.
	 */
	@SuppressWarnings("deprecation")
	private int[] processLetOperation(String exp, Map<String, Integer> map) {
		int[] arrRtn = new int[2];
		
		// Let operation would be at least 17 in length not including the space since spaces been remove in mainOperation, exp: let(a,5,add(a,a))
		if(exp.length()<17) { 
			LOGGER.error(exp + " is not a valid expression.");
			arrRtn[0] = -100;
			return arrRtn;
		}
		
		// Considering no space removed already, assuming all the variable name would be single character, the 6th character must be ",".
		if(exp.charAt(5)!=',') { // Would be invalid exp.
			LOGGER.error(exp + " is not a valid expression.");
			arrRtn[0] = -100;
			return arrRtn;
		}
		
		String var = String.valueOf(exp.charAt(4));
		
		char ch = exp.charAt(6);
		int operStartPos = 0;
		int operEndPos = exp.length() - 1;
		
		// map variable from an numeric value.
		if(Character.isDigit(ch)||ch=='-') {
			int commasPos = exp.indexOf(",", 6);
			String strNum = exp.substring(6, commasPos);
			
			if(!NumberUtils.isNumber(strNum)) { // invalid exp
				LOGGER.error(exp + " is not a valid expression.");
				arrRtn[0] = -100;
				return arrRtn;
			}
			
			map.put(var, Integer.valueOf(strNum));
			operStartPos = commasPos + 1;
			
		// map variable from exp calculation.
		}else {
			int expEndPos = getCurrentOperationClosePosition(exp, 6);
			if(expEndPos==-100) { // exp not valid
				arrRtn[0] = -100;
				return arrRtn;
			}

			String expSub = exp.substring(6, expEndPos);
			
			arrRtn = operationDispatch(expSub, map);
			
			if(arrRtn[0]!=100) // Called operationDispatch and exception to be thrown.
				return arrRtn;

			map.put(var, arrRtn[1]);
			operStartPos = expEndPos + 1;
		}
		
		//Now calculate the value of the let operation.
		String expLetOper = exp.substring(operStartPos, operEndPos);
		
		arrRtn = operationDispatch(expLetOper, map);
		
		if(arrRtn[0]!=100) // Called operationDispatch and exception to be thrown.
			return arrRtn;
		
		return arrRtn;
	}

	/**
	 *  Return the position of current operation end
	 *  
	 *  Example: add(let(<start pos>a, 5, add(a, a))<end pos>, mult(2, 3))
	 *  
	 *  end position of let operation above is <end pos> that is the position of ",".
	 */
	private int getCurrentOperationClosePosition(String exp, int pos) {
		Stack<Character> stk = new Stack<>();
		int posMove = exp.indexOf("(", pos);
		
		if(posMove==-1) { // exp not valid
			LOGGER.error(exp + " is not a valid expression.");
			return -100;
		}
		
		stk.push('(');
		posMove++;
		
		while(!stk.isEmpty()) {
			if(exp.charAt(posMove)=='(')
				stk.add('(');
			else if(exp.charAt(posMove)==')')
				stk.pop();
			
			posMove++;
			if(!stk.isEmpty()&&posMove>=exp.length()) {
				LOGGER.error(exp + " is not a valid expression.");
				return -100;
			}
		}
		
		return posMove;
	}
	
	/**
	 * @return 
	 * 	return empty string "" if the exp is valid otherwise return the string value of reason of whey the exp invalid.
	 * 
	 */
	private String validateExpression(String exp) {
		if(StringUtils.isEmpty(exp))
			return "Expression is emty.";
		
		Stack<Character> stk = new Stack<>();
		
		for(char ch: exp.toCharArray()) {
			if(ch=='(')
				stk.push(ch);
			else if(ch==')') {
				if(stk.isEmpty()) {
					return "Expression is invalid because unbalance parenthesis.";
				}
				stk.pop();
			}
			else {
				if((ch - '0')>=0&&(ch - '0')<=9)
					continue;
				if((ch - 'a')>=0&&(ch - 'a')<=25)
					continue;
				if(ch==','||ch=='-')
					continue;
				
				return "Expression is invalid because '" + ch + "' is an invalid character.";
			}
		}
		
		if(!stk.isEmpty())
			return "Expression is invalid because unbalance parenthesis.";
		
		return "";
	}

	public static void Main(String[] args) throws Exception {
		String arg = args[0];
		
		Calculator calculator = new Calculator();
		
		System.out.println(calculator.mainOperation(arg));
	}
}
