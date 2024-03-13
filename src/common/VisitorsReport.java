package common;

import java.io.Serializable;

public class VisitorsReport implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L; // Unique version identifier
    private Integer totalIndividualVisitors;
    private Integer totalGroupVisitors;
    private Integer totalFamilyVisitors; // New field for family visitors
    private Integer totalVisitors; // Field for total visitors

    // Updated constructor
    public VisitorsReport(Integer totalIndividualVisitors, Integer totalGroupVisitors, Integer totalFamilyVisitors, Integer totalVisitors) {
        this.totalIndividualVisitors = totalIndividualVisitors;
        this.totalGroupVisitors = totalGroupVisitors;
        this.totalFamilyVisitors = totalFamilyVisitors; // Initialize the new field
        this.totalVisitors = totalVisitors; // Initialize total visitors
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

    public Integer getTotalFamilyVisitors() { // Getter for the new field
        return totalFamilyVisitors;
    }

    public void setTotalFamilyVisitors(Integer totalFamilyVisitors) { // Setter for the new field
        this.totalFamilyVisitors = totalFamilyVisitors;
    }

    public Integer getTotalVisitors() { // Getter for total visitors
        return totalVisitors;
    }

    public void setTotalVisitors(Integer totalVisitors) { // Setter for total visitors
        this.totalVisitors = totalVisitors;
    }

    // ToString method
    @Override
    public String toString() {
        return "VisitorsReport{" +
                "totalIndividualVisitors=" + totalIndividualVisitors +
                ", totalGroupVisitors=" + totalGroupVisitors +
                ", totalFamilyVisitors=" + totalFamilyVisitors +
                ", totalVisitors=" + totalVisitors +
                '}';
    }
}
