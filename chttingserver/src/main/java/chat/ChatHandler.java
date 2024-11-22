package chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChatHandler extends TextWebSocketHandler {

	private Map<String, List<WebSocketSession>> roomSessions = new HashMap<>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		String username = (String) session.getAttributes().get("username");
		String roomId = (String) session.getAttributes().get("roomId");
		log.info(username + "님이 입장하셨습니다.");
		
		if (!roomSessions.containsKey(roomId)) {
			roomSessions.put(roomId, new ArrayList<>());
		}
		roomSessions.get(roomId).add(session);
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		String username = (String) session.getAttributes().get("username");
		String roomId = (String) session.getAttributes().get("roomId");
		log.info(username + ":" + message.getPayload());

		List<WebSocketSession> sessions = roomSessions.get(roomId);
		if (sessions != null) {
			 for(WebSocketSession s : sessions) {
	        	  s.sendMessage(new TextMessage(username + ": " + message.getPayload()));
	        }
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		
		String username = (String) session.getAttributes().get("username");
		String roomId = (String) session.getAttributes().get("roomId");
		log.info(username + "님이 퇴장하셨습니다.");
		
		List<WebSocketSession> sessions = roomSessions.get(roomId);
		if (sessions != null) {
			sessions.remove(session);
			if (sessions.isEmpty()) {
				roomSessions.remove(roomId);
			}
		}
	}
}
