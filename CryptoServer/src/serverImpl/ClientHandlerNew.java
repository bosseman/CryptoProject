package serverImpl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import clientClass.ClientObject;
import clientClass.Conversation;
import database.DatabaseImpl;
import serverImpl.GenerateRandomStuff;

public class ClientHandlerNew implements Runnable {

	private Socket client;
	private BufferedReader in;
	public DataOutputStream out;
	private ArrayList<ClientHandlerNew> onlineClients;
	private DatabaseImpl db;
	public ClientObject thisClient;
	private ChatRoom chatRoom;
	private String[] request;
	private String response;
	private ArrayList<String> usersLogin;
	public String thisUserName;
	
	public ClientHandlerNew(Socket clientSocket, ArrayList<ClientHandlerNew> onlineClients, DatabaseImpl db,
			ChatRoom chatRoom, ArrayList<String> usersLogin ) throws IOException {
		this.usersLogin = usersLogin;
		this.chatRoom = chatRoom;
		this.db = db;
		this.client = clientSocket;
		this.onlineClients = onlineClients;
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new DataOutputStream(client.getOutputStream());
		thisClient = new ClientObject();
	}
 
	// Hand shake is completed.
	@Override
	public void run() {
		try {

			// 1. Save clients public key

			// 2. Send server public key

			// 3. Client either attempts to log in or registers an account
			boolean loggedIn = false;

			while (!loggedIn) {
				request = prepareStatement(in.readLine());
				if (request[0].equals("authentication")) { // authentication~<username>~<password>
					String safePassword = securePassword(request[2]);
					loggedIn = login(request[1], safePassword, client);
					if (loggedIn) {
						response = safeTransmission("authentication~true", thisClient);
					} else {
						response = safeTransmission("authentication~false", thisClient);
					}
				} else if (request[0].contentEquals("register")) {
					loggedIn = createAccount(request[1], request[2], client);
				} else {
					response = safeTransmission("authentication~false~Invalid Request", thisClient);
				}
				out.writeBytes(response + '\n');
			}

			// ----------------------------------------------------------------------------\\
			while (loggedIn) {
				request = prepareStatement(in.readLine());
				switch(request[0]) {
				
				// 1. Search for a user
				case "search":
					//This opens a convo, stay until requested to leave
					response = loadConvo(request[1]);
					response = safeTransmission(response, thisClient);
					out.writeBytes(response+'\n');
					while(true) {
						request = prepareStatement(in.readLine());
						if(request[0].equals("send")) {
							boolean finished = sendMessage(request[1], request[2]);
						}else {
							break;
						}
					}
					response = "search~false";
					break;
				// 2. View all online users
				case "onlineUser":
					response = getOnlineUsers();
					break;
				// 3. View all users
				case "allusers":
					response  = allusers();
					break;
				// 4. Enter the chat room
				case "chatroom":
					chatRoom.addToChatRoom(thisClient);
					response = loadChatRoom();
					response = safeTransmission(response, thisClient);
					out.writeBytes(response+'\n');
					while(true) {
						request = prepareStatement(in.readLine());
						if(request[1].equals("send")) {
							 broadCast(request[1]);
						}else {
							chatRoom.removeFromChatRoom(thisClient);
							break;
						}
						response = "chatroom~false";
					}
					break;
				// 5. log off
				default:
					loggedIn = false;
				}
				response = safeTransmission(response, thisClient);
				out.writeBytes(response+'\n');
			}
			// ----------Error handling-----------------\\
		} catch (IOException e) {
			onlineClients.remove(thisClient);
		} finally {
			try {
				out.close();
				in.close();
				onlineClients.remove(thisClient);
			} catch (IOException e1) {
				onlineClients.remove(thisClient);
			}
		}
		// ------------------------------------------\\
	}
	//Loads all clients from the chat room
	private String loadChatRoom() {
		String returnThis = "user~";
		ArrayList<ClientObject> chatRoomUsers = (ArrayList<ClientObject>) chatRoom.getChatRoomClients();
		for( ClientObject s : chatRoomUsers) {
			if(!s.equals(thisClient.getUserName())) {
				returnThis.concat(s.getUserName());
				returnThis.concat("|");
			}
		}
		
		return returnThis;	
	}
	// Encrypts the user password
	private String securePassword(String password) {
		String returnThis;

		returnThis = password;

		return returnThis;
	}

	// Calls all methods needed for decryption
	private String[] prepareStatement(String message) {
		String[] returnThis;

		String temp = reverseCBC(message);

		temp = decrypt(temp);

		returnThis = parseMessage(temp);

		return returnThis;
	}
	private String allusers() {
		String returnThis = "user~";
		ArrayList<ClientObject> all = db.getUsers();
		for( ClientObject s : all) {
			if(!s.equals(thisClient.getUserName())) {
				returnThis.concat(s.getUserName());
				returnThis.concat("|");
			}
		}
		
		return returnThis;		
	}
	private String getOnlineUsers() {
		String returnThis = "user~";
		
		for(String s : usersLogin) {
			if(!s.equals(thisClient.getUserName())) {
				returnThis.concat(s);
				returnThis.concat("|");
			}
		}
		
		return returnThis;
	}
	// Calls all method needed for transmission
	private String safeTransmission(String message, ClientObject sending) {
		String returnThis;

		returnThis = encrypt(message, sending);

		returnThis = cbc(message);

		return returnThis;
	}

	// Create a account
	private boolean createAccount(String userName, String password, Socket sock) throws IOException {
		boolean successful = false;

		successful = usernameValidation(userName);

		if (successful) {
			thisClient.setUserName(userName);
			thisClient.setPassword(password);
			thisClient.setPublicId(GenerateRandomStuff.generateKey(10));
			thisClient.setClientSocket(sock);
			db.writeNewUser(thisClient);
		}

		return successful;
	}

	// Load conversation
	private String loadConvo(String userName) {
		List<Conversation> thisConvo = new ArrayList<>();
		String returnThis = "convo~";
		if (searchForUser(userName)) {
			returnThis.concat("true|");
			 thisConvo = db.getConversations();
			 for(Conversation c: thisConvo) {
				 returnThis.concat("toId:");
				 returnThis.concat(c.getToId());
				 returnThis.concat("^fromId");
				 returnThis.concat(c.getFromId());
				 returnThis.concat("^message:");
				 returnThis.concat(c.getMessage());
				 returnThis.concat("|");
				 
			 }
		}else {
			returnThis.concat("false");
		}
		return returnThis;
	}

	// Search for a user
	private boolean searchForUser(String userName) {

		boolean exist = db.search(userName);

		return exist;
	}

	// Username validation for making new account
	private boolean usernameValidation(String userName) {
		boolean approved = false;

		approved = db.checkUsername(userName);

		return approved;
	}

	// account validation
	private boolean login(String userName, String password, Socket sock) {
		boolean approved = false;

		ClientObject temp = db.validate(userName, password, sock);

		if (temp != null) {
			approved = true;
			thisClient.setUserName(temp.getUserName());
			thisClient.setPublicId(temp.getPublicId());
			usersLogin.add(thisClient.getUserName());
			thisUserName = userName;
		}

		return approved;
	}

	// Reverse CBC
	private String reverseCBC(String message) {
		String reverseCBC = message;

		return message;
	}

	// Cypher block chaining
	private String cbc(String message) {
		String shuffle = message;
		// Logic goes here
		return message;
	}

	// Method for decrypting the message
	private String decrypt(String message) {
		String decrypted = message;
		// Logic goes here
		return message;
	}

	// Parses the message, return back an array
	private String[] parseMessage(String message) {
		String[] returnThis = message.split("~");

		return returnThis;
	}

	// Encrypts the message
	private String encrypt(String message, ClientObject sendTo) {
		String encrypted = message;
		// Logic will go here
		return encrypted;
	}
	private boolean sendMessage(String userName, String message) throws IOException {
		Conversation c = new Conversation();
		c.setFromId(thisClient.getUserName());
		c.setToId(userName);
		c.setMessage(message);
		db.writenewMessage(c);
		
		for(ClientHandlerNew ch: onlineClients) {
			if(ch.thisClient.equals(userName)) {
				String sending = "sent~"+userName+"~"+message;
				sending = safeTransmission(sending, ch.thisClient);
				ch.out.writeBytes(sending);
			}
		}
		return true;
	}
	
	// --------Method for broadcasting a message-------\\
	private void broadCast(String words) throws IOException {
		for (ClientObject a : chatRoom.getChatRoomClients()) {
			String broadCastThis = safeTransmission(words, a);
			DataOutputStream sending = new DataOutputStream(a.getClientSocket().getOutputStream());
			sending.writeBytes(broadCastThis + '\n');
			sending.close();
		}
	}

}
