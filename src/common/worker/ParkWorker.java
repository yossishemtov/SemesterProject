package common.worker;

import java.io.Serializable;

public class ParkWorker extends GeneralParkWorker implements Serializable{

	public ParkWorker(Integer workerNumber, String firstName, String lastName, String email, String role,
			String userName, String password,Integer worksAtPark) {
		super(workerNumber, firstName, lastName, email, role, userName, password, worksAtPark);
		// TODO Auto-generated constructor stub
	}

	public void checkAvailableSpace() {
		
	}
}
