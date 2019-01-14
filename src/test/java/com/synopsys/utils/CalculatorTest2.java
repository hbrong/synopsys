package test.java.com.synopsys.utils;

import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import main.java.com.synopsys.utils.Calculator;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 
 * @author brianr
 * 
 * Unit test for Calculator covering exception throwing and all the operations.
 *
 */
public class CalculatorTest {

	private Calculator calculator = null;
	private Exception exception = null;
	private int result = 0;

	@Before
	public void setUp() {
		BasicConfigurator.configure();
		calculator = new Calculator();
	}
	
	@Test
	public void testAdd() throws Exception {
		result = calculator.mainOperation("add(1, 1)");
		Assert.assertEquals(result, 2);
	}
	
	@Test
	public void testSub() throws Exception {
		result = calculator.mainOperation("sub(2, 1)");
		Assert.assertEquals(result, 1);
	}
	
	@Test
	public void testMult() throws Exception {
		result = calculator.mainOperation("mult(2, 2)");
		Assert.assertEquals(result, 4);
	}
	
	@Test
	public void testDiv() throws Exception {
		result = calculator.mainOperation("div(2, 2)");
		Assert.assertEquals(result, 1);
	}

	@Test
	public void testLet() throws Exception {
		result = calculator.mainOperation("let(a, 5, aDd(a, a))");
		Assert.assertEquals(result, 10);

		result = calculator.mainOperation("let(a, 5, add(5, a))");
		Assert.assertEquals(result, 10);

		result = calculator.mainOperation("let(a, let(a, mult(1, 5), add(a, 5)), add(5, a))");
		Assert.assertEquals(result, 15);

		result = calculator.mainOperation("let(a, 5, add(a, 5))");
		Assert.assertEquals(result, 10);

		result = calculator.mainOperation("let(a, 5, let(b, mult(a, 10), add(b, a)))");
		Assert.assertEquals(result, 55);

		result = calculator.mainOperation("let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))");
		Assert.assertEquals(result, 40);
	}


	@Test
	public void testSubException() {
		exception = assertThrows(Exception.class, () -> calculator.mainOperation("sub(2147483647, -1)"));
		assertEquals("Result beyond the integer max value.", exception.getMessage());

		exception = assertThrows(Exception.class, () -> calculator.mainOperation("sub(-2147483648, 1)"));
		assertEquals("Result beyond the integer min value.", exception.getMessage());
	}

	@Test
	public void testMultException() {
		exception = assertThrows(Exception.class, () -> calculator.mainOperation("mult(2147483647, 2)"));
		assertEquals("Result beyond the integer max value.", exception.getMessage());

		exception = assertThrows(Exception.class, () -> calculator.mainOperation("mult(-2147483648, 2)"));
		assertEquals("Result beyond the integer min value.", exception.getMessage());
	}

	@Test
	public void testDivException() {
		exception = assertThrows(Exception.class, () -> calculator.mainOperation("div(1, 0)"));
		assertEquals("An operation of divided by zero.", exception.getMessage());
	}

	@Test
	public void testLetException() {
		exception = assertThrows(Exception.class, () -> calculator.mainOperation("let(a, 5, add{5, a})"));
		assertEquals("Expression is invalid because '{' is an invalid character.", exception.getMessage());


		// exception = assertThrows(Exception.class, () -> calculator.let("()())("));
		exception = assertThrows(Exception.class, () -> calculator.mainOperation("let()())("));
		assertEquals("Expression is invalid because unbalance parenthesis.", exception.getMessage());

		// exception = assertThrows(Exception.class, () -> calculator.let("())"));
		exception = assertThrows(Exception.class, () -> calculator.mainOperation("let(()))"));
		assertEquals("Expression is invalid because unbalance parenthesis.", exception.getMessage());

		// exception = assertThrows(Exception.class, () -> calculator.let("()("));
		exception = assertThrows(Exception.class, () -> calculator.mainOperation("let(()()"));
		assertEquals("Expression is invalid because unbalance parenthesis.", exception.getMessage());

		// aaa is not a valid operation
		exception = assertThrows(Exception.class, () -> calculator.mainOperation("a, 5, aaa(b, mult(a, 10), add(b, a))"));
		assertEquals("The expression is invalid.", exception.getMessage());

		// variable only one character
		exception = assertThrows(Exception.class, () -> calculator.mainOperation("let(ab, 5, add(b, mult(a, 10), add(b, a)))"));
		assertEquals("The expression is invalid.", exception.getMessage());

		exception = assertThrows(Exception.class, () -> calculator.mainOperation("let(a, 5, add(z, 5))"));
		assertEquals("No value been specified for the variable being used.", exception.getMessage());
	}
}
