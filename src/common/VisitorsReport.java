package common;

import java.time.LocalDate;

public class VisitorsReport extends Report {

    private static final long serialVersionUID = 1L;

    private Integer totalIndividualVisitors;
    private Integer totalGroupVisitors;
    private Integer totalFamilyVisitors;
    private Integer totalVisitors;

    public VisitorsReport(Integer reportID, ReportType reportType, int parkID, LocalDate date, int month, String comment,
                          Integer totalIndividualVisitors, Integer totalGroupVisitors, Integer totalFamilyVisitors, Integer totalVisitors) {
        super(reportID, reportType, parkID, date, month, comment); // Pass month to the superclass constructor
        this.totalIndividualVisitors = totalIndividualVisitors;
        this.totalGroupVisitors = totalGroupVisitors;
        this.totalFamilyVisitors = totalFamilyVisitors;
        this.totalVisitors = totalVisitors;
    }
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
        // Update toString to call super.toString() and append VisitorsReport specific fields
        return super.toString() + 
               ", VisitorsReport{" +
               "totalIndividualVisitors=" + totalIndividualVisitors +
               ", totalGroupVisitors=" + totalGroupVisitors +
               ", totalFamilyVisitors=" + totalFamilyVisitors +
               ", totalVisitors=" + totalVisitors +
               '}';
    }
}
