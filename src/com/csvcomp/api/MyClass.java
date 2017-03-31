package com.csvcomp.api;

public class MyClass {

	public static void main(String args[]) {
		Animal a = new Dog();
		a.bark();
		// a.test();
	}

}

class Animal {
	void eatSomething() {
		System.out.println("animal eating something");
	}
}

class Dog extends Animal {
	// @Override
	
	String color= "brown";
	
	void bark() {
		System.out.println("dog barks");
	}
	
	
	
}