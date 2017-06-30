package sdc.assets.annotations.base;

import sdc.assets.annotations.Complexity;
import sdc.assets.annotations.Complexity.ComplexityLevel;

@Complexity(ComplexityLevel.SIMPLE)
public class SimpleAnnotationsTest {

	public SimpleAnnotationsTest() {
		super();
	}

	public void theMethod() {
		System.out.println("console output");
	}

	public static void main(String cheese[]) {
		System.out.println("Yo!");
	}
}