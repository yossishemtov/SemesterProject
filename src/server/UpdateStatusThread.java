package server;

import java.time.Duration;
import java.time.LocalTime;

import DB.DatabaseController;

/**
 * A thread designed to update database statuses at specific times. It handles
 * updates for both a waiting list and order statuses, performing these updates
 * daily at 8:00 AM and 11:00 PM respectively.
 */
public class UpdateStatusThread implements Runnable {

	private DatabaseController DC;

	/**
	 * Constructs an {@code UpdateStatusThread} with a specified
	 * {@link DatabaseController}.
	 *
	 * @param DBController the database controller used for updating database
	 *                     records.
	 */
	public UpdateStatusThread(DatabaseController DBController) {
		this.DC = DBController;
	}

	/**
	 * The main execution method for the thread. This runs in a continuous loop,
	 * determining the next target time for an update, sleeping until it's close to
	 * that time, and then performing the necessary update.
	 */
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {

				LocalTime now = LocalTime.now();
				LocalTime timeForWaitingListUpdate = LocalTime.of(8, 0); // 8 AM
				LocalTime timeForOrderUpdate = LocalTime.of(23, 0); // 11 PM

				LocalTime nextTarget = getNextTarget(now, timeForWaitingListUpdate, timeForOrderUpdate);
				sleepUntilCloseTo(nextTarget);
				while (LocalTime.now().isBefore(nextTarget)) {
					Thread.sleep(100);
				}
				performActionForTime(nextTarget, timeForWaitingListUpdate, timeForOrderUpdate);
			}

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			System.out.println("Thread was interrupted, stopping.");
		}
	}

	/**
	 * Determines the next target time for an update based on the current time.
	 *
	 * @param now                      the current time
	 * @param timeForWaitingListUpdate the time scheduled for updating the waiting
	 *                                 list
	 * @param timeForOrderUpdate       the time scheduled for updating order
	 *                                 statuses
	 * @return the next target time for an update
	 */
	private LocalTime getNextTarget(LocalTime now, LocalTime timeForWaitingListUpdate, LocalTime timeForOrderUpdate) {
		if (now.isBefore(timeForWaitingListUpdate) || now.isAfter(timeForOrderUpdate)) {
			return timeForWaitingListUpdate;
		} else {
			System.out.println("Thread timeForOrderUpdate.");
			return timeForOrderUpdate;
		}
	}

	/**
	 * Sleeps the thread until it's close to the next target time for an update.
	 *
	 * @param targetTime the next target time to wake up for an update
	 * @throws InterruptedException if the thread is interrupted while sleeping
	 */
	private void sleepUntilCloseTo(LocalTime targetTime) throws InterruptedException {
        LocalTime now = LocalTime.now();
        long sleepTimeMillis;

        if (now.isAfter(targetTime)) { // Means target is effectively tomorrow
            sleepTimeMillis = Duration.between(now, LocalTime.MAX).plusSeconds(1).toMillis() // Sleep until just after midnight
                             + Duration.between(LocalTime.MIN, targetTime).toMillis(); // Then add time until target next day
        } else {
            sleepTimeMillis = Duration.between(now, targetTime.minusMinutes(1)).toMillis();
        }

        if (sleepTimeMillis > 0) {
            Thread.sleep(sleepTimeMillis);
        }
    }

	/**
	 * Performs the appropriate update action based on the target time.
	 *
	 * @param targetTime               the time the update is targeted for
	 * @param timeForWaitingListUpdate the time scheduled for updating the waiting
	 *                                 list
	 * @param timeForOrderUpdate       the time scheduled for updating order
	 *                                 statuses
	 */
	private void performActionForTime(LocalTime targetTime, LocalTime timeForWaitingListUpdate,
			LocalTime timeForOrderUpdate) {
		if (targetTime.equals(timeForWaitingListUpdate)) {
			DC.updateWaitingListStatusForToday();
		} else if (targetTime.equals(timeForOrderUpdate)) {
			DC.updateOrderStatusForToday();
		}
	}
}
