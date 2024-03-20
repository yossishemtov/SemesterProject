package common;

import gui.OrderAVisitController;

public class GetInstance {

	private OrderAVisitController OrderC;
	

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
