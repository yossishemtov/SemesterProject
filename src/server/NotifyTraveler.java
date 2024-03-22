package server;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import DB.DatabaseController;
import common.Order;
import common.Park;

/**
 * NotifyTravelers class implements Runnable.
 * 
 * This class handle all the automated functionality:
 *  Send reminder 24 hours before visit.
 *  Cancel the visit if the traveler did not confirmed within two hours.
 *  Notify the next in the waiting list
 *
 */
public class NotifyTraveler implements Runnable {

	private final int second = 1000;
	private final int minute = second * 60;
	private DatabaseController DC;
	
	public NotifyTraveler(Connection mysqlconnection) {
		DC = new DatabaseController("root","Aa123456");
		
	}

	@Override
	public void run() {

		while (true) {
			System.out.println("Looking for relevant orders");
			ArrayList<Order> orders = DC.getOrdersByStatusInLastTwentyFourHours("PENDING");

			for (Order order : orders) {
					runNotifySent(order);
			}

			try {
				Thread.sleep(1 * minute);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * This function create thread that check if the traveler confirmed his order.
	 * if he did not confirmed his order within two hours - the order canceled
	 * automatically and we notify the first in the waiting list (if there is
	 * someone)
	 * 
	 */
	private void runNotifySent(Order order) {
		Thread notifySent = new Thread(new Runnable() {

			@Override
			public void run() {
				String status = Order.status.PENDING_EMAIL_SENT.toString();
				String orderId = String.valueOf(order.getOrderId());
				DC.updateOrderStatusArray(new ArrayList<String>(Arrays.asList(status, orderId)));

				sendConfirmationMessage(order);

				int totalSleep = 0;
				Order updatedOrder = null;

				while (totalSleep != 120) {
					updatedOrder = DC.getOrderbyId(order.getOrderId());
					/* NEED TO CHECK STATUS */
					if (updatedOrder.getOrderStatus().equals(Order.status.CANCELED.toString())
							|| updatedOrder.getOrderStatus().equals(Order.status.CONFIRMED.toString()))
						break;

					try {
						Thread.sleep(1 * minute);
						totalSleep += 1;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (!updatedOrder.getOrderStatus().equals(Order.status.CONFIRMED.toString())) {
					status = Order.status.CANCELED.toString();
					orderId = String.valueOf(updatedOrder.getOrderId());
					DC.updateOrderStatusArray(new ArrayList<String>(Arrays.asList(status, orderId)));

					/* Need to Send cancel order msg */
					sendCancelMessage(updatedOrder);

					NotifyWaiting notifyWaitingList = new NotifyWaiting(order);
					new Thread(notifyWaitingList).start();
				}
			}

		});
		notifySent.start();
	}


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

}
