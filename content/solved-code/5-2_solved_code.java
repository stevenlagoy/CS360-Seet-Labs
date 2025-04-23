interface Animal {
	String makeSound();
}

abstract class Mammal implements Animal {
	String name;

	Mammal(String name) {
		this.name = name;
	}
}

class Dog extends Mammal {
	Dog(String name) {
		super(name);
	}

	public String makeSound() {
		return "Woof";
	}
}

class Cat extends Mammal {
	Cat(String name) {
		super(name);
	}

	public String makeSound() {
		return "Meow";
	}
}

public class Lab {
	public static static void main(String[] args) {
		Animal d = new Dog("Rex");
		Animal c = new Cat("Whiskers");
		return d.makeSound() + " " + c.makeSound();
	}
}
