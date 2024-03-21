package common;

import java.io.Serializable;

import client.ClientUI;
import common.worker.GeneralParkWorker;
import gui.OrderAVisitController;

public class Usermanager implements Serializable {

    private static final long serialVersionUID = 1L;

    private static GeneralParkWorker currentWorker;
    private static Traveler currentTraveler;
    // Flags to indicate whether a worker or traveler is connected
    private static boolean isWorkerConnected = false;
    private static boolean isTravelerConnected = false;	
	private static boolean isNewTraveler=false;


	
    public static GeneralParkWorker getCurrentWorker() {
        return currentWorker; 
    }

    public static void setCurrentWorker(GeneralParkWorker worker) {
        Usermanager.currentWorker = worker;
        // Set the worker connected flag based on whether the worker is null
        Usermanager.isWorkerConnected = (worker != null);
    }

    public static Traveler getCurrentTraveler() {
        return currentTraveler;
    }

    public static void setCurrentTraveler(Traveler traveler) {
        Usermanager.currentTraveler = traveler;
        // Set the traveler connected flag based on whether the traveler is null
        Usermanager.isTravelerConnected = (traveler != null);
    }

    // Methods to check if a worker or traveler is connected
    public static boolean isWorkerConnected() {
        return isWorkerConnected;
    }

    public static boolean isTravelerConnected() {
        return isTravelerConnected;
    }

	public static boolean GetisNewTraveler() {
		return isNewTraveler;
	}

	public static void setNewTraveler(boolean isNewTraveler) {
		Usermanager.isNewTraveler = isNewTraveler;
	}
}