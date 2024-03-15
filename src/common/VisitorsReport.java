package common;

import java.io.Serializable;
import java.time.LocalDate;

public class VisitorsReport extends Report implements Serializable {

	private static final long serialVersionUID = 1L;

    private Integer totalIndividualVisitors;
    private Integer totalGroupVisitors;
    private Integer totalFamilyVisitors;
    private Integer totalVisitors;

    public VisitorsReport(int reportID, ReportType reportType, int parkID, LocalDate date, String comment,
                          Integer totalIndividualVisitors, Integer totalGroupVisitors, Integer totalFamilyVisitors, Integer totalVisitors) {
        super(reportID, reportType, parkID, date, comment);
        this.totalIndividualVisitors = totalIndividualVisitors;
        this.totalGroupVisitors = totalGroupVisitors;
        this.totalFamilyVisitors = totalFamilyVisitors;
        this.totalVisitors = totalVisitors;
    }
    // Getters and Setters
   

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

    // ToString method updated to include new fields
    @Override
    public String toString() {
        return "VisitorsReport{" +
                "reportID=" + getReportID() +
                ", parkID=" + getParkID() +
                ", date=" + getDate() +
                ", comment='" + getComment() + '\'' +
                ", totalIndividualVisitors=" + totalIndividualVisitors +
                ", totalGroupVisitors=" + totalGroupVisitors +
                ", totalFamilyVisitors=" + totalFamilyVisitors +
                ", totalVisitors=" + totalVisitors +
                '}';
    }
}
