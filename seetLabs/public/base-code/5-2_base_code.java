//define your Animal, Mammal, Dog, and Cat classes here

public class Lab {
	public static static void main(String[] args) {
		Animal d = new Dog("Rex");
		Animal c = new Cat("Whiskers");
		return d.makeSound() + " " + c.makeSound();
	}
}
