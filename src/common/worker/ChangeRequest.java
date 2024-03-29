package common.worker;

import java.io.Serializable;

public class ChangeRequest implements Serializable {

	private static final long serialVersionUID = 1L;
    private Integer id; 
    private String parkName;
    private Integer parkNumber;
    private Integer capacity;
    private Integer gap; 
    private Integer staytime;
    private Integer oldGap; 

    

    
    private ApprovalStatus approved; // Using ApprovalStatus enum

    // Define the ApprovalStatus enum outside of any methods but inside the class
    public enum ApprovalStatus {
        REJECTED, APPROVAL, WAITING_FOR_APPROVAL;

    	public static ApprovalStatus fromString(String status) {
    	    String normalizedStatus = status.toUpperCase().replace(" ", "_");
    	    switch (normalizedStatus) {
    	        case "REJECTED":
    	            return REJECTED;
    	        case "APPROVAL":
    	            return APPROVAL;
    	        case "WAITING_FOR_APPROVAL":
    	            return WAITING_FOR_APPROVAL;
    	        default:
    	            throw new IllegalArgumentException("Unknown approval status: " + status);
    	    }
    	}

        // Convert an ApprovalStatus enum to a string
        @Override
        public String toString() {
            switch (this) {
                case REJECTED:
                    return "REJECTED";
                case APPROVAL:
                    return "APPROVAL";
                case WAITING_FOR_APPROVAL:
                    return "WAITING_FOR_APPROVAL";
                default:
                    throw new IllegalArgumentException("Unknown approval status");
            }
        }
    }

    // Constructor
    public ChangeRequest(Integer id, String parkName, Integer parkNumber, Integer capacity, Integer gap, Integer staytime, String approved) {
        this.id = id; // Initialize in constructor
        this.parkName = parkName;
        this.parkNumber = parkNumber;
        this.capacity = capacity;
        this.gap = gap;
        this.staytime = staytime;
        this.approved = ApprovalStatus.fromString(approved);
    }
    

    // Getters and Setters
    public String getParkName() {
        return parkName;
    }
    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public Integer getParkNumber() {
        return parkNumber;
    }

    public void setParkNumber(Integer parkNumber) {
        this.parkNumber = parkNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getGap() {
        return gap;
    }

    public void setGap(Integer gap) {
        this.gap = gap;
    }
    public Integer getOldGap() {
        return oldGap;
    }

    public void setOldGap(Integer gap) {
        this.oldGap = gap;
    }

    public Integer getStaytime() {
        return staytime;
    }

    public void setStaytime(Integer staytime) {
        this.staytime = staytime;
    }

    public String getApproved() {
        return approved.toString(); // Convert ApprovalStatus to String
    }

    public void setApproved(String approved) {
        this.approved = ApprovalStatus.fromString(approved); // Convert String to ApprovalStatus
    }

    @Override
    public String toString() {
        return "ChangeRequest{" +
                "parkName='" + parkName + '\'' +
                ", parkNumber=" + parkNumber +
                ", capacity=" + capacity +
                ", gap=" + gap +
                ", staytime=" + staytime +
                ", approved=" + approved +
                '}';
    }
}
