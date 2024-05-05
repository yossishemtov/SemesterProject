package common;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Represents a message exchanged between client and server, containing data and a command.
 *
 * @param <T> the type of data contained in the message
 */
public class ClientServerMessage<T> implements Serializable{
	
	/**
	 * The serial version UID for serialization.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The data transferred in the message.
	 */
	
	private Object dataTransfered;
	
	/**
	 * The command associated with the message.
	 */
	private String command;
	
	/**
	 * A flag indicating a certain condition.
	 */
	private Boolean flag;
	

	/**
	 * Constructs a new ClientServerMessage with the specified data and command.
	 *
	 * @param dataTransfered the data to be transferred
	 * @param command the command associated with the message
	 */
	public ClientServerMessage(Object dataTransfered, String command) {
		this.dataTransfered = dataTransfered;
		this.command = command;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Converts the data to an ArrayList.
	 *
	 * @return an ArrayList containing the data
	 * @throws Exception if an error occurs during conversion
	 */
	public ArrayList<T> convertDataToArrayList() throws Exception{
		//Converting a generic data of some kind of objects to arrayList of them
		return (ArrayList<T>)this.dataTransfered;
	}
	
	@Override
	/**
	 * Returns a string representation of the message.
	 *
	 * @return a string representation of the message
	 */
	public String toString() {
	    // Check for null directly without casting
	    String dataString = (dataTransfered != null) ? dataTransfered.toString() : "NULL";
	    return "Operation: " + command + " | Data: " + dataString;
	}

	
	@SafeVarargs
	/**
	 * Creates an ArrayList from the provided data objects.
	 *
	 * @param dataObject the data objects to include in the ArrayList
	 * @param <T> the type of data objects
	 * @return an ArrayList containing the data objects
	 */
	public static<T> ArrayList<T> createDataArrayList(T... dataObject){
		//Generic method to create an arrayList from any kind of dataobjects provided
		
		ArrayList<T> data = new ArrayList<>();
		for(T param : dataObject) {
			data.add(param);
		}
		
		return data;
		
	}

	/**
	 * Retrieves the data transferred in the message.
	 *
	 * @return the data transferred
	 */
	public Object getDataTransfered() {
		return dataTransfered;
	}

	/**
	 * Sets the data to be transferred in the message.
	 *
	 * @param dataTransfered the data to be transferred
	 */
	public void setDataTransfered(Object dataTransfered) {
		this.dataTransfered = dataTransfered;
	}
	
	/**
	 * Sets the flag to false.
	 */
	public void setflagFalse() {
		this.flag=false;
	}
	
	/**
	 * Sets the flag to true.
	 */
	public void setflagTrue() {
		this.flag=true;
	}
	
	/**
	 * Retrieves the flag value.
	 *
	 * @return the flag value
	 */
	public Boolean getFlag() {
		return flag;
	}
	
	/**
	 * Retrieves the command associated with the message.
	 *
	 * @return the command associated with the message
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Sets the command associated with the message.
	 *
	 * @param command the command to be set
	 */
	public void setCommand(String command) {
		this.command = command;
	}
	
	
}
