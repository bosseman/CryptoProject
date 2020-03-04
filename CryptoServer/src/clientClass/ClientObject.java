package clientClass;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ClientObject {
	//---Save this------\\
	private String userName;
	private String publicId;
	private String password;
	
	//----Create these when client is active---\\
	private String publicKey;
	private String shareKey;
	//This get created when the client establishes a connection
	private Socket clientSocket;
	
	public ClientObject() {

	}
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


	public String getShareKey() {
		return shareKey;
	}
	public void setShareKey(String shareKey) {
		this.shareKey = shareKey;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Socket getClientSocket() {
		return clientSocket;
	}
	public void setClientSocket(Socket outToClient) {
		this.clientSocket = outToClient;
	}

	
	
}
