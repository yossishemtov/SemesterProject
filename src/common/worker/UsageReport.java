package common.worker;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class UsageReport extends Report implements Serializable {
    private static final long serialVersionUID = 1L;
    // Map to hold day of the month and the usage on that day
    private Map<Integer, Integer> dailyUsage;
    private int parkCapacity; // Field to hold the park's capacity

    // Constructor
    public UsageReport(Integer reportID, ReportType reportType, int parkID, LocalDate date, int month, String comment, Map<Integer, Integer> dailyUsage, int parkCapacity) {
        super(reportID, reportType, parkID, date, month, comment); // Call to super class constructor
        this.dailyUsage = dailyUsage != null ? new HashMap<>(dailyUsage) : new HashMap<>();
        this.parkCapacity = parkCapacity; // Initialize parkCapacity
    }

    // Default constructor
    protected UsageReport() {
        super(); // Call to super class default constructor
        this.dailyUsage = new HashMap<>();
        // Default capacity could be set here if applicable, e.g., this.parkCapacity = defaultCapacity;
    }

    // Getter and Setter for dailyUsage
    public Map<Integer, Integer> getDailyUsage() {
        return dailyUsage;
    }

    public void setDailyUsage(Map<Integer, Integer> dailyUsage) {
        this.dailyUsage = dailyUsage != null ? new HashMap<>(dailyUsage) : new HashMap<>();
    }

    // Getter and setter for parkCapacity
    public int getParkCapacity() {
        return parkCapacity;
    }

    public void setParkCapacity(int parkCapacity) {
        this.parkCapacity = parkCapacity;
    }

    // Method to add or update the usage for a specific day
    public void updateDailyUsage(int day, int usage) {
        this.dailyUsage.put(day, usage);
    }

    @Override
    public String toString() {
        return "UsageReport{" +
                "reportID=" + getReportID() +
                ", reportType=" + getReportType() +
                ", parkID=" + getParkID() +
                ", date=" + getDate() +
                ", month=" + getMonth() +
                ", comment='" + getComment() + '\'' +
                ", dailyUsage=" + dailyUsage +
                ", parkCapacity=" + parkCapacity +
                '}';
    }
}
