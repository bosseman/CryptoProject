package clientClass;

import java.sql.Date;

public class Conversation {
	

	private String toId;
	private String fromId;
	private String message;

	
	public Conversation() {
		
	}
	 

	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
