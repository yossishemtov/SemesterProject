package logic;

import java.io.Serializable;

/**
 * Park class represent a park in the system.
 */
@SuppressWarnings("serial")
public class Park implements Serializable {
	private int parkId;
	private String parkName;
	private int maxVisitors;
	private int estimatedStayTime;
	private int gapBetweenMaxAndCapacity;
	private int currentVisitors;

	public Park(int parkId, String parkName, int maxVisitors, int estimatedStayTime, int gapBetweenMaxAndCapacity,
			int currentVisitors) {
		super();
		this.parkId = parkId;
		this.parkName = parkName;
		this.maxVisitors = maxVisitors;
		this.estimatedStayTime = estimatedStayTime;
		this.gapBetweenMaxAndCapacity = gapBetweenMaxAndCapacity;
		this.currentVisitors = currentVisitors;
	}

	public int getParkId() {
		return parkId;
	}

	public void setParkId(int parkId) {
		this.parkId = parkId;
	}

	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	public int getMaxVisitors() {
		return maxVisitors;
	}

	public void setMaxVisitors(int maxVisitors) {
		this.maxVisitors = maxVisitors;
	}

	public int getEstimatedStayTime() {
		return estimatedStayTime;
	}

	public void setEstimatedStayTime(int estimatedStayTime) {
		this.estimatedStayTime = estimatedStayTime;
	}

	public int getGapBetweenMaxAndCapacity() {
		return gapBetweenMaxAndCapacity;
	}

	public void setGapBetweenMaxAndCapacity(int gapBetweenMaxAndCapacity) {
		this.gapBetweenMaxAndCapacity = gapBetweenMaxAndCapacity;
	}

	public int getCurrentVisitors() {
		return currentVisitors;
	}

	public void setCurrentVisitors(int currentVisitors) {
		this.currentVisitors = currentVisitors;
	}

}
