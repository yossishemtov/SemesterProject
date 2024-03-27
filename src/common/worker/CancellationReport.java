package common.worker;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CancellationReport implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<Integer, Integer> dailyCancellations = new HashMap<>();
	private Map<Integer, Integer> dailyUnfulfilledOrders = new HashMap<>();
	private Integer parkNumber;
	private Integer monthNumber;

	// Constructor
	public CancellationReport(Integer parkNumber, Integer monthNumber) {
		this.parkNumber = parkNumber;
		this.monthNumber = monthNumber;
	}
	   // Set the entire daily cancellations map
    public void setDailyCancellations(Map<Integer, Integer> dailyCancellations) {
        this.dailyCancellations = new HashMap<>(dailyCancellations);
    }

    // Set the entire daily unfulfilled orders map
    public void setDailyUnfulfilledOrders(Map<Integer, Integer> dailyUnfulfilledOrders) {
        this.dailyUnfulfilledOrders = new HashMap<>(dailyUnfulfilledOrders);
    }

	// Record a cancellation 
	public void recordCancellation(int dayOfMonth) {
		dailyCancellations.merge(dayOfMonth, 1, Integer::sum);
	}

	// Set cancellations for a specific day
	public void setCancellationsForDay(int dayOfMonth, int count) {
		dailyCancellations.put(dayOfMonth, count);
	}

	// Record an unfulfilled order
	public void recordUnfulfilledOrder(int dayOfMonth) {
		dailyUnfulfilledOrders.merge(dayOfMonth, 1, Integer::sum);
	}

	// Set unfulfilled orders for a specific day
	public void setUnfulfilledOrdersForDay(int dayOfMonth, int count) {
		dailyUnfulfilledOrders.put(dayOfMonth, count);
	}

	public Integer getParkNumber() {
		return parkNumber;
	}

	public void setParkNumber(Integer parkNumber) {
		this.parkNumber = parkNumber;
	}

	public Integer getMonthNumber() {
		return monthNumber;
	}

	public void setMonthNumber(Integer monthNumber) {
		this.monthNumber = monthNumber;
	}

	public Map<Integer, Integer> getDailyCancellations() {
		return new HashMap<>(this.dailyCancellations);
	}

	public Map<Integer, Integer> getDailyUnfulfilledOrders() {
		return new HashMap<>(this.dailyUnfulfilledOrders);
	}

	@Override
	public String toString() {
		StringBuilder reportBuilder = new StringBuilder();
		reportBuilder.append("Cancellation and Unfulfilled Orders Report for Park Number: ").append(parkNumber)
				.append(", Month: ").append(monthNumber).append("\n");

		reportBuilder.append("Cancellations:\n");
		dailyCancellations.forEach((day, count) -> reportBuilder.append("Day ").append(day).append(": ").append(count)
				.append(" cancellations\n"));

		reportBuilder.append("Unfulfilled Orders:\n");
		dailyUnfulfilledOrders.forEach((day, count) -> reportBuilder.append("Day ").append(day).append(": ")
				.append(count).append(" unfulfilled orders\n"));

		return reportBuilder.toString();
	}
}
