package common;

import java.time.LocalDate;
import java.time.LocalTime;

public class Visit {
	private LocalDate visitDate;
	private LocalTime enteringTime;
	private LocalTime existingTime;
	
	
	public Visit(LocalDate visitDate, LocalTime enteringTime, LocalTime existingTime) {
		this.visitDate = visitDate;
		this.enteringTime = enteringTime;
		this.existingTime = existingTime;
	}


	public LocalDate getVisitDate() {
		return visitDate;
	}


	public void setVisitDate(LocalDate visitDate) {
		this.visitDate = visitDate;
	}


	public LocalTime getEnteringTime() {
		return enteringTime;
	}


	public void setEnteringTime(LocalTime enteringTime) {
		this.enteringTime = enteringTime;
	}


	public LocalTime getExistingTime() {
		return existingTime;
	}


	public void setExistingTime(LocalTime existingTime) {
		this.existingTime = existingTime;
	}
	
}
