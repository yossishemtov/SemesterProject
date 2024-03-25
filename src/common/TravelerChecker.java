package common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import client.ClientUI;
import javafx.scene.control.Alert.AlertType;
@SuppressWarnings("unchecked")

public class TravelerChecker {
	
	public void viewPendingNotifications(){
    	//receive notifications about orders that are pending

    	Traveler currentTraveler = Usermanager.getCurrentTraveler();
    	
    	try {

    	ClientServerMessage<?> findPendingNotificationsOfTravelerOrders = new ClientServerMessage<>(currentTraveler.getId(), Operation.GET_STATUS_PENDING_NOTIFICATION_BY_TRAVELERID);
		ClientUI.clientControllerInstance.sendMessageToServer(findPendingNotificationsOfTravelerOrders);
		
	    ClientServerMessage<?> pendingNotificationsOrders = ClientUI.clientControllerInstance.getData();
	    ArrayList<Order> pendingOrders = (ArrayList<Order>) pendingNotificationsOrders.getDataTransfered();
	    
	    for (Order order : pendingOrders) {
	    	if(currentTraveler.getId().equals(order.getVisitorId())) {
	    	    String orderDate = order.getDate().toString();
	    	    String orderTime = order.getVisitTime().toString();
	    	    
	    	    LocalDateTime orderDateTime = LocalDateTime.of(LocalDate.parse(orderDate), LocalTime.parse(orderTime));
	    	    LocalDateTime currentDateTime = LocalDateTime.now();
	    	    
	    	    if(currentDateTime.isAfter(orderDateTime))
	    	    	break;
	    	    
	    	    String[] message = {
	    	    	    "Please Confirm Your Visit",
	    	    	    String.format("Dear Visitor,\n"
	    	    	            + "You have placed an order for a visit to %s on %s at %s.\n"
	    	    	            + "Kindly confirm your visit within the next two hours.\n"
	    	    	            + "If you fail to confirm your visit within the specified time, your order will be automatically cancelled.\n\n"
	    	    	            + "Best Regards, GoNature\n" , order.getParkName(), order.getDate(), order.getVisitTime())
	    	    	};

	    	    int option = JOptionPane.showConfirmDialog(null, message , "Order", JOptionPane.YES_NO_OPTION);
	    	    
	    	    if (option == JOptionPane.YES_OPTION) {
	    	        //change the orderStatus to confirmed
	    	    	 ClientServerMessage<?> updateStatusConfirmed = new ClientServerMessage<>(new ArrayList<String>
	                    (Arrays.asList("CONFIRMED",order.getOrderId()+"")), Operation.PATCH_ORDER_STATUS_ARRAYLIST);
	                    ClientUI.clientControllerInstance.sendMessageToServer(updateStatusConfirmed);
	    	    	//change notification status to passed
	    	    	
	                    String[] messageYes = {
			    	    	    String.format("Dear Visitor,\n"
			    	    	            + "You have confirmed you're order to the %s Park on %s at %s.\n"
			    	    	            + "Best Regards, GoNature\n", order.getParkName(), order.getDate().toString(), order.getVisitTime().toString())
			    	    	};
	    	        // For simplicity, let's display a message dialog with order details
	    	        JOptionPane.showMessageDialog(null, messageYes, "Order Details", JOptionPane.INFORMATION_MESSAGE);
	    	        
	    	    }
	    	    
	    	    if (option == JOptionPane.NO_OPTION || option == JOptionPane.CLOSED_OPTION) {
	    	    	
	    	    	String[] messageNo = {
		    	    	    String.format("Dear Visitor,\n"
		    	    	            + "You have canceled you're order to the %s Park on %s at %s.\n"
		    	    	            + "Best Regards, GoNature\n", order.getParkName(), order.getDate().toString(), order.getVisitTime().toString())
		    	    	};
	    	    	
                    JOptionPane.showMessageDialog(null, messageNo, "Order Details", JOptionPane.INFORMATION_MESSAGE);

                    ClientServerMessage<?> updateStatusCanceled = new ClientServerMessage<>(new ArrayList<String>
                    (Arrays.asList("CANCELED",order.getOrderId()+"")), Operation.PATCH_ORDER_STATUS_ARRAYLIST);
                    ClientUI.clientControllerInstance.sendMessageToServer(updateStatusCanceled);
	                }
		    	}
	
		    }
	    
    		}catch (Exception e) {
    			new Alerts(AlertType.WARNING, "Something went wrong", "Cancellation",
    					"Something went wrong with receiving notifications for Pending orders").showAndWait();
	    	}
    }
    
    
    public void viewCanceledNotifications() {
    	//receive notifications about orders that were canceled by the server
    	Traveler currentTraveler = Usermanager.getCurrentTraveler();
    	
    	try {
    		
    		//Get all the notifications of orders that were canceled by the server
	    	ClientServerMessage<?> findPendingNotificationsOfTravelerOrders = new ClientServerMessage<>(currentTraveler.getId(), Operation.GET_STATUS_CANCELED_NOTIFICATION_BY_TRAVELERID);
			ClientUI.clientControllerInstance.sendMessageToServer(findPendingNotificationsOfTravelerOrders);
			
		    ClientServerMessage<?> canceledNotificationsOrders = ClientUI.clientControllerInstance.getData();
		    ArrayList<Order> canceledOrders = (ArrayList<Order>) canceledNotificationsOrders.getDataTransfered();
	    
		//Displaying all the notifications with their corresponding orders
	    for (Order order : canceledOrders) {
	    	if(currentTraveler.getId().equals(order.getVisitorId())) {
	    		String parkName = order.getParkName();
	    	    String orderDate = order.getDate().toString();
	    	    String orderTime = order.getVisitTime().toString();
	    	    
	    	    LocalDateTime orderDateTime = LocalDateTime.of(LocalDate.parse(orderDate), LocalTime.parse(orderTime));
	    	    LocalDateTime currentDateTime = LocalDateTime.now();
	    	    
	    	    if(currentDateTime.isAfter(orderDateTime))
	    	    	break;
	    	    
	    	    JOptionPane.showConfirmDialog(null, "Your order at " + parkName + " on " + orderDate + " at " + orderTime + " has been Canceled.", "Order Canceled", JOptionPane.OK_OPTION);
	
	    	}
	    }
    		}catch (Exception e) {
    			new Alerts(AlertType.WARNING, "Something went wrong", "Cancellation",
    					"Something went wrong with receiving notifications for Canceled orders").showAndWait();
	    	}
    }
    
	public void viewWaitingNotifications() {
    	Traveler currentTraveler = Usermanager.getCurrentTraveler();
	    ClientServerMessage<?> findSpot = new ClientServerMessage<>(null, Operation.GET_STATUS_HAS_SPOT);
		ClientUI.clientControllerInstance.sendMessageToServer(findSpot);
	    ClientServerMessage<?> spotMsg = ClientUI.clientControllerInstance.getData();
	    ArrayList<WaitingList> spotOrders = (ArrayList<WaitingList>) spotMsg.getDataTransfered();
	    for (WaitingList waiting : spotOrders) {
	    	if(currentTraveler.getId().equals(waiting.getVisitorId())) {
	    	    String orderDate = waiting.getDate().toString();
	    	    String orderTime = waiting.getVisitTime().toString();
	    	    
	    	    LocalDateTime orderDateTime = LocalDateTime.of(LocalDate.parse(orderDate), LocalTime.parse(orderTime));
	    	    LocalDateTime currentDateTime = LocalDateTime.now();
	    	    
	    	    if(currentDateTime.isAfter(orderDateTime))
	    	    	break;
	    	    
	    	    String[] message = {
	    	    	    "We've reserved a spot for you in the park!",
	    	    	    String.format("Dear Visitor,\n"
	    	    	            + "We're thrilled to inform you that a spot has become available for your visit while you were on the waiting list!\n"
	    	    	            + "You're invited to visit %s Park on %s at %s.\n"
	    	    	            + "To confirm your visit at this time, please respond within the two hours.\n\n"
	    	    	            + "Best Regards, GoNature\n", waiting.getParkName(), waiting.getDate().toString(), waiting.getVisitTime().toString())
	    	    	};

	    	    int option = JOptionPane.showConfirmDialog(null,message, "Order Confirmed", JOptionPane.YES_NO_OPTION);
	    	    
	    	    if (option == JOptionPane.YES_OPTION) {
	    	    	String[] messageYes = {
		    	    	    String.format("Dear Visitor,\n"
		    	    	            + "You have confirmed you're order to the %s Park on %s at %s.\n"
		    	    	            + "Best Regards, GoNature\n", waiting.getParkName(), waiting.getDate().toString(), waiting.getVisitTime().toString())
		    	    	};
	    	        JOptionPane.showMessageDialog(null, messageYes, "Order Details", JOptionPane.INFORMATION_MESSAGE);
	    	        
	    	        ClientServerMessage<?> updateStatus = new ClientServerMessage<>(new ArrayList<String>
	    			(Arrays.asList("CONFIRMED",waiting.getWaitingListId()+"")), Operation.PATCH_WAITING_STATUS);
	    			ClientUI.clientControllerInstance.sendMessageToServer(updateStatus);
	    		    
	    			waiting.setOrderId(OrderChecker.getLastNumber());
	    		    waiting.setStatus("CONFIRMED");
	    		    /**Registering NewOrder to DB*/
					ClientServerMessage<?> OrderAttempt = new ClientServerMessage<>(waiting, Operation.POST_ORDER_FROM_WAITING);
					ClientUI.clientControllerInstance.sendMessageToServer(OrderAttempt);

	    	    }
	    	    
	    	    if (option == JOptionPane.NO_OPTION || option == JOptionPane.CLOSED_OPTION) {
	    	    	String[] messageNo = {
		    	    	    String.format("Dear Visitor,\n"
		    	    	            + "You have canceled you're order to the %s Park on %s at %s.\n"
		    	    	            + "Best Regards, GoNature\n", waiting.getParkName(), waiting.getDate().toString(), waiting.getVisitTime().toString())
		    	    	};
	    	    	
	    	        JOptionPane.showMessageDialog(null, messageNo, "Order Details", JOptionPane.INFORMATION_MESSAGE);
	    	        
	    	        ClientServerMessage<?> updateStatus = new ClientServerMessage<>(new ArrayList<String>
	    			(Arrays.asList("CANCELED",waiting.getWaitingListId()+"")), Operation.PATCH_WAITING_STATUS);
	    			ClientUI.clientControllerInstance.sendMessageToServer(updateStatus);
	    	    }
	    	}

	    }
    }
    
}
