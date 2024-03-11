package logic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a response from the server to the client
 */
@SuppressWarnings("serial")
public class ServerToClientResponse<T> implements Serializable {

	private boolean result;
	private int rowsAffected;
	private ArrayList<T> resultSet = new ArrayList<T>();
	private int arrayListSize;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public int getRowsAffected() {
		return rowsAffected;
	}

	public void setRowsAffected(int rowsAffected) {
		this.rowsAffected = rowsAffected;
	}

	public ArrayList<T> getResultSet() {
		return resultSet;
	}

	public void setResultSet(ArrayList<T> resultSet) {
		this.resultSet = resultSet;
	}

	public int getArrayListSize() {
		return arrayListSize;
	}

	public void setArrayListSize(int arrayListSize) {
		this.arrayListSize = arrayListSize;
	}

}
