package common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;


import client.ClientUI;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

@SuppressWarnings("unchecked")
public class TravelerChecker {
	
	public void viewPendingNotifications() {
        // receive notifications about orders that are pending

        Traveler currentTraveler = Usermanager.getCurrentTraveler();

        try {

            ClientServerMessage<?> findPendingNotificationsOfTravelerOrders = new ClientServerMessage<>(currentTraveler.getId(), Operation.GET_STATUS_PENDING_NOTIFICATION_BY_TRAVELERID);
            ClientUI.clientControllerInstance.sendMessageToServer(findPendingNotificationsOfTravelerOrders);

            ClientServerMessage<?> pendingNotificationsOrders = ClientUI.clientControllerInstance.getData();
            ArrayList<Order> pendingOrders = (ArrayList<Order>) pendingNotificationsOrders.getDataTransfered();

            for (Order order : pendingOrders) {
                if (currentTraveler.getId().equals(order.getVisitorId())) {
                    String orderDate = order.getDate().toString();
                    String orderTime = order.getVisitTime().toString();

                    LocalDateTime orderDateTime = LocalDateTime.of(LocalDate.parse(orderDate), LocalTime.parse(orderTime));
                    LocalDateTime currentDateTime = LocalDateTime.now();

                    if (currentDateTime.isAfter(orderDateTime))
                        break;

                    String[] message = {
                            "Please Confirm Your Visit",
                            String.format("Dear Visitor,\n"
                                    + "You have placed an order for a visit to %s on %s at %s.\n"
                                    + "Kindly confirm your visit within the next two hours.\n"
                                    + "If you fail to confirm your visit within the specified time, your order will be automatically cancelled.\n\n"
                                    + "Best Regards, GoNature\n", order.getParkName(), order.getDate(), order.getVisitTime())
                    };

                    Alerts confirmAlert = new Alerts(AlertType.CONFIRMATION, "Order", "Please Confirm Your Visit", message[1]);
                    confirmAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            // Change the orderStatus to confirmed
                            ClientServerMessage<?> updateStatusConfirmed = new ClientServerMessage<>(new ArrayList<String>
                                    (Arrays.asList("CONFIRMED", order.getOrderId() + "")), Operation.PATCH_ORDER_STATUS_ARRAYLIST);
                            ClientUI.clientControllerInstance.sendMessageToServer(updateStatusConfirmed);
                            // Display confirmation message
                            String[] messageYes = {
                                    String.format("Dear Visitor,\n"
                                            + "You have confirmed your order to the %s Park on %s at %s.\n"
                                            + "Best Regards, GoNature\n", order.getParkName(), order.getDate().toString(), order.getVisitTime().toString())
                            };
                            Alerts confirmationAlert = new Alerts(AlertType.INFORMATION, "Order Details", "Order Confirmed", messageYes[0]);
                            confirmationAlert.showAndWait();
                        } else {
                            // Handle cancellation
                            String[] messageNo = {
                                    String.format("Dear Visitor,\n"
                                            + "You have canceled your order to the %s Park on %s at %s.\n"
                                            + "Best Regards, GoNature\n", order.getParkName(), order.getDate().toString(), order.getVisitTime().toString())
                            };
                            Alerts cancellationAlert = new Alerts(AlertType.INFORMATION, "Order Details", "Order Canceled", messageNo[0]);
                            cancellationAlert.showAndWait();
                            // Change the order status to canceled
                            ClientServerMessage<?> updateStatusCanceled = new ClientServerMessage<>(new ArrayList<String>
                                    (Arrays.asList("CANCELED", order.getOrderId() + "")), Operation.PATCH_ORDER_STATUS_ARRAYLIST);
                            ClientUI.clientControllerInstance.sendMessageToServer(updateStatusCanceled);
                        }
                    });
                }
            }

        } catch (Exception e) {
            new Alerts(AlertType.WARNING, "Something went wrong", "Cancellation",
                    "Something went wrong with receiving notifications for Pending orders").showAndWait();
        }
    }
    
    
	public void viewCanceledNotifications() {
	    Traveler currentTraveler = Usermanager.getCurrentTraveler();

	    try {
	        ClientServerMessage<?> findPendingNotificationsOfTravelerOrders = new ClientServerMessage<>(currentTraveler.getId(), Operation.GET_STATUS_CANCELED_NOTIFICATION_BY_TRAVELERID);
	        ClientUI.clientControllerInstance.sendMessageToServer(findPendingNotificationsOfTravelerOrders);

	        ClientServerMessage<?> canceledNotificationsOrders = ClientUI.clientControllerInstance.getData();
	        ArrayList<Order> canceledOrders = (ArrayList<Order>) canceledNotificationsOrders.getDataTransfered();

	        for (Order order : canceledOrders) {
	            if (currentTraveler.getId().equals(order.getVisitorId())) {
	                String parkName = order.getParkName();
	                String orderDate = order.getDate().toString();
	                String orderTime = order.getVisitTime().toString();

	                LocalDateTime orderDateTime = LocalDateTime.of(LocalDate.parse(orderDate), LocalTime.parse(orderTime));
	                LocalDateTime currentDateTime = LocalDateTime.now();

	                if (currentDateTime.isAfter(orderDateTime))
	                    break;
	                String[] messageCancel = {
                            String.format("Dear Visitor,\n"
                                    + "You have canceled your order to the %s Park on %s at %s.\n"
                                    + "Best Regards, GoNature\n", order.getParkName(), order.getDate().toString(), order.getVisitTime().toString())
                    };
	                ClientServerMessage<?> changeStatusToCancel = new ClientServerMessage<>(
	                        new ArrayList<String>(Arrays.asList("CANCELED", String.valueOf(order.getOrderId()))), Operation.PATCH_ORDER_STATUS_ARRAYLIST);
	                ClientUI.clientControllerInstance.sendMessageToServer(changeStatusToCancel);

	                Alerts canceledAlert = new Alerts(AlertType.INFORMATION, "Order Canceled", "Order Canceled", messageCancel[1]);
	                canceledAlert.showAndWait();
	            }
	        }
	    } catch (Exception e) {
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
	        if (currentTraveler.getId().equals(waiting.getVisitorId())) {
	            String orderDate = waiting.getDate().toString();
	            String orderTime = waiting.getVisitTime().toString();

	            LocalDateTime orderDateTime = LocalDateTime.of(LocalDate.parse(orderDate), LocalTime.parse(orderTime));
	            LocalDateTime currentDateTime = LocalDateTime.now();

	            if (currentDateTime.isAfter(orderDateTime))
	                break;

	            String[] message = {
	                    "We've reserved a spot for you in the park!",
	                    String.format("Dear Visitor,\n"
	                            + "We're thrilled to inform you that a spot has become available for your visit while you were on the waiting list!\n"
	                            + "You're invited to visit %s Park on %s at %s.\n"
	                            + "To confirm your visit at this time, please respond within the two hours.\n\n"
	                            + "Best Regards, GoNature\n", waiting.getParkName(), waiting.getDate().toString(), waiting.getVisitTime().toString())
	            };

	            Alerts waitingAlert = new Alerts(AlertType.CONFIRMATION, "Order Confirmed", "We've reserved a spot for you in the park!", message[1]);
	            waitingAlert.showAndWait().ifPresent(response -> {
	                if (response == ButtonType.YES) {
	                    String messageYes = String.format("Dear Visitor,\n"
	                            + "You have confirmed your order to the %s Park on %s at %s.\n"
	                            + "Best Regards, GoNature\n", waiting.getParkName(), waiting.getDate().toString(), waiting.getVisitTime().toString());
	                    Alerts confirmationAlert = new Alerts(AlertType.INFORMATION, "Order Details", "Order Confirmed", messageYes);
	                    confirmationAlert.showAndWait();
	                    // Update status to confirmed and register new order
	                    ClientServerMessage<?> updateStatus = new ClientServerMessage<>(new ArrayList<String>
	                            (Arrays.asList("CONFIRMED", String.valueOf(waiting.getWaitingListId()))), Operation.PATCH_WAITING_STATUS);
	                    ClientUI.clientControllerInstance.sendMessageToServer(updateStatus);
	                    waiting.setOrderId(OrderChecker.getLastNumber());
	                    waiting.setStatus("CONFIRMED");
	                    ClientServerMessage<?> orderAttempt = new ClientServerMessage<>(waiting, Operation.POST_ORDER_FROM_WAITING);
	                    ClientUI.clientControllerInstance.sendMessageToServer(orderAttempt);
	                } else {
	                    String messageNo = String.format("Dear Visitor,\n"
	                            + "You have canceled your order to the %s Park on %s at %s.\n"
	                            + "Best Regards, GoNature\n", waiting.getParkName(), waiting.getDate().toString(), waiting.getVisitTime().toString());
	                    Alerts cancellationAlert = new Alerts(AlertType.INFORMATION, "Order Details", "Order Canceled", messageNo);
	                    cancellationAlert.showAndWait();
	                    // Update status to canceled
	                    ClientServerMessage<?> updateStatus = new ClientServerMessage<>(new ArrayList<String>
	                            (Arrays.asList("CANCELED", String.valueOf(waiting.getWaitingListId()))), Operation.PATCH_WAITING_STATUS);
	                    ClientUI.clientControllerInstance.sendMessageToServer(updateStatus);
	                }
	            });
	        }
	    }
	}
    
}