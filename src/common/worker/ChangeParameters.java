package common.worker;

import java.time.LocalDate;

public class ChangeParameters {
	private Integer oldParameter;
	private Integer newParameter;
	private Integer parkNumber;
	private LocalDate Date;
	private enum status{
		CONFIRMED,
		CANCELED,
		PENDING
	}
	
	
	public ChangeParameters(Integer oldParameter, Integer newParameter, Integer parkNumber, LocalDate date) {
		super();
		this.oldParameter = oldParameter;
		this.newParameter = newParameter;
		this.parkNumber = parkNumber;
		Date = date;
	}


	public Integer getOldParameter() {
		return oldParameter;
	}


	public void setOldParameter(Integer oldParameter) {
		this.oldParameter = oldParameter;
	}


	public Integer getNewParameter() {
		return newParameter;
	}


	public void setNewParameter(Integer newParameter) {
		this.newParameter = newParameter;
	}


	public Integer getParkNumber() {
		return parkNumber;
	}


	public void setParkNumber(Integer parkNumber) {
		this.parkNumber = parkNumber;
	}


	public LocalDate getDate() {
		return Date;
	}


	public void setDate(LocalDate date) {
		Date = date;
	}
	
	
}
