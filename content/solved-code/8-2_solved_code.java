import java.util.*;

public class Lab {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
	
		// Read space-separated integers from user
		// Store integers in a list
		ArrayList<Integer> list = new ArrayList<>();
		while (scanner.hasNext()) {
			String input = scanner.nextLine();
			list.add(Integer.parseInt(input));
		}
		
		// Sort the list
		list.sort(null);
		
		// Convert list to set to remove duplicates
		Set<Integer> set = Set.copyOf(list);

		// Print sorted list and unique set
		System.out.println(list.toString());
		System.out.println(set.toString());

		scanner.close();
	}
}
