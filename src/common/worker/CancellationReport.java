package common.worker;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a report detailing daily cancellations and unfulfilled orders for a specific park and month.
 * This class is designed to aggregate and provide information that can be used for analyzing the
 * operational efficiency and customer satisfaction levels.
 */
public class CancellationReport implements Serializable {
    private static final long serialVersionUID = 1L;

    // Maps to store the counts of daily cancellations and unfulfilled orders
    private Map<Integer, Integer> dailyCancellations = new HashMap<>();
    private Map<Integer, Integer> dailyUnfulfilledOrders = new HashMap<>();
    
    // Identifiers for the park and month the report pertains to
    private Integer parkNumber;
    private Integer monthNumber;

    /**
     * Constructs a new CancellationReport for a specific park and month.
     *
     * @param parkNumber  the park's identifying number
     * @param monthNumber the month number (1 for January, 2 for February, etc.)
     */
    public CancellationReport(Integer parkNumber, Integer monthNumber) {
        this.parkNumber = parkNumber;
        this.monthNumber = monthNumber;
    }

    /**
     * Sets the entire map of daily cancellations. Existing data is replaced.
     *
     * @param dailyCancellations a map with days of the month as keys and cancellation counts as values
     */
    public void setDailyCancellations(Map<Integer, Integer> dailyCancellations) {
        this.dailyCancellations = new HashMap<>(dailyCancellations);
    }

    /**
     * Sets the entire map of daily unfulfilled orders. Existing data is replaced.
     *
     * @param dailyUnfulfilledOrders a map with days of the month as keys and unfulfilled order counts as values
     */
    public void setDailyUnfulfilledOrders(Map<Integer, Integer> dailyUnfulfilledOrders) {
        this.dailyUnfulfilledOrders = new HashMap<>(dailyUnfulfilledOrders);
    }

    /**
     * Records a cancellation on a specific day of the month. The count for that day is incremented by one.
     *
     * @param dayOfMonth the day of the month on which the cancellation occurred
     */
    public void recordCancellation(int dayOfMonth) {
        dailyCancellations.merge(dayOfMonth, 1, Integer::sum);
    }

    /**
     * Sets the number of cancellations for a specific day of the month.
     *
     * @param dayOfMonth the day of the month
     * @param count      the count of cancellations
     */
    public void setCancellationsForDay(int dayOfMonth, int count) {
        dailyCancellations.put(dayOfMonth, count);
    }

    /**
     * Records an unfulfilled order on a specific day of the month. The count for that day is incremented by one.
     *
     * @param dayOfMonth the day of the month on which the unfulfilled order occurred
     */
    public void recordUnfulfilledOrder(int dayOfMonth) {
        dailyUnfulfilledOrders.merge(dayOfMonth, 1, Integer::sum);
    }

    /**
     * Sets the number of unfulfilled orders for a specific day of the month.
     *
     * @param dayOfMonth the day of the month
     * @param count      the count of unfulfilled orders
     */
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
