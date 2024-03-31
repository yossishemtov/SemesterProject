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
 * This class handle all the automated functionality:
 * Send reminder 24 hours before visit.
 * Cancel the visit if the traveler did not confirmed within two hours.
 * Notify the next in the waiting list
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

	public NotifyThread(DatabaseController DBController) {
		DC = DBController;
        ordersWithAlerts = new ArrayList<>(); // Initialize the array
	}

	/**
	 * This function handle all the automated functionality:
	 * Send reminder 24 hours before visit.
	 * Cancel the visit if the traveler did not confirmed within two hours.
	 * Notify the next in the waiting list
	 */
	@Override
	public void run() {

		while (true) {

			ArrayList<Order> pendingOrders = DC.getOrdersByStatusInLastTwentyFourHours("PENDING");
			ArrayList<OrderNotification> ordersAlreadyNotified= DC.getTodayNotificationsWithStatus("SENT");
			
			
			for (Order order : pendingOrders) {
				String status = "PENDING_EMAIL_SENT";
				String orderId = String.valueOf(order.getOrderId());
				order.setStatus(status);
				//Change status of orders to PENDING_EMAIL_SENT
				DC.updateOrderStatus(order);
				
				/**Visitor has 2 hours to accept visit from now*/
				OrderNotification createNotification = new OrderNotification(Integer.parseInt(orderId), LocalDate.now(),
						LocalTime.now(), LocalTime.now().plusHours(2), "SENT");
				
				//Add notification to the orders notified arrayList
				ordersAlreadyNotified.add(createNotification);
				
				//Posting the notification in the db
				DC.postOrderNotification(createNotification);
				
				
			}
			
			ordersWithAlerts = ordersAlreadyNotified;
			CancelOrderAndNotify();
			deleteAlertsExpired();
			findCanceledOrders();
			try {
				Thread.sleep(1 * minute);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	private void CancelOrderAndNotify() {
	    
	    // Iterate through orders with alerts and cancel expired orders
	    Iterator<OrderNotification> iterator = ordersWithAlerts.iterator();
	    while (iterator.hasNext()) {
	    	OrderNotification notificationOfSpecificOrder = iterator.next();
	        if (isAlertExpired(LocalTime.now(), notificationOfSpecificOrder.getEndNotification())) {
	            // Cancel the order
	        	System.out.print("Cancel Order");
	        	
	        	//Change status in the orderNotification table
	        	DC.changeStatusOfNotification(notificationOfSpecificOrder.getOrderId(), "PASSED");
	        	
	        	//Change status in the order table
	            DC.updateOrderStatusArray(new ArrayList<String>(Arrays.asList("CANCELEDBYSERVER", String.valueOf(notificationOfSpecificOrder.getOrderId()))));

	            // Remove the canceled order from orderNotifications
	            WaitingListControl.notifyPersonFromWaitingList(DC.getOrderbyId(notificationOfSpecificOrder.getOrderId()));

	            iterator.remove();
	        }
	    }
	}
	
	private void findCanceledOrders() {
		ArrayList<Order> canceledOrders = DC.getOrdersByStatusInLastTwentyFourHours("CANCELED");
		
		for (Order order : canceledOrders) {
            WaitingListControl.notifyPersonFromWaitingList(order);
		}

	}

	
	private void deleteAlertsExpired() {
	    DC.deleteExpiredOrderAlerts();
	}
	
	private boolean isAlertExpired(LocalTime currentTime, LocalTime alertEndTime) {
	    return currentTime.isAfter(alertEndTime);
	}





}