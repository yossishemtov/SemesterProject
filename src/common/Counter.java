package common;

public class Counter {
    private static Counter obj;
    private int count;
    private int waitlistId;

    private Counter() {
        count = 0;
        waitlistId = 0;
    }

    public static synchronized Counter getCounter() {
        if (obj == null) {
            obj = new Counter(); // Create a new instance if it doesn't exist
        }
        return obj;
    }

    public synchronized int placeInList() {
        return count++;
    }

    public synchronized int waitlistId() {
        return waitlistId++;
    }
}
