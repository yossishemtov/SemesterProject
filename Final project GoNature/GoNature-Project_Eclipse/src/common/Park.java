package common;

import java.io.Serializable;

/**
 * The Park class represents a park entity.
 * It contains attributes such as name, park number, maximum visitors allowed, current visitors,
 * capacity, location, stay time, number of workers, manager ID, gap, and working time.
 */
public class Park  implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private Integer parkNumber;
	private Integer maxVisitors;
	private Integer capacity;
	private Integer currentVisitors;
	private String location;
	private Integer staytime;
	private Integer workersAmount;
	private Integer managerID;
	private Integer gap;
	private Integer workingTime;
	private Integer unorderedVisits;
	
	/**
     * Constructs a new Park object with the specified parameters.
     *
     * @param name          The name of the park.
     * @param parkNumber    The unique number assigned to the park.
     * @param maxVisitors   The maximum number of visitors allowed in the park.
     * @param capacity      The capacity of the park.
     * @param currentVisitors The current number of visitors in the park.
     * @param location      The location of the park.
     * @param staytime      The duration of stay allowed in the park.
     * @param workersAmount The number of workers in the park.
     * @param gap           The gap parameter indicating the allowed difference in capacity.
     * @param managerID     The ID of the park manager.
     * @param workingTime   The working time of the park.
     */
	public Park(String name, Integer parkNumber, Integer maxVisitors, Integer capacity, Integer currentVisitors,
			String location, Integer staytime, Integer workersAmount,Integer gap, Integer managerID, Integer workingTime) {
		super();
		this.name = name;
		this.parkNumber = parkNumber;
		this.maxVisitors = maxVisitors;
		this.capacity = capacity;
		this.currentVisitors = currentVisitors;
		this.location = location;
		this.staytime = staytime;
		this.gap = gap; 
		this.workersAmount = workersAmount;
		this.managerID = managerID;
		this.workingTime = workingTime;
	}
	
    public Integer getGap() {
        return gap;
    }

    public void setGap(Integer gap) {
        this.gap = gap;
    }
	public Integer getUnorderedVisits() {
		return unorderedVisits;
	}

	public void setUnorderedVisits(Integer unorderedVisits) {
		this.unorderedVisits = unorderedVisits;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParkNumber() {
		return parkNumber;
	}
	public void setParkNumber(Integer parkNumber) {
		this.parkNumber = parkNumber;
	}
	public Integer getMaxVisitors() {
		return maxVisitors;
	}
	public void setMaxVisitors(Integer maxVisitors) {
		this.maxVisitors = maxVisitors;
	}
	public Integer getCapacity() {
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	public Integer getCurrentVisitors() {
		return currentVisitors;
	}
	public void setCurrentVisitors(Integer currentVisitors) {
		this.currentVisitors = currentVisitors;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Integer getStaytime() {
		return staytime;
	}
	public void setStaytime(Integer staytime) {
		staytime = staytime;
	}
	public Integer getWorkersAmount() {
		return workersAmount;
	}
	public void setWorkersAmount(Integer workersAmount) {
		this.workersAmount = workersAmount;
	}
	public Integer getManagerid() {
		return managerID;
	}
	public void setManagerid(Integer managerid) {
		this.managerID = managerid;
	}
	public Integer getWorkingTime() {
		return workingTime;
	}
	public void setWorkingTime(Integer workingTime) {
		this.workingTime = workingTime;
	}
	
    @Override
    public String toString() {
        return "Park{" +
                "name='" + name + '\'' +
                ", parkNumber=" + parkNumber +
                ", maxVisitors=" + maxVisitors +
                ", capacity=" + capacity +
                ", currentVisitors=" + currentVisitors +
                ", location='" + location + '\'' +
                ", staytime=" + staytime +
                ", workersAmount=" + workersAmount +
                ", managerID=" + managerID +
                ", gap=" + gap + // Include 'gap' in the toString representation
                ", workingTime=" + workingTime +
                '}';
    }
	
}
