//package Controls;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import client.SystemClient;
//import client.ClientUI;
//import logic.ClientToServerRequest;
//import logic.ClientToServerRequest.Request;
//import common.MsgTemplates;
//import common.Messages;
//import common.Order;
//import common.Park;
//import common.Traveler;
//
///**
// * OrderControl class handles all the order related functionalities
// */
//@SuppressWarnings("unchecked")
//public class OrderControl {
//
//	/**
//	 * This function return the max discount for a given date in a given park.
//	 * 
//	 * @param visitDate The visit date
//	 * @param parkId    The park to search discounts at.
//	 * @return Discount object.
//	 */
//	/*public static Discount getMaxDiscount(String visitDate, String parkId) {
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.GET_MAX_DISCOUNT,
//				new ArrayList<String>(Arrays.asList(visitDate, parkId)));
//		ClientUI.chat.accept(request);
//		Discount discount = (Discount) ChatClient.responseFromServer.getResultSet().get(0);
//		return discount;
//	}*/
//
//	/**
//	 * This function gets a traveler and his order. The function adds the order to
//	 * DB if the date is available. And also notify the traveler via email, and
//	 * message in client
//	 * 
//	 * @param order The order to add to the database
//	 * @param traveler The traveler that made this order
//	 * @return Order object - the order that was inserted to the database
//	 */
//	public static Order addOrderAndNotify(Order order, Traveler traveler) {
//		Order recentOrder = null;
//		if (OrderControl.addOrder(order, traveler)) {
//			recentOrder = OrderControl.getTravelerRecentOrder(traveler.getTravelerId());
//
//			/* Insert massage to data base */
//			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//			LocalDateTime now = LocalDateTime.now();
//			String dateAndTime = dtf.format(now);
//			String date = dateAndTime.split(" ")[0];
//			String time = dateAndTime.split(" ")[1];
//			if (recentOrder != null) { // Insert email to the database
//				/* Create message content */
//				String emailContent = String.format(MsgTemplates.orderConfirmation[1].toString(),
//						String.valueOf(recentOrder.getOrderId()),
//						ParkControl.getParkName(String.valueOf(recentOrder.getParkId())), recentOrder.getOrderDate(),
//						recentOrder.getOrderTime(), recentOrder.getOrderType(),
//						String.valueOf(recentOrder.getNumberOfParticipants()), String.valueOf(recentOrder.getPrice()));
//
//				/* Add message to data base */
//				NotificationControl.sendMessageToTraveler(traveler.getTravelerId(), date, time,
//						MsgTemplates.orderConfirmation[0], emailContent, String.valueOf(recentOrder.getOrderId()));
//
//				/* Send message by mail */
//				Messages msg = new Messages(0, traveler.getTravelerId(), date, time, MsgTemplates.orderConfirmation[0],
//						emailContent, recentOrder.getOrderId());
//				NotificationControl.sendSms(traveler.getPhoneNumber(), msg); // Need to de-comment when showing
//				NotificationControl.sendMailInBackground(msg, null);
//			}
//
//		}
//		return recentOrder;
//	}
//
//	/**
//	 * This function gets a traveler and his order. The function adds the order to
//	 * DB if the date is available.
//	 * 
//	 * @param order    - Order object
//	 * @param traveler - Traveler object
//	 * @return true on success, false otherwise
//	 */
//	public static boolean addOrder(Order order, Traveler traveler) {
//		if ((order.getOrderStatus().equals(OrderStatus.PENDING.toString()) && isDateAvailable(order))
//				|| order.getOrderStatus().equals(OrderStatus.WAITING.toString())) {
//			ClientToServerRequest<Order> request = new ClientToServerRequest<>(Request.ADD_ORDER);
//			request.setObj(order);
//			ClientUI.clientControllerInstance.accept(request);
//
//			/* Add order */
//			if (SystemClient.serverResponse.isResult()) {
//				if (TravelerControl.isTravelerExist(traveler.getTravelerId())) {
//					return true; // Finished add order
//				} else {
//					/* Add traveler */
//					ClientToServerRequest<Traveler> addTravelerRequest = new ClientToServerRequest<>(
//							Request.ADD_TRAVELER);
//					addTravelerRequest.setObj(traveler);
//					ClientUI.clientControllerInstance.accept(addTravelerRequest);
//					if (SystemClient.serverResponse.isResult()) {
//						return true; // Finished add order
//					}
//				}
//			}
//		} else
//			return false; // Date not available
//
//		return false;
//
//	}
//
//	/** ליבדוק אם צריך
//	 * This function gets an id of a traveler and return his most recent order.
//	 * 
//	 * @param travelerId - the traveler id
//	 * @return Order object - the traveler most recent order
//	 */
//	public static Order getTravelerRecentOrder(String travelerId) {
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.GET_RECENT_ORDER,
//				new ArrayList<String>(Arrays.asList(travelerId)));
//		ClientUI.clientControllerInstance.accept(request);
//		Order order = (Order) SystemClient.serverResponse.getResultSet().get(0);
//		return order;
//
//	}
//
//	/**
//	 * This function gets an order and check if the order's date and time is
//	 * available
//	 * 
//	 * @param order - the order to check
//	 * @return true if available, false otherwise
//	 */
//	public static boolean isDateAvailable(Order order) {
//		String parkId = String.valueOf(order.getParkId());
//		Park park = ParkControl.getParkById(parkId);
//		String timeToCheck = order.getOrderTime();
//		int hour = Integer.parseInt(timeToCheck.split(":")[0]);
//		int estimatedStayTime = park.getEstimatedStayTime();
//
//		String visitDate = order.getOrderDate();
//		String startTime = (hour - estimatedStayTime) + ":00";
//		String endTime = (hour + estimatedStayTime) + ":00";
//
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.GET_ORDERS_BETWEEN_DATES,
//				new ArrayList<String>(Arrays.asList(parkId, visitDate, startTime, endTime)));
//		ClientUI.clientControllerInstance.accept(request);
//
//		ArrayList<Order> orders = SystemClient.serverResponse.getResultSet();
//
//		int numberOfVisitors = 0; /* This variable holds the number of visitors on the relevant date */
//		for (Order ord : orders) {
//			numberOfVisitors += ord.getNumberOfParticipants();
//		}
//
//		/*
//		 * Check if the amount of visitors including this order exceeds the allowed
//		 * number
//		 */
//		if (numberOfVisitors + order.getNumberOfParticipants() >= park.getMaxVisitors()
//				- park.getGapBetweenMaxAndCapacity()) {
//
//			return false;
//		}
//
//		return true;
//	}
//
//	/** ליבדוק אם צריך
//	 * This function return all of the orders that a certain ID has.
//	 * 
//	 * @param id - the Traveler Id
//	 * @return ArrayList of object Order, containing all of the orders for the given Id.
//	 */
//	public static ArrayList<Order> getOrders(String id) {
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.GET_ALL_ORDER_FOR_ID,
//				new ArrayList<String>(Arrays.asList(id)));
//		ClientUI.clientControllerInstance.accept(request);
//		return SystemClient.serverResponse.getResultSet();
//	}
//
//	/** ליבדוק אם צריך
//	 * This function return all of the orders that are in the system.
//	 * 
//	 * @return ArrayList of object Order, containing all the orders that are in the system.
//	 */
//	public static ArrayList<Order> getAllOrders() {
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.GET_ALL_ORDERS);
//		ClientUI.clientControllerInstance.accept(request);
//		return SystemClient.serverResponse.getResultSet();
//	}
//
//	/**
//	 * This function convert an ArrayList of object Order to ArrayList of object OrderTb
//	 * 
//	 * @param ordersArrayList - ArrayList of object Order
//	 * @return ArrayList of object OrderTb, containing all the orders in the given ArrayList of orders.
//	 */
//	public static ArrayList<OrderTable> convertOrderToOrderTb(ArrayList<Order> ordersArrayList) {
//		ArrayList<OrderTable> orderTbArrayList = new ArrayList<OrderTable>();
//		for (Order order : ordersArrayList) {
//			OrderTable orderTb = new OrderTable(order);
//			orderTbArrayList.add(orderTb);
//		}
//		return orderTbArrayList;
//	}
//
//	/**
//	 * This function change order status of orderId to statusName.
//	 * 
//	 * @param orderId    - String object
//	 * @param statusName - OrderStatus object
//	 * @return true on success, false otherwise
//	 */
//	public static boolean changeOrderStatus(String orderId, OrderStatus statusName) {
//		String status = statusName.toString();
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.CHANGE_ORDER_STATUS_BY_ID,
//				new ArrayList<String>(Arrays.asList(status, orderId)));
//		ClientUI.clientControllerInstance.accept(request);
//		return SystemClient.serverResponse.isResult();
//	}
//
//	/**
//	 * This function adds the visit to the DB.
//	 * 
//	 * @param order - OrderTb object
//	 * @return true on success, false otherwise
//	 */
//	public static boolean addVisit(OrderTable order) {
//		String travelerId = order.getTravelerId();
//		String parkId = String.valueOf(order.getParkId());
//		String date = order.getOrderDate();
//		String enterTime = order.getOrderTime();
//
//		Park park = ParkControl.getParkById(parkId);
//		String estimated = String.valueOf(park.getEstimatedStayTime());
//
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.ADD_VISIT,
//				new ArrayList<String>(Arrays.asList(travelerId, parkId, enterTime, estimated, date)));
//		ClientUI.clientControllerInstance.accept(request);
//
//		return SystemClient.serverResponse.isResult();
//	}
//
//	/**
//	 * This function adds the visit to the DB.
//	 * 
//	 * @param order - Order object
//	 * @return true on success, false otherwise
//	 */
//	public static boolean addVisit(Order order) {
//		String travelerId = order.getTravelerId();
//		String parkId = String.valueOf(order.getParkId());
//		String date = order.getOrderDate();
//		String enterTime = order.getOrderTime();
//
//		Park park = ParkControl.getParkById(parkId);
//		String estimated = String.valueOf(park.getEstimatedStayTime());
//
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.ADD_VISIT,
//				new ArrayList<String>(Arrays.asList(travelerId, parkId, enterTime, estimated, date)));
//		ClientUI.clientControllerInstance.accept(request);
//		return SystemClient.serverResponse.isResult();
//	}
//
//	/**
//	 * This function adds the order to the DB, while not adding the traveler to the DB.
//	 * 
//	 * @param order - Order object
//	 * @return true on success, false otherwise
//	 */
//	public static boolean addCasualOrder(Order order) {
//		ClientToServerRequest<Order> request = new ClientToServerRequest<>(Request.ADD_CASUAL_ORDER);
//		request.setObj(order);
//		ClientUI.clientControllerInstance.accept(request);
//		return SystemClient.serverResponse.isResult();
//	}
//
//	/** ליבדוק אם צריך
//	 * This function return all of the orders for a specific park.
//	 * 
//	 * @param parkId - int variable
//	 * @return ArrayList of object Order, containing all the orders for a spesific park.
//	 */
//	public static ArrayList<Order> getAllOrdersForParkId(int parkId) {
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.GET_ALL_ORDERS_FOR_PARK,
//				new ArrayList<String>(Arrays.asList(String.valueOf(parkId))));
//		ClientUI.clientControllerInstance.accept(request);
//		return SystemClient.serverResponse.getResultSet();
//	}
//
//	/** ליבדוק אם צריך
//	 * This function return all of the orders for traveler in a specific park.
//	 * 
//	 * @param parkId - String object
//	 * @param id     - String object
//	 * @return ArrayList of object Order, containing all the orders for traveler in a specific park.
//	 */
//	public static ArrayList<Order> getOrdersForTravelerInPark(String parkId, String id) {
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(
//				Request.GET_ALL_ORDERS_FOR_PARK_WITH_TRAVLER, new ArrayList<String>(Arrays.asList(parkId, id)));
//		ClientUI.clientControllerInstance.accept(request);
//		return SystemClient.serverResponse.getResultSet();
//	}
//
//	/**
//	 * This function ask the server to check if there is someone at the waiting list.
//	 * Id there is someone in the waiting list he will get a notification
//	 * 
//	 * @param orderId the order id of the canceled order.
//	 */
//	public static void checkWaitingList(int orderId) {
//		ClientToServerRequest<Integer> request = new ClientToServerRequest<>(Request.CHECK_WAITING_LIST,
//				new ArrayList<Integer>(Arrays.asList(orderId)));
//		ClientUI.clientControllerInstance.accept(request);
//	}
//
//	/** ליבדוק אם צריך
//	 * This function gets a traveler id and return the most recent order for this traveler
//	 * which it's status is 'Completed'
//	 * 
//	 * @param id The traveler id
//	 * @return Order most the recent order which it's status is 'Completed'
//	 */
//	public static Order getRelevantOrder_ParkExit(String id) {
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.GET_RELEVANT_ORDER_EXIT,
//				new ArrayList<String>(Arrays.asList(id)));
//		ClientUI.clientControllerInstance.accept(request);
//		Order order = (Order) SystemClient.serverResponse.getResultSet().get(0);
//		return order;
//	}
//
//	/** ליבדוק אם צריך
//	 * This function gets a traveler id and return the most recent order for this traveler
//	 * which it's status is 'Confirmed'
//	 * 
//	 * @param id The traveler id
//	 * @return Order most the recent order which it's status is 'Confirmed'
//	 */
//	public static Order getRelevantOrderByTravelerID_ParkEntrance(String id) {
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//		LocalDateTime now = LocalDateTime.now();
//		String dateAndTime = dtf.format(now);
//
//		String date = dateAndTime.split(" ")[0];
//		String time = dateAndTime.split(" ")[1];
//		int hour = Integer.parseInt(time.split(":")[0]);
//		String startTime = String.valueOf(hour - 2) + ":" + time.split(":")[1];
//		String endTime = String.valueOf(hour + 2) + ":" + time.split(":")[1];
//
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.GET_RELEVANT_ORDER_ENTRANCE,
//				new ArrayList<String>(Arrays.asList(id, date, startTime, endTime)));
//		ClientUI.clientControllerInstance.accept(request);
//		Order order = (Order) SystemClient.serverResponse.getResultSet().get(0);
//		return order;
//
//	}
//
//	/**
//	 * This function change number of visitors in existing order.
//	 * 
//	 * @param orderId  String object
//	 * @param numberOfParticipantsInCurrentOrder  int variable
//	 * @return true on success, false otherwise
//	 */
//	public static boolean changeNumberOfVisitorsInExisitingOrder(String orderId,
//			int numberOfParticipantsInCurrentOrder) {
//		String number = String.valueOf(numberOfParticipantsInCurrentOrder);
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(
//				Request.CHANGE_ORDER_NUMBER_OF_VISITORS_BY_ID, new ArrayList<String>(Arrays.asList(number, orderId)));
//		ClientUI.clientControllerInstance.accept(request);
//		return SystemClient.serverResponse.isResult();
//	}
//
//	/**
//	 * This function update the price in an exisiting order.
//	 * 
//	 * @param id - String object
//	 * @param price - double variable
//	 * @return true on success, false otherwise
//	 */
//	public static boolean updateOrderPrice(String id, double price) {
//		String p = String.valueOf(price);
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.CHANGE_ORDER_PRICE_BY_ID,
//				new ArrayList<String>(Arrays.asList(p, id)));
//		ClientUI.clientControllerInstance.accept(request);
//		return SystemClient.serverResponse.isResult();
//
//	}
//
//}
