package common;

import java.io.Serializable;
import java.time.LocalDate;

public class Report implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum ReportType {
        USAGE, VISITOR,
    }

    private int reportID;
    private ReportType reportType;
    private int parkID;
    private LocalDate date;
    private String comment;

    // Constructors
    public Report(int reportID, ReportType reportType, int parkID, LocalDate date, String comment) {
        this.reportID = reportID;
        this.reportType = reportType;
        this.parkID = parkID;
        this.date = date;
        this.comment = comment;
    }

    // Assume default constructor for inheritance
    protected Report() {
    }

    // Getters and Setters
    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
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
                ", comment='" + comment + '\'' +
                '}';
    }
}
