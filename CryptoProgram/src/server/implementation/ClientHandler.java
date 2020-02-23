package server.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import server.clientObject.ClientObject;
import server.utils.Utils;
import server.security.SecurityImpl;

public class ClientHandler implements Runnable{
	
	private Socket clientSocket; //client socket
	private BufferedReader input; //Request
	private PrintWriter output; //Response
	private static ArrayList<ClientHandler> clients; //active clients for broadcasting
	private static ClientObject clientObject; //This user
	private static Utils utils;	//Util functions
	private static String responseMessage; //Response
	

	
	public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients) throws IOException {
		this.clientSocket = clientSocket;
		input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		output = new PrintWriter(clientSocket.getOutputStream(), true);
		this.clients = clients;
		clientObject = null; //By default
		utils = new Utils();
	}

	@Override
	public void run() {
		
		//Key Exchange
		String[] request;
		try {
			//------KEY EXCHANGE---NOT ENCRYPTED--------
			request = utils.parseString(input.readLine()); 
			clientObject.setPublicKey(request[2]);		
			output.write(utils.publicKeyTransfer());
			//------------END OF KEY EXCHANGE -----------
			
			//--------ENCRYPTION BEGINS ------------------
		
			//---------LOGIN OR REGISTER------------------

			//-----------VALIDATE USER -------------------
			
			//-------------REGISTER ACCOUNT ---------------

		} catch (IOException e) {
			e.printStackTrace();
		}
		//------------SUCCESSFULLY LOGGED INTO SYSTEM---------
		while(true) {
			//--------------MAIN MENU-------------------------
			
			//------------SEARCH FOR USER---------------------
			
			//------------SINGLE CONVERSTATION----------------
			
			//------------GROUP CONERSATION -------------------
			
			//---------------LOGOUT----------------------------
		}
	}
}
