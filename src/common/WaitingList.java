package common;

import java.io.Serializable;

public class WaitingList  implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer count;
	
	public WaitingList(Integer count) {
		this.count = count;
	}
}
