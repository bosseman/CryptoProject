package database;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import clientClass.ClientObject;
import clientClass.Conversation;

public class DatabaseImpl {
	
	private FileReader messageReader;
	private FileReader personReader;
	
	private FileWriter messageWriter;
	private FileWriter personWriter;
	
	private ArrayList<ClientObject> users;
	private ArrayList<Conversation> conversations;
	
	String convo = (new File("conversation.txt").getAbsolutePath());
	String person = (new File("person.txt").getAbsolutePath());
	
	public DatabaseImpl() throws IOException {
		
		users = new ArrayList<>();
		conversations = new ArrayList<>();
		readAllUsers();
		readAllConversations();
		
	}
	//---------------Search for a user -------------\\
	public boolean search(String userName) {
		boolean exist = false;
		for(ClientObject c : users) {
			if(c.getUserName().equals(userName)) {
				exist = true;
				return exist;
			}
		}
		return exist;
	}
	//---------------Load conversation---------------\\
	public ArrayList<Conversation> getConvo(String fromName, String toName){
		ArrayList<Conversation> thisConvo = new ArrayList<>();
		for(Conversation c: conversations) {
			if((c.getFromId().equals(fromName) && c.getToId().equals(toName)) || 
					(c.getFromId().equals(toName) && c.getToId().equals(fromName))) {
				thisConvo.add(c);
			}
		}
		return thisConvo;
	}
	
	//----------------Check username-------------------\\
	public boolean checkUsername(String userName) {
		boolean valid = true;
		for(ClientObject c: users) {
			if(c.getUserName().contentEquals(userName)) {
				valid = false;
				return valid;
			}
		}
		return valid;
	}
	
	//-----------------Validate user---------------------\\
	public ClientObject validate(String userName, String password, Socket clientSocket) {
		ClientObject thisClient = new ClientObject();
		for(ClientObject c: users) {
			if(c.getUserName().equals(userName)) {
				if(c.getPassword().contentEquals(password)) {
					thisClient.setUserName(c.getUserName());
					thisClient.setPublicId(c.getPublicId());
					thisClient.setClientSocket(clientSocket);
					return thisClient;
				}
			}
		}
		return null;
	}
	//---------------Save a message ----------------------\\
	public void writenewMessage(Conversation message) throws IOException{
		messageWriter = new FileWriter(convo, true);
		String fromId = message.getFromId();
		String toId = message.getToId();
		String said = message.getMessage();

		String saveThis = fromId + "~" + toId + "~" + said;
		BufferedWriter bw = new BufferedWriter(messageWriter);
		bw.write(saveThis);
		bw.close();
		conversations.add(message);
	}
	//-------------Save a new User -----------------------\\
	public void writeNewUser(ClientObject clientObject) throws IOException{
		personWriter = new FileWriter(person, true);
		String userName = clientObject.getUserName();
		String password = clientObject.getPassword();
		String publicId = clientObject.getPublicId();
		
		String saveThis = userName + "~" + password + "~" + publicId;
		BufferedWriter bw = new BufferedWriter(personWriter);
		bw.write(saveThis);
		bw.close();
		users.add(clientObject);
	}
	//---------------Load all users on load-----------------\\
	public void readAllUsers() throws IOException {
		personReader = new FileReader(person);
		BufferedReader br = new BufferedReader(personReader);
		String line;
		while((line = br.readLine()) != null) {
			String[] fields = line.split("~");
			
			ClientObject client = new ClientObject();
			client.setUserName(fields[0]);
			client.setPublicId(fields[1]);
			client.setPassword(fields[2]);
			
			users.add(client);
		}
		br.close();
	}
	//---------------------------------------------------------\\
	
	//-------------------Load all conversations------------------\\
	public void readAllConversations() throws IOException {
		messageReader = new FileReader(convo);
		BufferedReader br = new BufferedReader(messageReader);
		String line;
		while((line = br.readLine()) != null) {
			String[] fields = line.split("~");
			
			Conversation convo = new Conversation();
			
			convo.setFromId(fields[0]);
			convo.setToId(fields[1]);
			convo.setMessage(fields[2]);
			
			conversations.add(convo);
		}
		br.close();		
	}
	//---------------------------------------------------------\\
	public ArrayList<ClientObject> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<ClientObject> users) {
		this.users = users;
	}
	public ArrayList<Conversation> getConversations() {
		return conversations;
	}
	public void setConversations(ArrayList<Conversation> conversations) {
		this.conversations = conversations;
	}
	
}
