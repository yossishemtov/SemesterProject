package common;

public class VisitorsReport {
    private int totalIndividualVisitors;
    private int totalGroupVisitors;
    private int totalFamilyVisitors; // New field for family visitors

    // Updated constructor
    public VisitorsReport(int totalIndividualVisitors, int totalGroupVisitors, int totalFamilyVisitors) {
        this.totalIndividualVisitors = totalIndividualVisitors;
        this.totalGroupVisitors = totalGroupVisitors;
        this.totalFamilyVisitors = totalFamilyVisitors; // Initialize the new field
    }

    // Getters and Setters
    public int getTotalIndividualVisitors() {
        return totalIndividualVisitors;
    }

    public void setTotalIndividualVisitors(int totalIndividualVisitors) {
        this.totalIndividualVisitors = totalIndividualVisitors;
    }

    public int getTotalGroupVisitors() {
        return totalGroupVisitors;
    }

    public void setTotalGroupVisitors(int totalGroupVisitors) {
        this.totalGroupVisitors = totalGroupVisitors;
    }

    public int getTotalFamilyVisitors() { // Getter for the new field
        return totalFamilyVisitors;
    }

    public void setTotalFamilyVisitors(int totalFamilyVisitors) { // Setter for the new field
        this.totalFamilyVisitors = totalFamilyVisitors;
    }
}
