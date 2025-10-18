package com.hellojenkins.app.genkins.websocket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class JenkinsWebSocketHandler extends TextWebSocketHandler {
    // 일반 이벤트용 세션
    private final Set<WebSocketSession> eventSessions = ConcurrentHashMap.newKeySet();
    // 개요(overview)용 세션
    private final Set<WebSocketSession> overviewSessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Set<WebSocketSession> sessions = this.getSessionSet(session);
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	Set<WebSocketSession> sessions = this.getSessionSet(session);
    	sessions.remove(session);
    }

    /** 일반 이벤트 메시지 전송 */
    public void sendEventMessage(String message) {
        this.sendToSessions(eventSessions, message);
    }

    /** Overview 메시지 전송 */
    public void sendOverviewMessage(String message) {
        this.sendToSessions(overviewSessions, message);
    }

    /** 공통 메시지 전송 로직 */
    private void sendToSessions(Set<WebSocketSession> sessions, String message) {
        sessions.forEach(session -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
   private Set<WebSocketSession> getSessionSet(WebSocketSession session) {
       // 연결된 URI에 따라 세션을 분류
       String path = session.getUri() != null ? session.getUri().getPath() : "";
       if (path.endsWith("/jenkins")) {
           return eventSessions;
//       } else if (path.endsWith("/overview")) {
       }
       else {   
           return overviewSessions;
       }
   }
}
