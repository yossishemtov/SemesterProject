package common.worker;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a usage report for a park, detailing daily usage statistics and
 * the park's capacity. This class extends the {@link Report} class to provide
 * specific data fields and methods related to the usage of park facilities.
 */
public class UsageReport extends Report implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * A map holding the daily usage data, with day of the month as keys and usage
	 * numbers as values.
	 */
	private Map<Integer, Integer> dailyUsage;

	/**
	 * The maximum capacity of the park. This field is used to gauge the extent of
	 * usage relative to potential capacity.
	 */
	private int parkCapacity;

	/**
	 * Constructs a new UsageReport with detailed information.
	 *
	 * @param reportID     The unique identifier for the report.
	 * @param reportType   The type of report, which should be USAGE for instances
	 *                     of this class.
	 * @param parkID       The ID of the park to which the report is related.
	 * @param date         The date relevant to the report's data.
	 * @param month        The month for which the report provides data.
	 * @param comment      Any additional comments or notes associated with the
	 *                     report.
	 * @param dailyUsage   A map of daily usage data with days as keys and usage
	 *                     counts as values.
	 * @param parkCapacity The maximum capacity of the park.
	 */
	public UsageReport(Integer reportID, ReportType reportType, int parkID, LocalDate date, int month, String comment,
			Map<Integer, Integer> dailyUsage, int parkCapacity) {
		super(reportID, reportType, parkID, date, month, comment);
		this.dailyUsage = dailyUsage != null ? new HashMap<>(dailyUsage) : new HashMap<>();
		this.parkCapacity = parkCapacity;
	}

	/**
	 * Default constructor for usage report.
	 */
	protected UsageReport() {
		super();
		this.dailyUsage = new HashMap<>();
	}

	/**
	 * Returns the daily usage map.
	 *
	 * @return A map containing the daily usage data.
	 */
	public Map<Integer, Integer> getDailyUsage() {
		return dailyUsage;
	}

	/**
	 * Sets the daily usage map.
	 *
	 * @param dailyUsage A map containing the daily usage data.
	 */
	public void setDailyUsage(Map<Integer, Integer> dailyUsage) {
		this.dailyUsage = dailyUsage != null ? new HashMap<>(dailyUsage) : new HashMap<>();
	}

	/**
	 * Returns the park's capacity.
	 *
	 * @return The capacity of the park.
	 */
	public int getParkCapacity() {
		return parkCapacity;
	}

	/**
	 * Sets the park's capacity.
	 *
	 * @param parkCapacity The capacity of the park to set.
	 */
	public void setParkCapacity(int parkCapacity) {
		this.parkCapacity = parkCapacity;
	}

	/**
	 * Updates the usage data for a specific day of the month.
	 *
	 * @param day   The day of the month.
	 * @param usage The usage count for the specified day.
	 */
	public void updateDailyUsage(int day, int usage) {
		this.dailyUsage.put(day, usage);
	}

	@Override
	public String toString() {
		return "UsageReport{" + "reportID=" + getReportID() + ", reportType=" + getReportType() + ", parkID="
				+ getParkID() + ", date=" + getDate() + ", month=" + getMonth() + ", comment='" + getComment() + '\''
				+ ", dailyUsage=" + dailyUsage + ", parkCapacity=" + parkCapacity + '}';
	}
}
