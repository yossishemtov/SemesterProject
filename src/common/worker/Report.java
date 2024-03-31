package common.worker;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a detailed report related to park operations, including usage and
 * visitor statistics. This class is designed to encapsulate all the information
 * necessary for generating comprehensive reports on various aspects of park
 * management and visitor engagement.
 */
public class Report implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Enum defining the possible types of reports that can be generated.
	 */
	public enum ReportType {
		USAGE, // A report focused on the usage statistics of the park's facilities.
		VISITOR // A report detailing visitor demographics, numbers, and trends.
	}

	private Integer reportID; // Unique identifier for the report.
	private ReportType reportType; // The type of report.
	private int parkID; // The ID of the park to which this report is related.
	private LocalDate date; // The date when the report was generated or relevant for.
	private int month; // The month for which the report is relevant, providing temporal context.
	private String comment; // Additional comments or notes associated with the report.

	/**
	 * Constructs a new Report instance with the specified details.
	 *
	 * @param reportID   The unique identifier for the report.
	 * @param reportType The type of report being generated.
	 * @param parkID     The ID of the park to which the report is related.
	 * @param date       The date relevant to the report's data.
	 * @param month      The month relevant to the report's data.
	 * @param comment    Any additional comments or notes associated with the
	 *                   report.
	 */
	public Report(Integer reportID, ReportType reportType, int parkID, LocalDate date, int month, String comment) {
		this.reportID = reportID;
		this.reportType = reportType;
		this.parkID = parkID;
		this.date = date;
		this.month = month;
		this.comment = comment;
	}

	/**
	 * Protected no-argument constructor for frameworks that require it for
	 * instantiation via reflection.
	 */
	protected Report() {
	}

	// Getters and setters for all fields

	/**
	 * Gets the unique report ID.
	 *
	 * @return The report's unique identifier.
	 */
	public Integer getReportID() {
		return reportID;
	}

	/**
	 * Sets the unique report ID.
	 *
	 * @param reportID The unique identifier to set for the report.
	 */
	public void setReportID(Integer reportID) {
		this.reportID = reportID;
	}

	/**
	 * Gets the report type as a string.
	 *
	 * @return The name of the report type.
	 */
	public String getReportType() {
		return reportType.name();
	}

	/**
	 * Sets the report type.
	 *
	 * @param reportType The type of report.
	 */
	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public int getParkID() {
		return parkID;
	}

	public void setParkID(int parkID) {
		this.parkID = parkID;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Report{" + "reportID=" + reportID + ", reportType=" + reportType + ", parkID=" + parkID + ", date="
				+ date + ", month=" + month + // Include month in toString
				", comment='" + comment + '\'' + '}';
	}
}
