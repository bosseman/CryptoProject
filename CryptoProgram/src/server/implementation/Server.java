package server.implementation;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private static final int PORT = 8080; //port
	private static ArrayList<ClientHandler> clients = new ArrayList<>(); //hold clients
	private static ArrayList<ClientHandler> clientsInChatRoom = new ArrayList<>(); //Clients in chat room
	private static ExecutorService pool = Executors.newFixedThreadPool(100); //Limit amount of clients
	
	public static void main(String[] args) throws IOException{
		ServerSocket listener = new ServerSocket(PORT); //Listening
			
		while(true) {

				Socket client = listener.accept(); //Accept client
				ClientHandler clientThread = new ClientHandler(client, clients); //Hand off client to handler
				
				clients.add(clientThread); //Manage all clients
				pool.execute(clientThread);
			}
		
	}
	
}
