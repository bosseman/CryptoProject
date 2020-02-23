package server.clientObject;

public class ClientObject {
	private String userName;
	private String publicId;
	private String publicKey;
	private int userLastActive;
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public int getUserLastActive() {
		return userLastActive;
	}

	public void setUserLastActive(int userLastActive) {
		this.userLastActive = userLastActive;
	}

}
