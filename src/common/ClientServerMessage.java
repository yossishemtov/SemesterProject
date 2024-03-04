package common;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientServerMessage<T> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//A generic class that is meant to handle data and command from server and client in a generic way
	private Object dataTransfered;
	private String command;
	
	public ClientServerMessage(Object dataTransfered, String command) {
		this.dataTransfered = dataTransfered;
		this.command = command;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<T> convertDataToArrayList() throws Exception{
		//Converting a generic data of some kind of objects to arrayList of them
		return (ArrayList<T>)this.dataTransfered;
	}
	
	@Override
	public String toString() {
		//Returning a string representation of the command and data stored in dataTranfered object
		if((String)dataTransfered != null) {
			return "Operation: " + command.toString() + " Input ArrayList : " + dataTransfered.toString();
		}
		
		return "Operation: " + command.toString() + " Input ArrayList : " + "NULL";
	}
	
	@SafeVarargs
	public static<T> ArrayList<T> createDataArrayList(T... dataObject){
		//Generic method to create an arrayList from any kind of dataobjects provided
		
		ArrayList<T> data = new ArrayList<>();
		for(T param : dataObject) {
			data.add(param);
		}
		
		return data;
		
	}

	public Object getDataTransfered() {
		return dataTransfered;
	}

	public void setDataTransfered(Object dataTransfered) {
		this.dataTransfered = dataTransfered;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	
	
}
