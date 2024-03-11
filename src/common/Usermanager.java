package common;
import common.worker.GeneralParkWorker;

import java.io.Serializable;

import common.Traveler;

public class Usermanager  implements Serializable {

	private static final long serialVersionUID = 1L;

	  private static GeneralParkWorker currentWorker;
	    private static Traveler currentTraveler;

	    public static GeneralParkWorker getCurrentWorker() {
	        return currentWorker;
	    }

	    public static void setCurrentWorker(GeneralParkWorker worker) {
	    	Usermanager.currentWorker = worker;
	    }

	    public static Traveler getCurrentTraveler() {
	        return currentTraveler;
	    }

	    public static void setCurrentTraveler(Traveler traveler) {
	    	Usermanager.currentTraveler = traveler;
	    }
	    
	}
