//package Controls;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import client.SystemClient;
//import client.ClientUI;
//import logic.ClientToServerRequest;
//import logic.Traveler;
//import logic.ClientToServerRequest.Request;
//
///**
// * TravelerControl class handles all the traveler related functionalities
// */
//@SuppressWarnings("rawtypes")
//public class TravelerControl {
//
//	/* כל מה שקשור ל-subscriber לא צריך
//	 * This function get an id and returns Subscriber object that match the given id.
//	 * 
//	 * @param id the id of the subscriber
//	 * @return Subscriber object, null if not exist
//	 
//	public static Subscriber getSubscriber(String id) {
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.GET_SUBSCRIBER,
//				new ArrayList<String>(Arrays.asList(id)));
//		ClientUI.clientControllerInstance.accept(request);
//		Subscriber subscriber = (Subscriber) SystemClient.serverResponse.getResultSet().get(0);
//		return subscriber;
//	}
//	*/
//
//	/** שיניתי פה
//	 * This function gets an id and check if there is a traveler 
//	 * that match this id.
//	 * 
//	 * @param id the id to check
//	 * @return true if the traveler exist in the system, false otherwise
//	 */
//	public static boolean isTravelerExist(String id) {
//		/* First we check if the id is subscriber */
//		ClientToServerRequest request = new ClientToServerRequest<>(Request.TRAVELER_LOGIN_ID,
//				new ArrayList<String>(Arrays.asList(id)));
//		ClientUI.clientControllerInstance.accept(request);
//		Traveler traveler = (Traveler) SystemClient.serverResponse.getResultSet().get(0);
//		if (traveler != null) // If not null means the traveler exist
//			return true;
//		return false;
//	}
//
//	/**
//	 * This function deletes a traveler from traveler table by his ID
//	 * 
//	 * @param id traveler ID
//	 */
//	public static void deleteFromTravelerTable(String id) {
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.DELETE_TRAVELER,
//				new ArrayList<String>(Arrays.asList(id)));
//		ClientUI.clientControllerInstance.accept(request);
//	}
//
//
//
//	/**
//	 * This function get an id and returns Traveler object that match the given id.
//	 * 
//	 * @param id - the id of the traveler
//	 * @return Traveler object
//	 */
//	public static Traveler getTraveler(String id) {
//		ClientToServerRequest request = new ClientToServerRequest<>(Request.TRAVELER_LOGIN_ID,
//				new ArrayList<String>(Arrays.asList(id)));
//		ClientUI.clientControllerInstance.accept(request);
//		Traveler traveler = (Traveler) SystemClient.serverResponse.getResultSet().get(0);
//		return traveler;
//	}
//
//}
