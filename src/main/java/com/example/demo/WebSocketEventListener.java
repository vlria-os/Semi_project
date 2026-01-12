package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final ChatPresenceStore store;
    private final SimpMessagingTemplate template;

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> session = accessor.getSessionAttributes();
        if (session == null) return;

        Object roomIdObj = session.get("roomId");
        Object userIdObj = session.get("userId");
        if (roomIdObj == null || userIdObj == null) return;

        int roomId = (int) roomIdObj;
        int userId = (int) userIdObj;

        // ✅ 유령 제거
        store.leaveRoom(roomId, userId);

        // ✅ presence 갱신 브로드캐스트
        template.convertAndSend("/topic/presence/" + roomId, store.getUserCount(roomId));
    }
}

