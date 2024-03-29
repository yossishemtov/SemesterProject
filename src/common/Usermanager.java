package common;

import java.io.Serializable;

import client.ClientUI;
import common.worker.GeneralParkWorker;
import gui.OrderAVisitController;


/**
 * The Usermanager class manages the current user sessions in the system.
 * It keeps track of the currently logged-in general park worker and traveler.
 */
public class Usermanager implements Serializable {

    private static final long serialVersionUID = 1L;

    private static GeneralParkWorker currentWorker;
    private static Traveler currentTraveler;
    // Flags to indicate whether a worker or traveler is connected
    private static boolean isWorkerConnected = false;
    private static boolean isTravelerConnected = false;	
	private static boolean isNewTraveler;


	/**
     * Retrieves the currently logged-in general park worker.
     *
     * @return The currently logged-in general park worker.
     */
    public static GeneralParkWorker getCurrentWorker() {
        return currentWorker; 
    }

    /**
     * Sets the currently logged-in general park worker.
     *
     * @param worker The general park worker to set as currently logged-in.
     */
    public static void setCurrentWorker(GeneralParkWorker worker) {
        Usermanager.currentWorker = worker;
        // Set the worker connected flag based on whether the worker is null
        Usermanager.isWorkerConnected = (worker != null);
    }

    /**
     * Retrieves the currently logged-in traveler.
     *
     * @return The currently logged-in traveler.
     */
    public static Traveler getCurrentTraveler() {
        return currentTraveler;
    }

    /**
     * Sets the currently logged-in traveler.
     *
     * @param traveler The traveler to set as currently logged-in.
     */
    public static void setCurrentTraveler(Traveler traveler) {
        Usermanager.currentTraveler = traveler;
        // Set the traveler connected flag based on whether the traveler is null
        Usermanager.isTravelerConnected = (traveler != null);
    }

    /**
     * Checks if a general park worker is currently connected.
     *
     * @return True if a general park worker is connected, false otherwise.
     */
    public static boolean isWorkerConnected() {
        return isWorkerConnected;
    }

    /**
     * Checks if a traveler is currently connected.
     *
     * @return True if a traveler is connected, false otherwise.
     */
    public static boolean isTravelerConnected() {
        return isTravelerConnected;
    }

    /**
     * Checks if a new traveler is being registered.
     *
     * @return True if a new traveler is being registered, false otherwise.
     */
	public static boolean GetisNewTraveler() {
		return isNewTraveler;
	}

	/**
     * Sets the flag indicating if a new traveler is being registered.
     *
     * @param isNewTraveler The flag to set indicating if a new traveler is being registered.
     */
	public static void setNewTraveler(boolean isNewTraveler) {
		Usermanager.isNewTraveler = isNewTraveler;
	}
}