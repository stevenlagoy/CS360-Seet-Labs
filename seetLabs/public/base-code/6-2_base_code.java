public class Lab {
	// define your divide method here


	public static String runTests(int a, int b) {
		try {
			int result = divide(a, b);
			return String.valueOf(result);
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}
	}
	public static void main(String[] args) {
		runTests();
	}
}
