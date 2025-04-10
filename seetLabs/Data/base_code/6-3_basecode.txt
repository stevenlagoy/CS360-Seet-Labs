public class Lab {

	public static int divide(int a, int b) {
		if (b == 0) {
			throw new IllegalArgumentException("Cannot divide by zero");
		}
		return a / b;
	}

	public static String runTests(int a, int b) {
		try {
			int result = divide(a, b);
			return String.valueOf(result);
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}
	}
}
