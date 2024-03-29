package common;

import gui.OrderAVisitController;

/**
 * GetInstance is a singleton class used to provide access to the OrderAVisitController instance.
 */

public class GetInstance {

	private OrderAVisitController OrderC;
	
    // Singleton instance of GetInstance
	private final static GetInstance instance = new GetInstance();

	public static GetInstance getInstance() {
		return instance;
	}
	public OrderAVisitController getOrderC() {
		return OrderC;
	}
 
	public void setOrderC(OrderAVisitController orderC) {
		OrderC = orderC;
	}

}
