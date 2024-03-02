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
	
	
}
