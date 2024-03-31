package common.worker;

import java.io.Serializable;

/**
 * Represents a change request for a park's operational parameters such as
 * capacity, gap between visits, and stay time. This class is used to manage
 * changes and approvals related to park operations.
 */
public class ChangeRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	// Fields represent the attributes of a change request
	private Integer id;
	private String parkName;
	private Integer parkNumber;
	private Integer capacity;
	private Integer gap;
	private Integer staytime;
	private Integer oldGap;
	
	/**
	 * Enum for approval status
	 */
	private ApprovalStatus approved; // Enum for approval status

	/**
	 * Approval statuses for a change request.
	 */
	public enum ApprovalStatus {
		/**
	     * Indicates that the change request has been rejected.
	     */
		REJECTED,
		/**
	     * Indicates that the change request has been approved.
	     */
		APPROVAL, 
		/**
	     * Indicates that the change request is waiting for approval.
	     */
		WAITING_FOR_APPROVAL;

		/**
		 * Converts a string representation of an approval status to its corresponding
		 * enum value.
		 *
		 * @param status The string representation of the approval status.
		 * @return The corresponding ApprovalStatus enum value.
		 */
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

		@Override
		public String toString() {
			// Provides a string representation for logging or display purposes
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

	/**
	 * Constructs a new ChangeRequest with specified details.
	 *
	 * @param id         Unique identifier for the change request.
	 * @param parkName   Name of the park.
	 * @param parkNumber Numerical identifier for the park.
	 * @param capacity   Proposed new capacity for the park.
	 * @param gap        Proposed new gap between visits.
	 * @param staytime   Proposed new stay time for visitors.
	 * @param approved   Initial approval status as a string.
	 */
	public ChangeRequest(Integer id, String parkName, Integer parkNumber, Integer capacity, Integer gap,
			Integer staytime, String approved) {
		this.id = id;
		this.parkName = parkName;
		this.parkNumber = parkNumber;
		this.capacity = capacity;
		this.gap = gap;
		this.staytime = staytime;
		this.approved = ApprovalStatus.fromString(approved); // Convert string to enum
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
		return "ChangeRequest{" + "parkName='" + parkName + '\'' + ", parkNumber=" + parkNumber + ", capacity="
				+ capacity + ", gap=" + gap + ", staytime=" + staytime + ", approved=" + approved + '}';
	}
}
