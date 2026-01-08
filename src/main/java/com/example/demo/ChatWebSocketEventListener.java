package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChatWebSocketEventListener {
    private final ChatPresenceStore store;

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event){
        StompHeaderAccessor accessor=StompHeaderAccessor.wrap(event.getMessage());

        Map<String,Object> session=accessor.getSessionAttributes();
        if(session == null) return;

        Integer roomId = accessor.getSessionAttributes() != null
                ? (Integer) accessor.getSessionAttributes().get("roomId")
                : null;

        Integer userId = accessor.getSessionAttributes() != null
                ? (Integer) accessor.getSessionAttributes().get("userId")
                : null;

        if(roomId != null && userId != null){
            store.leaveRoom(roomId,userId);
            System.out.println("비정상 종료 감지: room = " + roomId + ", user = " + userId);
        }
    }
}
