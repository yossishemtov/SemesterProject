package common.worker;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class VisitReport implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<VisitData> visits = new ArrayList<>();
    private Integer parkNumber;
    private Integer monthNumber;

    public enum TypeOfOrder {
        SOLO, FAMILY, GUIDEDGROUP
    }

    // Constructor
    public VisitReport(Integer parkNumber, Integer monthNumber) {
        this.parkNumber = parkNumber;
        this.monthNumber = monthNumber;
    }

    // Method to add a visit to the report
    public void addVisit(String enteringTimeStr, long durationMinutes, String typeOfOrderStr) {
        LocalTime enteringTime = LocalTime.parse(enteringTimeStr); // Convert string to LocalTime
        TypeOfOrder typeOfOrder = convertStringToTypeOfOrder(typeOfOrderStr); // Convert string to enum
        visits.add(new VisitData(enteringTime, durationMinutes, typeOfOrder)); // Add to the list
    }

    // Convert typeOfOrder string to TypeOfOrder enum
    private TypeOfOrder convertStringToTypeOfOrder(String typeOfOrderStr) {
        if (typeOfOrderStr != null) {
            try {
                return TypeOfOrder.valueOf(typeOfOrderStr.toUpperCase().trim());
            } catch (IllegalArgumentException e) {
                return TypeOfOrder.SOLO; // Default to SOLO if conversion fails
            }
        }
        return TypeOfOrder.SOLO; // Default to SOLO if input is null
    }

    public Integer getParkNumber() { return parkNumber; }
    public void setParkNumber(Integer parkNumber) { this.parkNumber = parkNumber; }
    
    public Integer getMonthNumber() { return monthNumber; }
    public void setMonthNumber(Integer monthNumber) { this.monthNumber = monthNumber; }

    public List<VisitData> getVisits() {
        return new ArrayList<>(this.visits);
    }

    @Override
    public String toString() {
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Visit Report for Park Number: ").append(parkNumber)
                      .append(", Month: ").append(monthNumber).append("\n");
        reportBuilder.append("Total Visits: ").append(visits.size()).append("\n");
        
        for (VisitData visit : visits) {
            reportBuilder.append(visit.toString()).append("\n");
        }

        return reportBuilder.toString();
    }
}
