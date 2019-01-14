# synopsys
Synopsys home work done by candidate Brian R.

A calculator program in Java that evaluates expressions in a very simple integer expression language. The program takes an input on the command line, computes the result, and prints it to the console.  For example:
% java calculator.Main "add(2, 2)"
4
Few more examples:
Input	Output
add(1, 2)	3
add(1, mult(2, 3))	7
mult(add(2, 2), div(9, 3))	12
let(a, 5, add(a, a))	10
let(a, 5, let(b, mult(a, 10), add(b, a)))	55
let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))	40
