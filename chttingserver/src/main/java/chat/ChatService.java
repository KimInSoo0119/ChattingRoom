package chat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class ChatService {

	private ConcurrentHashMap<String, ChatRoom> chatRooms = new ConcurrentHashMap<>();
	
	public void createRooms(String roomId) {
		chatRooms.put(roomId, new ChatRoom(roomId));	
	}
	
	public List<ChatRoom> getAllRooms() {
		return new ArrayList<>(chatRooms.values());
	}
}
