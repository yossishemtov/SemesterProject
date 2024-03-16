package common;

import java.io.Serializable;

public class HourlyVisitData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String hour;
    private int guidedGroupCount;
    private int familyCount;
    private int soloCount;

    // Constructor
    public HourlyVisitData(String hour, int guidedGroupCount, int familyCount, int soloCount) {
        this.hour = hour;
        this.guidedGroupCount = guidedGroupCount;
        this.familyCount = familyCount;
        this.soloCount = soloCount;
    }

    // Getters
    public String getHour() {
        return hour;
    }

    public int getGuidedGroupCount() {
        return guidedGroupCount;
    }

    public int getFamilyCount() {
        return familyCount;
    }

    public int getSoloCount() {
        return soloCount;
    }

    // Setters
    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setGuidedGroupCount(int guidedGroupCount) {
        this.guidedGroupCount = guidedGroupCount;
    }

    public void setFamilyCount(int familyCount) {
        this.familyCount = familyCount;
    }

    public void setSoloCount(int soloCount) {
        this.soloCount = soloCount;
    }
    @Override
    public String toString() {
        return "HourlyVisitData{" +
                "hour='" + hour + '\'' +
                ", guidedGroupCount=" + guidedGroupCount +
                ", familyCount=" + familyCount +
                ", soloCount=" + soloCount +
                '}';
    }

}
