package server;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JOptionPane;

import DB.DatabaseController;
import common.Order;
import common.OrderNotification;
import common.WaitingList;

/**
 * NotifyThread class implements Runnable.
 * 
 * This class handle all the automated functionality: Send reminder 24 hours
 * before visit. Cancel the visit if the traveler did not confirmed within two
 * hours. Notify the next in the waiting list
 *
 */
public class NotifyThread implements Runnable {

	private final int second = 1000;
	private final int minute = second * 30;
	private static DatabaseController DC;

	public static DatabaseController getDC() {
		return DC;
	}

	private ArrayList<OrderNotification> ordersWithAlerts; // Array to store orders with alerts
	private ArrayList<WaitingList> waitingArray;

	public NotifyThread(DatabaseController DBController) {
		DC = DBController;
		ordersWithAlerts = new ArrayList<>(); // Initialize the array
		waitingArray = new ArrayList<>();
	}

	/**
	 * This function handle all the automated functionality: Send reminder 24 hours
	 * before visit. Cancel the visit if the traveler did not confirmed within two
	 * hours. Notify the next in the waiting list
	 */
	@Override
	public void run() {

		while (true) {

			ArrayList<Order> pendingOrders = DC.getOrdersByStatusInLastTwentyFourHours("PENDING");
			ArrayList<OrderNotification> ordersAlreadyNotified = DC.getTodayNotificationsWithStatus("SENT");

			for (Order order : pendingOrders) {
				String status = "PENDING_EMAIL_SENT";
				String orderId = String.valueOf(order.getOrderId());
				order.setStatus(status);
				// Change status of orders to PENDING_EMAIL_SENT
				DC.updateOrderStatus(order);

				/** Visitor has 2 hours to accept visit from now */
				OrderNotification createNotification = new OrderNotification(Integer.parseInt(orderId), LocalDate.now(),
						LocalTime.now(), LocalTime.now().plusHours(2), "SENT");

				// Add notification to the orders notified arrayList
				ordersAlreadyNotified.add(createNotification);

				// Posting the notification in the db
				DC.postOrderNotification(createNotification);

				WaitingList waiting = new WaitingList(order.getOrderId(), order.getVisitorId(), order.getParkNumber(),
						order.getAmountOfVisitors(), order.getPrice(), order.getVisitorEmail(), order.getDate(),
						order.getVisitTime(), order.getOrderStatus(), order.getTypeOfOrder(),
						order.getTelephoneNumber(), order.getParkName(), 0, 0);
				waitingArray.add(waiting);

			}

			ordersWithAlerts = ordersAlreadyNotified;
			CancelOrderAndNotify(waitingArray);
			deleteAlertsExpired();

			try {
				Thread.sleep(1 * minute);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void CancelOrderAndNotify(ArrayList<WaitingList> waiting) {

		// Iterate through orders with alerts and cancel expired orders
		Iterator<OrderNotification> iterator = ordersWithAlerts.iterator();
		while (iterator.hasNext()) {
			int i = 0;
			OrderNotification notificationOfSpecificOrder = iterator.next();
			if (isAlertExpired(LocalTime.now(), notificationOfSpecificOrder.getEndNotification())) {
				// Cancel the order
				System.out.print("Cancel Order");

				// Change status in the orderNotification table
				DC.changeStatusOfNotification(notificationOfSpecificOrder.getOrderId(), "PASSED");

				// Change status in the order table
				DC.updateOrderStatusArray(new ArrayList<String>(
						Arrays.asList("CANCELEDBYSERVER", String.valueOf(notificationOfSpecificOrder.getOrderId()))));
				
					// Remove the canceled order from orderNotifications
					WaitingListControl.notifyPersonFromWaitingList(waiting.get(i));
					waiting.remove(0);
				
				iterator.remove();
			}
		}
	}

	private void deleteAlertsExpired() {
		DC.deleteExpiredOrderAlerts();
	}

	private boolean isAlertExpired(LocalTime currentTime, LocalTime alertEndTime) {
		return currentTime.isAfter(alertEndTime);
	}

}