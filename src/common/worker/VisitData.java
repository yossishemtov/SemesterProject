package common.worker;

import java.io.Serializable;
import java.time.LocalTime;

import common.worker.VisitReport.TypeOfOrder;

public class VisitData implements Serializable {
    private static final long serialVersionUID = 1L;
	private LocalTime enteringTime;
	private long durationMinutes;
	private TypeOfOrder typeOfOrder;

	public VisitData(LocalTime enteringTime, long durationMinutes, TypeOfOrder typeOfOrder) {
		this.enteringTime = enteringTime;
		this.durationMinutes = durationMinutes;
		this.typeOfOrder = typeOfOrder;
	}

	// Getters
	public LocalTime getEnteringTime() {
		return enteringTime;
	}

	public long getDurationMinutes() {
		return durationMinutes;
	}

	public TypeOfOrder getTypeOfOrder() {
		return typeOfOrder;
	}
	 @Override
	    public String toString() {
	        return "Type of Order: " + typeOfOrder +
	               ", Entering Time: " + enteringTime +
	               ", Duration: " + durationMinutes + " minutes";
	    }
}
