package common.worker;

import java.time.LocalDate;
import common.worker.Report.ReportType;

/**
 * Represents a report detailing the types and total number of visitors to a
 * park within a specific period.
 */
public class VisitorsReport extends Report {

	private static final long serialVersionUID = 1L;

	private Integer totalIndividualVisitors;
	private Integer totalGroupVisitors;
	private Integer totalFamilyVisitors;
	private Integer totalVisitors;

	/**
	 * Constructs a VisitorsReport with detailed visitor statistics.
	 *
	 * @param reportID                The unique identifier of the report.
	 * @param reportType              The type of the report.
	 * @param parkID                  The ID of the park the report is about.
	 * @param date                    The date of the report.
	 * @param month                   The month the report covers.
	 * @param comment                 Any additional comments about the report.
	 * @param totalIndividualVisitors The total number of individual visitors.
	 * @param totalGroupVisitors      The total number of visitors coming in groups.
	 * @param totalFamilyVisitors     The total number of visitors coming as
	 *                                families.
	 * @param totalVisitors           The total number of visitors to the park.
	 */
	public VisitorsReport(Integer reportID, ReportType reportType, int parkID, LocalDate date, int month,
			String comment, Integer totalIndividualVisitors, Integer totalGroupVisitors, Integer totalFamilyVisitors,
			Integer totalVisitors) {
		super(reportID, reportType, parkID, date, month, comment);
		this.totalIndividualVisitors = totalIndividualVisitors;
		this.totalGroupVisitors = totalGroupVisitors;
		this.totalFamilyVisitors = totalFamilyVisitors;
		this.totalVisitors = totalVisitors;
	}

	// Getter and setter methods with JavaDoc

	/**
	 * Returns the total number of individual visitors.
	 *
	 * @return The total number of individual visitors.
	 */
	public Integer getTotalIndividualVisitors() {
		return totalIndividualVisitors;
	}

	public void setTotalIndividualVisitors(Integer totalIndividualVisitors) {
		this.totalIndividualVisitors = totalIndividualVisitors;
	}

	public Integer getTotalGroupVisitors() {
		return totalGroupVisitors;
	}

	public void setTotalGroupVisitors(Integer totalGroupVisitors) {
		this.totalGroupVisitors = totalGroupVisitors;
	}

	public Integer getTotalFamilyVisitors() {
		return totalFamilyVisitors;
	}

	public void setTotalFamilyVisitors(Integer totalFamilyVisitors) {
		this.totalFamilyVisitors = totalFamilyVisitors;
	}

	public Integer getTotalVisitors() {
		return totalVisitors;
	}

	public void setTotalVisitors(Integer totalVisitors) {
		this.totalVisitors = totalVisitors;
	}

	@Override
	public String toString() {
		// Update toString to call super.toString() and append VisitorsReport specific
		// fields
		return super.toString() + ", VisitorsReport{" + "totalIndividualVisitors=" + totalIndividualVisitors
				+ ", totalGroupVisitors=" + totalGroupVisitors + ", totalFamilyVisitors=" + totalFamilyVisitors
				+ ", totalVisitors=" + totalVisitors + '}';
	}
}
