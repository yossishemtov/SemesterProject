package common.worker;

import java.io.Serializable;

public class DepartmentManager extends GeneralParkWorker implements Serializable {

	private static final long serialVersionUID = 1L;
    // Assuming the 'role' for DepartmentManager is predefined or set as a constant.
    private static final String ROLE = "Department Manager";

    public DepartmentManager(Integer workerId, String firstName, String lastName, String email,
                             String userName, String password, Integer worksAtPark) {
        // Call the superclass constructor with all necessary fields including 'worksAtPark'.
        // The 'role' is set directly in the constructor call, as it's predefined for this class.
        super(workerId, firstName, lastName, email, ROLE, userName, password, worksAtPark);
    }
    @Override
    public String toString() {
        // Start with information from the GeneralParkWorker toString method
        // and then append any DepartmentManager-specific details
        return super.toString() + " | Role: " + ROLE; // Add any specific fields of DepartmentManager if there are any
    }

    public void approveChange() {
        // Implementation for approving changes
    }

    public void declineChange() {
        // Implementation for declining changes
    }

    public void createReport() {
        // Implementation for creating reports
    }
}
