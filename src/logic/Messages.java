package logic;

import java.io.Serializable;

/**
 * This class is responsible for the system's messages.
 */
@SuppressWarnings("serial")
public class Messages implements Serializable {
	private int messageId;
	private String toId;
	private String sendDate;
	private String subject;
	private String content;
	private int orderId;
	private String sendTime;

	
	public Messages(int messageId, String toId, String sendDate, String sendTime, String subject, String content,int orderId) {
		this.messageId = messageId;
		this.toId = toId;
		this.sendDate = sendDate;
		this.subject = subject;
		this.content = content;
		this.sendTime=sendTime;
		this.orderId=orderId;
	}
	
	
	public int getOrderId() {
		return orderId;
	}


	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}


	public String getSendTime() {
		return sendTime;
	}


	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}


	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
}