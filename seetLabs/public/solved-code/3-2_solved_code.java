public class Lab {
	public static int add(int a, int b) {
		// Add and return the sum
		return a + b;
	}

	public static double multiply(double a, double b) {
		// Multiply and return the product
		return a * b;
	}

	public static int factorial(int n) {
		// Return the factorial using recursion
		// base case:
		if (n <= 1) return 1;
		// recursive case:
		else {
			return n + factorial(n - 1);
		}
	}
}
