package common.worker;

import java.io.Serializable;

/**
 * ParkManager class extends the GeneralParkWorker class to include specific functionalities
 * related to park management such as creating reports and handling change requests.
 */
public class ParkManager extends GeneralParkWorker implements Serializable {
    private static final String ROLE = "Park Manager";

    /**
     * Constructs a ParkManager with the specified details.
     *
     * @param workerId    Unique identifier for the worker.
     * @param firstName   Worker's first name.
     * @param lastName    Worker's last name.
     * @param email       Worker's email address.
     * @param userName    Worker's username for system access.
     * @param password    Worker's password for system access.
     * @param worksAtPark ID of the park where the worker is employed.
     */
    public ParkManager(Integer workerId, String firstName, String lastName, String email,String userName, String password, Integer worksAtPark) {
        super(workerId, firstName, lastName, email, ROLE, userName, password, worksAtPark);
    }

    /**
     * Creates reports specific to the management of the park.
     */
    public void createReport() {
        // Implementation for creating park-specific reports
    }

    /**
     * Handles requests for changes within the park.
     */
    public void changeRequest() {
        // Implementation for handling change requests
    }
}
