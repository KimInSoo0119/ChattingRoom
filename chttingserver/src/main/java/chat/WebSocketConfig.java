package chat;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatHandler chatHandler = new ChatHandler();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler, "/ws/rooms/{roomId}") 
                .addInterceptors(new HttpSessionHandshakeInterceptor() {
                    @Override 
                    public boolean beforeHandshake(
                        ServerHttpRequest request, ServerHttpResponse response, 
                        WebSocketHandler wsHandler, Map<String, Object> attributes
                    ) throws Exception {
                        // URL 경로에서 roomId 추출하여 세션 속성에 추가
                        String path = ((ServletServerHttpRequest) request).getServletRequest().getRequestURI();
                        String roomId = path.split("/ws/rooms/")[1];
                        attributes.put("roomId", roomId);

                        if (request instanceof ServletServerHttpRequest) {
                            HttpSession httpSession = ((ServletServerHttpRequest) request).getServletRequest().getSession(false);
                            if (httpSession != null) {
                                String username = (String) httpSession.getAttribute("username");
                                if (username != null) {
                                    attributes.put("username", username);
                                }
                            }
                        }

                        return super.beforeHandshake(request, response, wsHandler, attributes);
                    }
                })
                .setAllowedOrigins("*");
    }
}
