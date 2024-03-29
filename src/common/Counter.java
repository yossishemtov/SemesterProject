package common;

/**
 * Singleton class to manage counters for waitinglist.
 */
public class Counter {
	
	/**
	 * The singleton instance of the Counter class.
	 */
    private static Counter obj;
    
    /**
     * The count value.
     */
    private int count;
    
    /**
     * The waitlist ID value.
     */
    private int waitlistId;

    /**
     * Private constructor to initialize the count and waitlist ID.
     */
    private Counter() {
        count = 0;
        waitlistId = 0;
    }

    /**
     * Retrieves the singleton instance of the Counter class.
     *
     * @return the singleton instance of the Counter class
     */
    public static synchronized Counter getCounter() {
        if (obj == null) {
            obj = new Counter(); // Create a new instance if it doesn't exist
        }
        return obj;
    }

    /**
     * Increments the count and returns the new value.
     *
     * @return the new value of the count
     */
    public synchronized int placeInList() {
        return count++;
    }

    /**
     * Retrieves the next waitlist ID and increments the counter.
     *
     * @return the next waitlist ID
     */
    public synchronized int waitlistId() {
        return waitlistId++;
    }
}
