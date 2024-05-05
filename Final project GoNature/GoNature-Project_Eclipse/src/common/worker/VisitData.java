package common.worker;

import java.io.Serializable;
import java.time.LocalTime;

import common.worker.VisitReport.TypeOfOrder;

/**
 * Represents the data for a single visit to a park, including the time of
 * entry, the duration of the visit, and the type of order under which the visit
 * was made.
 */
public class VisitData implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * The time at which the visit began.
	 */
	private LocalTime enteringTime;

	/**
	 * The duration of the visit in minutes.
	 */
	private long durationMinutes;

	/**
	 * The type of order (e.g., reservation, walk-in) under which the visit was
	 * made.
	 */
	private TypeOfOrder typeOfOrder;

	/**
	 * Constructs a new VisitData instance with specified details of the visit.
	 *
	 * @param enteringTime    The time the visit started.
	 * @param durationMinutes The duration of the visit in minutes.
	 * @param typeOfOrder     The category of the visit, defined by the TypeOfOrder
	 *                        enum.
	 */
	public VisitData(LocalTime enteringTime, long durationMinutes, TypeOfOrder typeOfOrder) {
		this.enteringTime = enteringTime;
		this.durationMinutes = durationMinutes;
		this.typeOfOrder = typeOfOrder;
	}

	/**
	 * Returns the time the visit began.
	 *
	 * @return The entering time of the visit.
	 */
	public LocalTime getEnteringTime() {
		return enteringTime;
	}

	/**
	 * Returns the duration of the visit in minutes.
	 *
	 * @return The duration of the visit.
	 */
	public long getDurationMinutes() {
		return durationMinutes;
	}

	/**
	 * Returns the type of order under which the visit was made.
	 *
	 * @return The type of the order.
	 */
	public TypeOfOrder getTypeOfOrder() {
		return typeOfOrder;
	}

	@Override
	public String toString() {
		return "Type of Order: " + typeOfOrder + ", Entering Time: " + enteringTime + ", Duration: " + durationMinutes
				+ " minutes";
	}
}
