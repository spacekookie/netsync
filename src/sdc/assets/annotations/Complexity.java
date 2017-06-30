package sdc.assets.annotations;

public @interface Complexity {
	public enum ComplexityLevel {
		VERY_SIMPLE, SIMPLE, MEDIUM, COMPLEX, VERY_COMPLEX;
	}

	ComplexityLevel value();
}