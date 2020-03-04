package serverImpl;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import database.DatabaseImpl;


public class ServerStart {
	
	private static final int PORT = 8081;
	public static ArrayList<ClientHandlerNew> onlineClients = new ArrayList<>();
	public static ExecutorService pool = Executors.newFixedThreadPool(100);
	public static ChatRoom chatRoom = new ChatRoom();
	public static ArrayList<String> usersLogin = new ArrayList<>();
	
	public static void main(String[] args) throws IOException, SQLException{
		//Load database
		DatabaseImpl bd = new DatabaseImpl();
		System.out.println("Database has been loaded in.");
		
		ServerSocket listener = new ServerSocket(PORT);
		
		while(true) {
			Socket client = listener.accept();
			System.out.println("Client has been accepted.");
			
			
			//-------Hand off the client --------\\
			ClientHandlerNew clientThread = new ClientHandlerNew(client, onlineClients, bd, chatRoom, usersLogin);
			
			
			onlineClients.add(clientThread);
			pool.execute(clientThread);
			
		}

	}
}
