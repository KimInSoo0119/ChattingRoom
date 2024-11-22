package chat;

public class ChatRoom {
	
	String roomId;
	String roomName;
	
	public ChatRoom(String roomId) {
		this.roomId = roomId;
		this.roomName = "CHATROOM" + roomId;
	}
	
	public String getRoomId() {
		return roomId;
	}
	
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	public String getRoomName() {
		return roomName;
	}
	
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	
	
}
