package common.worker;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a report of all visits to a specific park during a given month.
 */
public class VisitReport implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<VisitData> visits = new ArrayList<>();
    private Integer parkNumber;
    private Integer monthNumber;

    /**
     * Defines the type of visit/order.
     */
    public enum TypeOfOrder {
        SOLO, FAMILY, GUIDEDGROUP
    }

    /**
     * Constructs a VisitReport with specified park number and month.
     *
     * @param parkNumber  The unique identifier of the park.
     * @param monthNumber The month for which the report is being generated.
     */
    public VisitReport(Integer parkNumber, Integer monthNumber) {
        this.parkNumber = parkNumber;
        this.monthNumber = monthNumber;
    }

    /**
     * Adds a visit to the report.
     *
     * @param enteringTimeStr The time of entry in HH:MM format.
     * @param durationMinutes The duration of the visit in minutes.
     * @param typeOfOrderStr  The type of visit/order (SOLO, FAMILY, GUIDEDGROUP).
     */
    public void addVisit(String enteringTimeStr, long durationMinutes, String typeOfOrderStr) {
        LocalTime enteringTime = LocalTime.parse(enteringTimeStr); // Convert string to LocalTime
        TypeOfOrder typeOfOrder = convertStringToTypeOfOrder(typeOfOrderStr); // Convert string to enum
        visits.add(new VisitData(enteringTime, durationMinutes, typeOfOrder)); // Add to the list
    }

    /**
     * Converts a string to the corresponding TypeOfOrder enum.
     *
     * @param typeOfOrderStr The string representation of the TypeOfOrder.
     * @return The TypeOfOrder enum or SOLO as a default.
     */
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

    // Getters and setters with brief JavaDoc comments

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
