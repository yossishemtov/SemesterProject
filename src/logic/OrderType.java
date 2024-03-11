package logic;

/*
 * OrderType enum represent the order types that are available in the park.
 */

public enum OrderType {
	SINGLE, FAMILY, GROUP;

	@Override
	public String toString() {
		switch (this) {
		case SINGLE:
			return "Single Visit";
		case FAMILY:
			return "Family Visit";
		case GROUP:
			return "Group Visit";
		default:
			throw new IllegalArgumentException();
		}
	}
}
