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
import DB.MySqlConnector;
import common.Order;
import common.Park;
import common.WaitingList;

/**
 * NotifyWaitingList class implements Runnable.
 * 
 * This class handle all the automated functionality:
 * Notify a person from waiting list that he has a spot in the park for his order.
 * Monitoring the order status for 1 hour.
 * Notify the next in the waiting list if the traveler did not confirm his order.
 *
 */
public class NotifyWaiting implements Runnable {

	private String date;
	private String time;
	private Park park;
	private Connection mysqlconnection;
	private DatabaseController DC;
	
	

	private Order order;

	private final int second = 1000;
	private final int minute = second * 60;

	
	public NotifyWaiting(Order order) {
		try {
			// Establish a connection to the MySQL database using your MySqlConnector
			DC = new DatabaseController("root","Aa123456");
			
			this.date = order.getDate().toString();
			this.time = order.getVisitTime().toString();
			this.park = DC.getParkDetails(order.getParkNumber());
			this.order = order;
		} catch (Exception e) {
			System.out.println("Exception was thrown - notify waiting list");
			e.printStackTrace();
		}
	}

	/**
	 * This function check if the traveler confirmed or canceled his order.
	 * if he did not confirmed his order within one hour - the order canceled
	 * automatically and we notify the next in the waiting list (if there is
	 * someone)
	 * 
	 */
	@Override
	public void run() {
		Order order = notifyPersonFromWaitingList(date, time, park);

		System.out.println("Entered notify waiting list");
		if (order == null) 
			return;
		

		String orderId = String.valueOf(order.getOrderId());
		String status = "WAITING_HAS_SPOT";
		DC.updateOrderStatusArray(new ArrayList<String>(Arrays.asList(status, orderId)));
		sendMessages(order);

		int totalSleep = 0;
		Order updatedOrder = null;

		while (totalSleep != 60) {
			System.out.println("Entred while");
			updatedOrder = DC.getOrderbyId(order.getOrderId());
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
			status = "CANCELED";
			orderId = String.valueOf(updatedOrder.getOrderId());
			DC.updateOrderStatusArray(new ArrayList<String>(Arrays.asList(status, orderId)));

			// Passing the orignal order that was canceled.
			NotifyWaiting notifyWaitingList = new NotifyWaiting(this.order);
			new Thread(notifyWaitingList).start();
		}

	}


	private void sendMessages(Order order) {
	    String parkName = order.getParkName();
	    String orderDate = order.getDate().toString();
	    String orderTime = order.getVisitTime().toString();
	    int option = JOptionPane.showConfirmDialog(null, "You have been placed on the waiting list for " + parkName + " on " + orderDate + " at " + orderTime + ". Would you like to see more details?", "Waiting List Placement", JOptionPane.YES_NO_OPTION);
	    
	    if (option == JOptionPane.YES_OPTION) {
	        // Show more details about the waiting list placement
	        // You can customize this according to your requirements, such as opening a new window with details
	        // For simplicity, let's display a message dialog with order details
	        String message = "Park: " + parkName + "\nDate: " + orderDate + "\nTime: " + orderTime;
	        JOptionPane.showMessageDialog(null, message, "Waiting List Placement Details", JOptionPane.INFORMATION_MESSAGE);
	    }
	}


	private Order notifyPersonFromWaitingList(String date, String time, Park park) {
		String parkId = String.valueOf(park.getParkNumber());
		String maxVisitors = String.valueOf(park.getMaxVisitors());
		String estimatedStayTime = String.valueOf(park.getStaytime());
		
		ArrayList<String> parameters = new ArrayList<String>(
				Arrays.asList(parkId, maxVisitors, estimatedStayTime, date, time));
		ArrayList<WaitingList> ordersThatMatchWaitingList = DC.findMatchingOrdersInWaitingList(parameters);
		return ordersThatMatchWaitingList.size() == 0 ? null : ordersThatMatchWaitingList.get(0);
	}

}