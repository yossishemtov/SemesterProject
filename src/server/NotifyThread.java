package server;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JOptionPane;

import com.mysql.cj.xdevapi.DbDoc;

import DB.DatabaseController;
import common.GetInstance;
import common.Order;
import common.OrderNotification;
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
	private final int minute = second * 10;
	private DatabaseController DC;
    private ArrayList<OrderNotification> ordersWithAlerts; // Array to store orders with alerts


	public NotifyThread(Connection mysqlconnection) {
		DC = new DatabaseController("root","root");
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
				
				//Change status of orders to PENDING_EMAIL_SENT
				DC.updateOrderStatusArray(new ArrayList<String>(Arrays.asList(status, orderId)));
				
				/**Visitor has 2 hours to accept visit from now*/
				OrderNotification createNotification = new OrderNotification(Integer.parseInt(orderId), LocalDate.now(),
						LocalTime.now(), LocalTime.now().plusHours(2), "SENT");
				
				//Add notification to the orders notified arraylist
				ordersAlreadyNotified.add(createNotification);
				
				//Posting the notification in the db
				DC.postOrderNotification(createNotification);

				sendConfirmationMessage(order);		
			}
			
			ordersWithAlerts = ordersAlreadyNotified;
			CancelOrderAndNotify();
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
	        	System.out.print("Caceled Order");
	        	
	        	//Change status in the ordernotification table
	        	DC.changeStatusOfNotification(notificationOfSpecificOrder.getOrderId(), "PASSED");
	        	
	        	//Change status in the order table
	            DC.updateOrderStatusArray(new ArrayList<String>(Arrays.asList("CANCELED", String.valueOf(notificationOfSpecificOrder.getOrderId()))));
	            sendCancelMessage(notificationOfSpecificOrder);
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
	private void sendCancelMessage(OrderNotification orderNotification) {
		System.out.print(orderNotification.getOrderId());
		Order canceledOrder = DC.getOrderbyId(orderNotification.getOrderId());
	    String parkName = canceledOrder.getParkName();
	    String orderDate = canceledOrder.getDate().toString();
	    String orderTime = canceledOrder.getVisitTime().toString();
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
