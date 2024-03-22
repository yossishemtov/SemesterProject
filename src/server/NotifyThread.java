package server;

import java.sql.Connection;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JOptionPane;

import DB.DatabaseController;
import common.GetInstance;
import common.Order;
import gui.TravelerFrameController;


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
	private final int minute = second * 60;
	private DatabaseController DC;
    private ArrayList<Order> ordersWithAlerts; // Array to store orders with alerts


	public NotifyThread(Connection mysqlconnection) {
		DC = new DatabaseController("root","Aa123456");
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

			ArrayList<Order> pendingOrders = DC.getPendingOrders();
			for (Order order : pendingOrders) {
				String status = "PENDING_EMAIL_SENT";
				String orderId = String.valueOf(order.getOrderId());
				DC.updateOrderStatusArray(new ArrayList<String>(Arrays.asList(status, orderId)));
				/**Visitor has 2 hours to accept visit from now*/
				order.setTimeOfFirstNotification(LocalTime.now()); 
				order.setLastTimeToAcceptNotification(LocalTime.now().plusHours(2));
				sendConfirmationMessage(order);
				addOrderWithAlert(order);
			}
			CancelOrderAndNotify();
			try {
				Thread.sleep(1 * minute);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void addOrderWithAlert(Order order) {
        ordersWithAlerts.add(order);
    }
	
	private void CancelOrderAndNotify() {
	    
	    // Iterate through orders with alerts and cancel expired orders
	    Iterator<Order> iterator = ordersWithAlerts.iterator();
	    while (iterator.hasNext()) {
	        Order order = iterator.next();
	        if (isAlertExpired(order.getTimeOfFirstNotification(), order.getLastTimeToAcceptNotification())) {
	            // Cancel the order
	            DC.updateOrderStatusArray(new ArrayList<String>(Arrays.asList("CANCELED", String.valueOf(order.getOrderId()))));
	            sendCancelMessage(order);
	            //WaitingListControl.notifyPersonFromWaitingList(order);
	            // Remove the canceled order from ordersWithAlerts
	            iterator.remove();
	        }
	    }
	}

	private boolean isAlertExpired(LocalTime currentTime, LocalTime alertEndTime) {
	    return currentTime.isAfter(alertEndTime);
	}




	/*
	 * This message is for a person to approve his visit.
	 */
	private void sendConfirmationMessage(Order order) {
	    String parkName = order.getParkName();
	    String orderDate = order.getDate().toString();
	    String orderTime = order.getVisitTime().toString();
	    int option = JOptionPane.showConfirmDialog(null, "Your order at " + parkName + " on " + orderDate + " at " + orderTime + " has been confirmed. Would you like to see more details?", "Order Confirmed", JOptionPane.YES_NO_OPTION);
	    
	    if (option == JOptionPane.YES_OPTION) {
	        // Show more details about the confirmed order
	        // You can customize this according to your requirements, such as opening a new window with order details
	        // For simplicity, let's display a message dialog with order details
	        String message = "Park: " + parkName + "\nDate: " + orderDate + "\nTime: " + orderTime;
	        JOptionPane.showMessageDialog(null, message, "Order Details", JOptionPane.INFORMATION_MESSAGE);
	    }

	}

	/*
	 * This message is for an canceled order.
	 */
	private void sendCancelMessage(Order order) {
	    String parkName = order.getParkName();
	    String orderDate = order.getDate().toString();
	    String orderTime = order.getVisitTime().toString();
	    int option = JOptionPane.showConfirmDialog(null, "Your order at " + parkName + " on " + orderDate + " at " + orderTime + " has been canceled. Would you like to see more details?", "Order Canceled", JOptionPane.YES_NO_OPTION);
	    
	    if (option == JOptionPane.YES_OPTION) {
	        // Show more details about the canceled order
	        // You can customize this according to your requirements, such as opening a new window with order details
	        // For simplicity, let's display a message dialog with order details
	        String message = "Park: " + parkName + "\nDate: " + orderDate + "\nTime: " + orderTime;
	        JOptionPane.showMessageDialog(null, message, "Order Details", JOptionPane.INFORMATION_MESSAGE);
	    }
	}

	/*
	 * This message is for a person in the waiting list.
	 */


}
