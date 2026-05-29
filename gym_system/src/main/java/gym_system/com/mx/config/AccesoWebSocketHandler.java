package gym_system.com.mx.config;

import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class AccesoWebSocketHandler extends TextWebSocketHandler{
	
	private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception{
		sessions.add(session);
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception{
		sessions.remove(session);
	}
	
	public void mandarSeñaleAcceso(String jsonDatosSocio) {
		for(WebSocketSession session : sessions) {
			if(session.isOpen()) {
				try {
					session.sendMessage(new TextMessage(jsonDatosSocio));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
