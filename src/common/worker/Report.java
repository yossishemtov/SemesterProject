package common.worker;

import java.io.Serializable;
import java.time.LocalDate;

public class Report implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum ReportType {
        USAGE, VISITOR,
    }

    private Integer reportID;
    private ReportType reportType;
    private int parkID;
    private LocalDate date;
    private int month; // New field
    private String comment;

    // Constructors
    public Report(Integer reportID, ReportType reportType, int parkID, LocalDate date, int month, String comment) {
        this.reportID = reportID;
        this.reportType = reportType;
        this.parkID = parkID;
        this.date = date;
        this.month = month; // Set the month
        this.comment = comment;
    }

    protected Report() {
    }

    // Getters and Setters for all fields including the new month field
    public Integer getReportID() {
        return reportID;
    }

    public void setReportID(Integer reportID) {
        this.reportID = reportID;
    }

    public String getReportType() {
        return reportType.name();
    }

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

    public int getMonth() { // Getter for month
        return month;
    }

    public void setMonth(int month) { // Setter for month
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
        return "Report{" +
                "reportID=" + reportID +
                ", reportType=" + reportType +
                ", parkID=" + parkID +
                ", date=" + date +
                ", month=" + month + // Include month in toString
                ", comment='" + comment + '\'' +
                '}';
    }
}
