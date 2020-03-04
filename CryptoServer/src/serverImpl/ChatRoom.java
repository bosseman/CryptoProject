package serverImpl;

import java.util.ArrayList;
import java.util.List;

import clientClass.ClientObject;

public class ChatRoom {
	private List<ClientObject> chatRoomClients = new ArrayList<>();
	
	public ChatRoom() {
		
	}

	public List<ClientObject> getChatRoomClients() {
		return chatRoomClients;
	}

	public void setChatRoomClients(List<ClientObject> chatRoomClients) {
		this.chatRoomClients = chatRoomClients;
	}
	
	public void addToChatRoom(ClientObject client) {
		this.chatRoomClients.add(client);
	}
	
	public void removeFromChatRoom(ClientObject client) {
		this.chatRoomClients.remove(client);
	}
}
