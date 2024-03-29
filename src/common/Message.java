package common;

import java.time.LocalDate;

/**
 * Represents a message with an address, content, and sending date.
 */
public class Message {
	private String address;
	private String content;
	private LocalDate sendingDate;
	
	
	public Message(String address, String content, LocalDate sendingDate) {
		super();
		this.address = address;
		this.content = content;
		this.sendingDate = sendingDate;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LocalDate getSendingDate() {
		return sendingDate;
	}
	public void setSendingDate(LocalDate sendingDate) {
		this.sendingDate = sendingDate;
	}
	
	
	
}
