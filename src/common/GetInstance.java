package common;

import gui.OrderAVisitController;

/**
 * Singleton class to provide a single instance of OrderAVisitController.
 */
public class GetInstance {

	/**
	 * The instance of OrderAVisitController.
	 */
	private OrderAVisitController OrderC;
	

	/**
	 * Private constructor to prevent instantiation from outside the class.
	 */
	private final static GetInstance instance = new GetInstance();

	/**
	 * Retrieves the singleton instance of GetInstance.
	 *
	 * @return the singleton instance of GetInstance
	 */
	public static GetInstance getInstance() {
		return instance;
	}
	
	/**
	 * Retrieves the OrderAVisitController instance.
	 *
	 * @return the OrderAVisitController instance
	 */
	public OrderAVisitController getOrderC() {
		return OrderC;
	}
 
	/**
	 * Sets the OrderAVisitController instance.
	 *
	 * @param orderC the OrderAVisitController instance to be set
	 */
	public void setOrderC(OrderAVisitController orderC) {
		OrderC = orderC;
	}

}
