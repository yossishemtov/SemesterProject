//package Controls;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import client.SystemClient;
//import client.ClientUI;
//import javafx.concurrent.Task;
//import logic.ClientToServerRequest;
//import logic.Messages;
//import logic.ClientToServerRequest.Request;
//
///**
// * NotificationControl class handles all the notification related functionalities
// */
//public class NotificationControl {
//
//	/**
//	 * This function gets  message and ask the server to add it to travelers messages.
//	 * 
//	 * @param toId     String object, travelerId
//	 * @param sendDate String object, sending date
//	 * @param sendTime String object, sending time
//	 * @param subject  String object, subject of the message
//	 * @param content  String object, subject of the message
//	 * @param orderId  String object, regarding which order the message is.
//	 * @return true on success, false otherwise.
//	 */
//	public static boolean sendMessageToTraveler(String toId, String sendDate, String sendTime, String subject,
//			String content, String orderId) {
//
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.SEND_MSG_TO_TRAVELER,
//				new ArrayList<String>(Arrays.asList(toId, sendDate, sendTime, subject, content, orderId)));
//		ClientUI.clientControllerInstance.accept(request);
//		return SystemClient.serverResponse.isResult();
//
//	}
//
//	/**
//	 * This function gets message and ask the server to send it by email.
//	 * 
//	 * @param msg the message to send
//	 * @return true on success, false otherwise.
//	 */
//	private static boolean sendEmail(Messages msg, String email) {
//		ClientToServerRequest<Messages> request = null;
//		if (email == null) {
//			request = new ClientToServerRequest<>(Request.SEND_EMAIL);
//		} else {
//			request = new ClientToServerRequest<>(Request.SEND_EMAIL_WITH_EMAIL);
//			request.setInput(email);
//		}
//		request.setObj(msg);
//		ClientUI.clientControllerInstance.accept(request);
//		return SystemClient.serverResponse.isResult();
//	}
//
//	/**
//	 * This function gets a message and send it by mail. The function creates a new
//	 * thread and runs at the background.
//	 * 
//	 * @param msg  the message to send
//	 * @param email The email to send to
//	 */
//	public static void sendMailInBackground(Messages msg, String email) {
//		Task<Boolean> mailTask = new Task<Boolean>() {
//			@Override
//			protected Boolean call() throws Exception {
//				NotificationControl.sendEmail(msg, email);
//				return true;
//			}
//		};
//		// send email in a background thread
//		new Thread(mailTask).start();
//
//	}
//
//	/**
//	 * This function gets traveler messages by his ID
//	 * 
//	 * @param id - the traveler's id
//	 * @return ArrayList of messages
//	 */
//	@SuppressWarnings("unchecked")
//	public static ArrayList<Messages> getMessages(String id) {
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.GET_MESSAGES_BY_ID,
//				new ArrayList<String>(Arrays.asList(id)));
//		ClientUI.clientControllerInstance.accept(request);
//		return SystemClient.serverResponse.getResultSet();
//	}
//
//	public static void sendSms(String phoneNumber, Messages msg) {
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.SEND_SMS,
//				new ArrayList<String>(Arrays.asList(phoneNumber, msg.getContent())));
//		ClientUI.clientControllerInstance.accept(request);
//	}
//
//}
