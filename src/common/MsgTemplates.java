package common;

/**
 * This class holds messages templates 
 *
 */
public class MsgTemplates {
	
	public static String [] orderConfirmation = {"Thank you for your order!", "Thank you for your order!\n"
			+ "We will send you a reminder one day before your visit.\n"
			+ "You need to confirm your order when you receive the reminder.\n\n"
			+ "Your order information:\n"
			+ "Order id: %s.\n"
			+ "Park: %s.\n"
			+ "Date: %s.\n"
			+ "Time: %s.\n"
			+ "Type: %s.\n"
			+ "Visitors: %s.\n"
			+ "Total price: %s.\n\n"
			+ "We will see you at the park,\n"
			+ "GoNature8 Family.\n\n"};



	public static String[] enterToWaitingList = { "Enter waiting list", "You are now in the waiting list\n"
			+ "We will send you an email if someone will cancel their visit\n" 
			+ "Order info:\n"
			+ "Park: %s\n"
			+ "Visit date: %s\n"
			+ "Visit time %s\n"
			+ "\n"
			+ "Thank you,\n"
			+ "GoNature8 Family" };
	
	public static String[] errorWaitingList = { "Error Waiting List", "There was error trying to put you in the waiting list.\n" 
	+ "Please try again later."
 };
	

	
	public static String [] ConfirmOrder24hoursBeforeVisit = { "Please Confirm your order",""
			+ "Hello,"
			+ "You have made an order for a visit in %s in %s at %s.\n"
			+ "Please confirm your order within two hours.\n"
			+ "NOTE: If you will not confirm your visit beforehand, your order will be automatically cancelled.\n\n"
			+ "Best Regards,\n"
			+ "GoNature8 Family."
			
	};
	
	public static String [] orderCancel = { "Your Order has been cancel",""
			+ "Hello,\n"
			+ "We would like to inform you that your visit order to %s in %s at %s was cancelled.\n\n"
			+ "Best Regards,\n"
			+ "GoNature8 Family."
			
	};
	
	public static String [] waitingListPlaceInPark = { "We have a spot for you in the park!", ""
			+ "Hello,\n"
			+ "We are happy to inform you that there was an opening for a visit while you were waiting!\n"
			+ "At %s park on %s at %s.\n"
			+ "If you would like to come at this time, please confirm.\n"
			+ "You have 1 hours to confirm the order before it automatically cancelled\n\n"
			+ "Best Regards,\n"
			+ "GoNature8 Family."		
			
	};
}
