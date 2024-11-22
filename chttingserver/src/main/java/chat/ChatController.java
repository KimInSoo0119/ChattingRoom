package chat;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChatController {
	
	Logger logger = LoggerFactory.getLogger(ChatController.class);
	
	@Autowired
	ChatService chatService;
 
    @GetMapping(value="/chat")
    public String getChatPage() {
        return "chat";
    }
    
    @GetMapping(value = "/roomSelection")
    public String getRoomSelectionPage(HttpSession session, @RequestParam String username, Model model) {
        if (username.contains(" ") || username.trim().isEmpty()) {
            model.addAttribute("Username cannot be empty or contain spaces.");
            return "chat"; 
        } else {            	
        	session.setAttribute("username", username);
        	model.addAttribute("username", username);
        	return "roomSelection"; 
        }
    }
    
    @PostMapping(value="/createRoom")
    public @ResponseBody ResponseEntity<String> createRoom(HttpSession session, @RequestParam String roomId) {
        if (roomId.contains(" ") || roomId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("roomId cannot be empty or contain spaces.");
        } else {     
            String username = (String) session.getAttribute("username");
            chatService.createRooms(roomId);
            return ResponseEntity.ok("방이 성공적으로 생성되었습니다. 방 번호: " + roomId + " 사용자 계정: " + username);
        }
    }

    @GetMapping(value="/getChatRooms")
    public @ResponseBody ResponseEntity<List<ChatRoom>> getChatRooms() {
    	List<ChatRoom> rooms = chatService.getAllRooms();
    	return ResponseEntity.ok(rooms);
    }
    
    @GetMapping(value="/joinRoom")
    public String joinRoom(HttpSession session, @RequestParam String roomId, @RequestParam String username,
    		Model model) {
    	session.setAttribute("roomId", roomId);
    	session.setAttribute("username", username);
    	model.addAttribute("roomId", roomId);
    	model.addAttribute("username", username);
    	return "joinRoom";
    }
}

