package logic;

/**
 * This class is responsible on the system's order status.
 */
public enum OrderStatus {
	CONFIRMED, PENDING , CANCELED, PENDING_EMAIL_SENT, WAITING, WAITING_HAS_SPOT, ENTERED_THE_PARK,COMPLETED;

	@Override
	public String toString() {
		switch (this) {
		case CONFIRMED:
			return "Order Confirmed";
		case PENDING:
			return "Order is Pending";
		case CANCELED:
			return "Order Canceled";
		case WAITING:
			return "Waiting";
		case ENTERED_THE_PARK:
			return "Entered the park";
		case PENDING_EMAIL_SENT:
			return "Pending, email has been sent";
		case WAITING_HAS_SPOT:
			return "Waiting has available spot";
		case COMPLETED:
			return "Visit has been completed";
		default:
			throw new IllegalArgumentException();
		}
	}
}